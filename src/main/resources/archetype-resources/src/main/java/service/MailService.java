package ${package}.service;

import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Mail Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 * </p>
 */
@Service
@lombok.extern.slf4j.Slf4j
@lombok.RequiredArgsConstructor
public class MailService {

  private final JavaMailSender javaMailSender;

  private final SpringTemplateEngine emailTemplateEngine;
  
  private final MessageSource messageSource;

  private char emailSeparator = ',';
  
  @Value("${dollar}{mail.from:noreply@doe.com}")
  private String from;
  
  @Value("${dollar}{mail.cc: }")
  private String cc;
  
  /**
   * General send email function
   * 
   * @param to target email address
   * @param subject email subject
   * @param content email content
   * @param isMultipart whether it's multiple part or not
   * @param isHtml whether content is html format or text plain
   */
  @Async
  public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
    log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart, isHtml, to, subject, content);

    // Prepare message using a Spring helper
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
      message.setTo(StringUtils.split(to, emailSeparator));
      message.setFrom(from);
      if (StringUtils.isNotBlank(cc)) {
        message.setCc(StringUtils.split(cc, emailSeparator));
      }
      message.setSubject(subject);
      message.setText(content, isHtml);

      javaMailSender.send(mimeMessage);
      log.debug("Sent email to '{}'", to);
    } catch (Exception e) {
      log.error("Email could not be sent", e);
    }
  }

  /**
   * send raw email
   * @param to target email address
   * @param cc additional email targets
   * @param bcc additional hidden email targets
   * @param subject email subject
   * @param content email content
   * @throws MessagingException MessagingException
   */
  @Async
  public void sendEmail(String to, String cc, String bcc, String subject, String content) throws MessagingException {
    log.debug("Send email to '{}' with subject '{}' and content={}", to, subject, content);

    // Prepare message using a Spring helper
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
      message.setTo(StringUtils.split(to, emailSeparator));
      message.setFrom(from);

      if (StringUtils.isNotBlank(cc)) {
        message.setCc(StringUtils.split(cc, emailSeparator));
      }
      if (StringUtils.isNotBlank(bcc)) {
        message.setBcc(StringUtils.split(bcc, emailSeparator));
      }
      message.setSubject(subject);
      message.setText(content, true);


      javaMailSender.send(mimeMessage);
      log.debug("Sent email to User '{}'", to);
    } catch (Exception e) {
      log.warn("Email could not be sent to user '{}'", to, e);
    }
  }

  /**
   * 
   * @param langKey locale
   * @param templateName template in dir mails/
   * @param titleKey subject
   * @param to recipients
   * @param additionalVariables pair if any
   */
  @Async
  public void sendEmailFromTemplate(String langKey, String templateName, String titleKey, String to, String[] additionalVariables) {
    Locale locale = Locale.forLanguageTag(langKey);
    Context context = new Context(locale);
    if (null != additionalVariables) {
      for (int i = 0 ; i < additionalVariables.length ; i += 2) {
        context.setVariable(additionalVariables[i], ObjectUtils.defaultIfNull(additionalVariables[i + 1], ""));
      }
    }
    String content = emailTemplateEngine.process(templateName, context);
    String subject = messageSource.getMessage(titleKey, null, titleKey, locale);
    sendEmail(to, subject, content, false, true);
  }
  
  /**
   * @return the emailSeparator
   */
  public char getEmailSeparator() {
    return emailSeparator;
  }


  /**
   * @param emailSeparator the emailSeparator to set
   */
  public void setEmailSeparator(char emailSeparator) {
    this.emailSeparator = emailSeparator;
  }

}
