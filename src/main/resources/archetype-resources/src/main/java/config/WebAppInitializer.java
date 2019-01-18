package ${package}.config;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Profiles;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ContextCleanupListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  private static final boolean h2Present = ClassUtils.isPresent("org.h2.server.web.WebServlet", WebMvcConfigurationSupport.class.getClassLoader());

  private ConfigurableEnvironment environment;

  @Override
  protected String[] getServletMappings() {
    return new String[] { "/" };
  }

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class<?>[] { ApplicationConfig.class };
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return null;
  }

  @Override
  protected Filter[] getServletFilters() {
    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
    characterEncodingFilter.setEncoding("UTF-8");
    characterEncodingFilter.setForceEncoding(true);

    DelegatingFilterProxy securityFilterChain = new DelegatingFilterProxy("springSecurityFilterChain");

    return new Filter[] { characterEncodingFilter, securityFilterChain };
  }

  @Override
  protected void registerContextLoaderListener(ServletContext servletContext) {
    super.registerContextLoaderListener(servletContext);
    servletContext.addListener(new HttpSessionEventPublisher());
    servletContext.addListener(ContextCleanupListener.class);
    if (h2Present && environment.acceptsProfiles(Profiles.of("dev")))
      addServletH2(servletContext);
  }

  @Override
  protected void customizeRegistration(ServletRegistration.Dynamic registration) {
    registration.setInitParameter("defaultHtmlEscape", "true");
    registration.setInitParameter("spring.profiles.active", "default");
  }


  //@Profile beans are loaded via root context
  @Override
  protected WebApplicationContext createRootApplicationContext() {
    WebApplicationContext context = super.createRootApplicationContext();
    Assert.isInstanceOf(AnnotationConfigWebApplicationContext.class, context, "safety upgrade, RootApplicationContext should be AnnotationConfigWebApplicationContext");
    environment = ((ConfigurableEnvironment)context.getEnvironment());
    environment.setActiveProfiles(environment.getProperty("spring.profiles.active", "dev"));
    return context;
  }
  
  private void addServletH2(ServletContext container) {
    ServletRegistration.Dynamic messageDispatcher = container.addServlet("H2Console", "org.h2.server.web.WebServlet");
    messageDispatcher.setInitParameter("webAllowOthers", "true");
    messageDispatcher.setInitParameter("trace", "true");
    messageDispatcher.setLoadOnStartup(1);
    messageDispatcher.addMapping("/h2/*");
  }
}
