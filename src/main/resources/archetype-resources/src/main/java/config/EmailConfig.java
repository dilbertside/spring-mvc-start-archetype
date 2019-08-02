package ${package}.config;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


@Configuration
@PropertySource("classpath:mail.properties")
public class EmailConfig {

  @Autowired
  private Environment environment;
	
  /**
   * from org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
   * @return {@link SpringTemplateEngine}
   */
  @Bean
  public SpringTemplateEngine emailTemplateEngine() {
    SpringTemplateEngine engine = new SpringTemplateEngine();
    engine.addTemplateResolver(emailTemplateResolver());
    return engine;
  }
	
	/**
   * Thymeleaf template resolver serving HTML 5 emails
   * @return {@link ClassLoaderTemplateResolver}
   */
  @Bean 
  @Description("Thymeleaf template resolver serving HTML 5 emails")
  public ClassLoaderTemplateResolver emailTemplateResolver() {
    ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
    emailTemplateResolver.setPrefix("email/");
    emailTemplateResolver.setSuffix(".html");
    emailTemplateResolver.setTemplateMode("HTML5");
    emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    emailTemplateResolver.setOrder(2);
    return emailTemplateResolver;
  }
  
	/**
	 * Mail sender configured for using Gmail
	 * @return {@link JavaMailSender}
	 */
	@Bean
	public JavaMailSender mailSender(){
		JavaMailSenderImpl bean = new JavaMailSenderImpl();
		bean.setHost(environment.getProperty("mail.username", String.class, "smtp.gmail.com"));
		bean.setUsername(environment.getProperty("mail.username"));
		bean.setPassword(environment.getProperty("mail.password"));
		bean.setDefaultEncoding(StandardCharsets.UTF_8.name());
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.auth", environment.getProperty("spring.mail.properties.mail.smtp.auth", Boolean.class, true));
		javaMailProperties.put("mail.smtp.starttls.enable", environment.getProperty("spring.mail.properties.mail.smtp.starttls.enable", Boolean.class, true));
		javaMailProperties.put("mail.debug", environment.acceptsProfiles(Profiles.of("dev")));
		bean.setJavaMailProperties(javaMailProperties);
		return bean;
	}
	
	@Bean
	public SimpleMailMessage mailMessage(){
		SimpleMailMessage bean = new SimpleMailMessage();
		bean.setFrom(environment.getProperty("mail.from", "noreply@doe.com"));
		bean.setTo(environment.getProperty("mail.recipient"));
		bean.setSubject(environment.getProperty("mail.subject"));
		return bean;
	}
}
