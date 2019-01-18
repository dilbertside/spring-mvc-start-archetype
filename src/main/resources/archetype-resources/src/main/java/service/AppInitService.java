#set( $dollar = '$' )
#set( $pound = '#' )
#set( $escape = '\' )
/**
 * AppInitService
 */
package ${package}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


/**
 * implementation of post start application actions, init, refresh, upgrade DB,...
 *
 */
@Service
public class AppInitService implements ApplicationListener<ContextRefreshedEvent>{

  private UserService userService;

  private Environment environment;
  
  @Value("${dollar}{app.security.user.username:user}")
  private String user;
  
  @Value("${dollar}{app.security.user.password:user}")
  private String pw;

  @Autowired
  public AppInitService(UserService userService, Environment environment) {
    this.userService = userService;
    this.environment = environment;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    initDefaultSecurityUsers();
  }

  protected void initDefaultSecurityUsers() {
    if (userService.count() == 0) {
      userService.createDefaultAuthorities("ROLE_ADMIN", "ROLE_API", "ROLE_USER", "ROLE_ACTUATOR");
      userService.createUser(environment.getProperty("app.security.admin.username", "admin"), environment.getProperty("app.security.admin.password", "admin"), 
          "admin" , "admin" , "admin@example.com", "en", 
          new String[] {"ROLE_ACTUATOR", "ROLE_ADMIN", "ROLE_API", "ROLE_USER"});
      userService.activateUser("admin");
      userService.createUser(user, pw, "default" , "user" , "user@example.com", "en", new String[] {"ROLE_API", "ROLE_USER"});
      userService.activateUser(user);
    }
    
  }
  
}
