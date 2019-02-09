package ${package}.signup;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import ${package}.config.WebSecurityConfigurationAware;

@Tag("controller")
public class SignupControllerTest extends WebSecurityConfigurationAware {
    
  /**
   * Test method for {@link ${package}.web.SignupController#signup(org.spring.webapp.dto.SignupForm, org.springframework.validation.Errors, org.springframework.web.servlet.mvc.support.RedirectAttributes)}.
   */
  @Test
  @DisplayName("show signup form")
  public void displaySignupForm() throws Exception {
    mockMvc.perform(get("/signup").with(csrf().asHeader()))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("signupForm"))
      .andExpect(view().name("signup/signup"))
      .andExpect(content().string(
          allOf(
              containsString("<title>Signup</title>"),
              containsString("<legend>Please Sign Up</legend>")
          ))
      );
  }
  
  @Test
  @DisplayName("proceed signup form")
  public void signupForm() throws Exception {
    mockMvc.perform(post("/signup")
        .with(csrf().asHeader())
        .param("email", "john@doe.com")
        .param("password", "doe")
      )
    .andDo(print())
    .andExpect(status().isFound())
    .andExpect(redirectedUrl("/"))
    ;
  }
}
