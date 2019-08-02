package ${package}.web.support;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ${package}.repository.UserRepository;

@Component
public class EmailExistsValidator implements ConstraintValidator<${package}.web.support.EmailExists, String> {

  private final UserRepository userRepository;

  @Autowired
  public EmailExistsValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void initialize(${package}.web.support.EmailExists constraintAnnotation) {

  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return !userRepository.exists(value);
  }
}