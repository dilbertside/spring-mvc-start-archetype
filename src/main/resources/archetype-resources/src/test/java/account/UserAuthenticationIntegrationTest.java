package ${package}.account;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ${package}.config.WebSecurityConfigurationAware;

public class UserAuthenticationIntegrationTest extends WebSecurityConfigurationAware {

    private static String SEC_CONTEXT_ATTR = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

    @Test
    @DisplayName("user not authenticated requires signin")
    public void requiresAuthentication() throws Exception {
        mockMvc.perform(get("/account/current"))
                .andExpect(redirectedUrl("http://localhost/signin"));
    }

    @Test
    @DisplayName("user authenticate success")
    public void userAuthenticates() throws Exception {
        final String username = "user";

        mockMvc.perform(post("/authenticate").with(csrf()).param("username", username).param("password", "user"))
                .andExpect(redirectedUrl("/"))
                .andExpect(r -> Assertions.assertEquals(((SecurityContext) r.getRequest().getSession().getAttribute(SEC_CONTEXT_ATTR)).getAuthentication().getName(), username));

    }

    @Test
    @DisplayName("user authentication has failed correctly with wrong password")
    public void userAuthenticationFails() throws Exception {
        final String username = "user";
        mockMvc.perform(post("/authenticate").with(csrf()).param("username", username).param("password", "invalid"))
                .andExpect(redirectedUrl("/signin?error=1"))
                .andExpect(r -> Assertions.assertNull(r.getRequest().getSession().getAttribute(SEC_CONTEXT_ATTR)));
    }
}
