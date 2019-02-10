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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;


@Tag("controller")
public class HomeControllerTest extends WebSecurityConfigurationAware {
    
  @Autowired
  protected MockHttpSession session;
  
  /**
   * Test method for {@link ${package}.web.HomeController${pound}index(java.security.Principal, org.springframework.ui.Model)}.
   */
  @Test
  @DisplayName("show home page no credentials")
  public void displayHomePage() throws Exception {
    mockMvc.perform(get("/").with(csrf().asHeader()))
      .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("module"))
      .andExpect(view().name("home/homeNotSignedIn"))
      .andExpect(content().string(
          allOf(
              containsString("Welcome to the Spring"),
              containsString("<p>\n" + 
                  "            Get started quickly by signing up.\n" + 
                  "        </p>")
          ))
      );
  }
  
  /**
   * Test method for {@link ${package}.web.HomeController${pound}index()}.
   */
  @Test
  @DisplayName("show home page with credentials")
  @WithMockUser(password="user")
  public void displayHomePageWithLogin() throws Exception {
    mockMvc.perform(get("/").with(csrf().asHeader()))
      .andDo(log()) // to activate change logger ".result" level to DEBUG in logback-test.xml
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("module"))
      .andExpect(view().name("home/homeSignedIn"))
      .andExpect(content().string(
          allOf(
              containsString("<title>Home page</title>"),
              containsString("Welcome to the Spring")
          ))
      );
  }

}
