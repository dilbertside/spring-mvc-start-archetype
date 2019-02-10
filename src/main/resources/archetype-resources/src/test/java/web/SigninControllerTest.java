package ${package}.web;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
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
      .andDo(print())
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
    .andDo(print())
    .andExpect(status().isFound())
    .andExpect(redirectedUrl("/"))
    ;
  }
}
