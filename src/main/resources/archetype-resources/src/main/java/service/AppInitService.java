#set( $dollar = '$' )
#set( $pound = '#' )
#set( $escape = '\' )
/**
 * AppInitService
 */
package ${package}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


/**
 * implementation of post start application actions, init, refresh, upgrade DB,...
 *
 */
@Service
public class AppInitService {

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

  /**
   * called twice: once by root context and 2nd time by dispatcher
   * @param event {@link ContextRefreshedEvent} to filter
   */
  @EventListener({ContextRefreshedEvent.class})
  public void onContextRefreshedEvent(ContextRefreshedEvent event) {
    if (null != event.getApplicationContext().getParent()) {// 2nd pass only
      initDefaultSecurityUsers();
    }
  }
  
  /**
   * It would be nice to have this event instead of having {@link ContextStartedEvent} called twice
   * <br> 
   * {@link AbstractApplicationContext#start()} publish this event but as comment gstackoverflow, how to call it?, no obvious hook provided 
   * https://stackoverflow.com/questions/5728376/spring-applicationlistener-is-not-receiving-events#comment73852040_5728681 
   * @param event {@link ContextStartedEvent} to filter
   */
  @EventListener({ContextStartedEvent.class})
  public void onContextStartedEvent(ContextStartedEvent event) {
    initDefaultSecurityUsers();
  }

  protected void initDefaultSecurityUsers() {
    // if database is empty, we fill it with required data
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
