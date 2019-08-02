package ${package}.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  public TokenBasedRememberMeServices rememberMeServices() {
    return new TokenBasedRememberMeServices("remember-me-key", userDetailsService);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
      .eraseCredentials(true)
      .userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    //@formatter:off
    web.ignoring()
      .antMatchers(HttpMethod.OPTIONS, "/**")
      .antMatchers("/res/**")
      .antMatchers("/css/**")
      .antMatchers("/js/**")
      .antMatchers("/images/**")
      .antMatchers("/webjars/**")
    ;
    //@formatter:on
  }
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
          .antMatchers("/", "/favicon.ico", "/res/**", "/signup", "/about").permitAll()
          .anyRequest().authenticated()
          .and()
      .formLogin()
          .loginPage("/signin")
          .permitAll()
          .failureUrl("/signin?error=1")
          .loginProcessingUrl("/authenticate")
          .and()
      .logout()
          .logoutUrl("/logout")
          .permitAll()
          .logoutSuccessUrl("/signin?logout")
          .and()
      .rememberMe()
          .rememberMeServices(rememberMeServices())
          .key("remember-me-key");
  }

  @Bean(name = "authenticationManager")
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
