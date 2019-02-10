package ${package}.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ${package}.config.WebSecurityConfigurationAware;

public class UserAuthenticationIntegrationTest extends WebSecurityConfigurationAware {

    private static String SEC_CONTEXT_ATTR = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

    final String username = "user";

    @Test
    @DisplayName("user not authenticated requires signin")
    public void requiresAuthentication() throws Exception {
      mockMvc.perform(get("/account/current"))
        .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
        .andExpect(redirectedUrl("http://localhost/signin"));
    }

    @Test
    @DisplayName("user authenticate success")
    public void userAuthenticates() throws Exception {

      mockMvc.perform(post("/authenticate").with(csrf()).param("username", username).param("password", "user"))
        .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
        .andExpect(redirectedUrl("/"))
        .andExpect(r -> Assertions.assertEquals(((SecurityContext) r.getRequest().getSession().getAttribute(SEC_CONTEXT_ATTR)).getAuthentication().getName(), username));

    }

    @Test
    @DisplayName("user authentication has failed correctly with wrong password")
    public void userAuthenticationFails() throws Exception {
      mockMvc.perform(post("/authenticate").with(csrf()).param("username", username).param("password", "invalid"))
        .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
        .andExpect(redirectedUrl("/signin?error=1"))
        .andExpect(r -> Assertions.assertNull(r.getRequest().getSession().getAttribute(SEC_CONTEXT_ATTR)));
    }
    
    @Test
    @DisplayName("user form login with security context")
    public void userFormLogin() throws Exception {
      mockMvc.perform(formLogin("/authenticate").user(username).password("user"))
        .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
        .andExpect(authenticated().withUsername(username))
        .andExpect(authenticated().withAuthentication(auth ->
          assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class)));
    }
    
    @Test
    @DisplayName("user form login failed with security context")
    public void userFormLoginError() throws Exception {
      mockMvc.perform(formLogin("/authenticate").user(username))
        .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
        .andExpect(unauthenticated());
    }
    
    @Test
    @DisplayName("user form logoff")
    @WithMockUser(password="user")
    public void userFormLogout() throws Exception {
      mockMvc.perform(logout("/logout"))
        .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
        .andExpect(unauthenticated())
        .andExpect(redirectedUrl("/signin?logout"));
    }
    
    @Test
    @DisplayName("user form logoff not authenticated")
    public void userLogout() throws Exception {
      mockMvc.perform(logout("/logout"))
        .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
        .andExpect(unauthenticated())
        .andExpect(redirectedUrl("/signin?logout"));
    }
    
}
