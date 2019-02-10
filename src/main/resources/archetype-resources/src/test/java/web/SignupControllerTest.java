#set( $dollar = '$' )
#set( $pound = '#' )
#set( $escape = '\' )
package ${package}.web;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import ${package}.config.WebSecurityConfigurationAware;

@Tag("controller")
public class SignupControllerTest extends WebSecurityConfigurationAware {
    
  /**
   * Test method for {@link ${package}.web.SignupController${pound}signup(org.spring.webapp.dto.SignupForm, org.springframework.validation.Errors, org.springframework.web.servlet.mvc.support.RedirectAttributes)}.
   */
  @Test
  @DisplayName("show signup form")
  public void displaySignupForm() throws Exception {
    mockMvc.perform(get("/signup").with(csrf().asHeader()))
      .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
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
    .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
    .andExpect(status().isFound())
    .andExpect(redirectedUrl("/"))
    ;
  }
}
