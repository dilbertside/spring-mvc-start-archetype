#set( $dollar = '$' )
#set( $pound = '#' )
#set( $escape = '\' )
package ${package}.web;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.RequestDispatcher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ${package}.config.WebSecurityConfigurationAware;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

@Tag("controller")
public class ErrorControllerTest extends WebSecurityConfigurationAware {
    
  
  /**
   * Test method for {@link ${package}.web.ErrorController${pound}generalError(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.ui.Model)}.
   * 
   * @throws Exception
   */
  @Test
  @DisplayName("general error")
  @WithMockUser(password="user")
  public void generalError() throws Exception {
    mockMvc.perform(get("/generalError")
        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value())
        .requestAttr(RequestDispatcher.ERROR_EXCEPTION, new NullPointerException("truly bad error"))
        )
      .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("errorMessage"))
      .andExpect(view().name("error/general"))
      .andExpect(content().string(
          allOf(
              containsString("<title>Error page</title>"),
              containsString("truly bad error")
          ))
      );
  }
  
  /**
   * 
   * TODO Improvement it should be redirected to /generalError but as it runs outside of a web server container, it returns empty page
   */
  @Test
  @DisplayName("error bad request")
  @WithMockUser(password="user")
  public void errorBadRequest() throws Exception {
    this.mockMvc
        .perform(post("/bad"))
        .andDo(log())  // to activate change logger ".result" level to DEBUG in logback-test.xml
        .andExpect(status().isForbidden())
        //.andExpect(redirectedUrl("/generalError"))
    ;
  }

  /**
   * 
   * TODO Improvement it should be redirected to /generalError but as it runs outside of a web server container, it returns empty page
   */
  @Test
  @DisplayName("error page not found")
  @WithMockUser(password="user")
  public void errorNotFound() throws Exception {
    this.mockMvc
        .perform(get("/nowhere"))
        .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
        .andExpect(status().isOk())
        //.andExpect(redirectedUrl("/generalError"))
    ;
  }
  
  @Test
  @DisplayName("error page not found, not logged in")
  public void errorNotFoundNoCredentials() throws Exception {
    this.mockMvc
        .perform(get("/nowhere"))
        .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
        .andExpect(status().isFound())
        .andExpect(redirectedUrlPattern("**/signin"))
    ;
  }

}
