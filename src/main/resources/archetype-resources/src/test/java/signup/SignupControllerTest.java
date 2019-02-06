package ${package}.signup;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import ${package}.config.WebSecurityConfigurationAware;

@Tag("controller")
public class SignupControllerTest extends WebSecurityConfigurationAware {
    
  @Test
  @DisplayName("show signup form")
  public void displaysSignupForm() throws Exception {
      mockMvc.perform(get("/signup").with(csrf().asHeader()))
          .andExpect(model().attributeExists("signupForm"))
          .andExpect(view().name("signup/signup"))
          .andExpect(content().string(
                  allOf(
                      containsString("<title>Signup</title>"),
                      containsString("<legend>Please Sign Up</legend>")
                  ))
          );
  }
}
