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
public class AboutControllerTest extends WebSecurityConfigurationAware {
    
  /**
   * Test method for {@link ${package}.web.AboutController#about()}.
   */
  @Test
  @DisplayName("show about page")
  public void displayAboutPage() throws Exception {
    mockMvc.perform(get("/about").with(csrf().asHeader()))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("module"))
      .andExpect(view().name("home/about"))
      .andExpect(content().string(
          allOf(
              containsString("About"),
              containsString("Demo project")
          ))
      );
  }
}
