package ${package}.repository;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import ${package}.web.support.SecurityUtils;


/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    String userName = SecurityUtils.getCurrentUserLogin();
    return Optional.of((userName != null ? userName : SecurityUtils.SYSTEM_ACCOUNT));
  }
}
