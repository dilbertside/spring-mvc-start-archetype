package ${package}.web;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;

import ${package}.config.WebSecurityConfigurationAware;

@Tag("controller")
public class SigninControllerTest extends WebSecurityConfigurationAware {
    
  /**
   * Test method for {@link ${package}.web.SigninController#signin()}.
   */
  @Test
  @DisplayName("show signin form")
  public void displaySigninForm() throws Exception {
    mockMvc.perform(get("/signin").with(csrf().asHeader()))
      .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
      .andExpect(status().isOk())
      .andExpect(view().name("signin/signin"))
      .andExpect(content().string(
          allOf(
              containsString("<title>Sign In</title>"),
              containsString("<legend>Please Sign In</legend>")
          ))
      );
  }
  
  @Test
  @DisplayName("proceed authenticate form")
  public void signinForm() throws Exception {
    mockMvc.perform(post("/authenticate")
        .with(csrf().asHeader())
        .param("username", "user")
        .param("password", "user")
      )
    .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
    .andExpect(status().isFound())
    .andExpect(redirectedUrl("/"))
    .andExpect(authenticated().withUsername("user").withRoles("API", "USER"))
    ;
  }
  
  @Test
  @DisplayName("proceed authenticate form wrong password")
  public void signinFormWrongPassword() throws Exception {
    mockMvc.perform(post("/authenticate")
        .with(csrf().asHeader())
        .param("username", "user")
        .param("password", "invalid")
      )
    .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
    .andExpect(status().isFound())
    .andExpect(redirectedUrl("/signin?error=1"))
    .andExpect(unauthenticated())
    ;
  }
}
