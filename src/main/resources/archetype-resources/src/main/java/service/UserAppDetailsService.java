package ${package}.service;

import java.util.Locale;
import java.util.Optional;

import org.spring.webapp.service.UserService;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${package}.entity.User;
import ${package}.repository.UserRepository;

/**
 * Authenticate a user from the database.
 */
@Service("userDetailsService")
@lombok.extern.slf4j.Slf4j
@lombok.RequiredArgsConstructor
public class UserAppDetailsService implements UserDetailsService {

  final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(final String login) {
    log.debug("Authenticating {}", login);
    String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
    Optional<User> userFromDatabase = userRepository.findOneByLogin(lowercaseLogin);
    return userFromDatabase.map(user -> {
      if (!user.isActivated()) {
        throw new UsernameNotFoundException("User " + lowercaseLogin + " was not activated");
      }
      return new org.springframework.security.core.userdetails.User(lowercaseLogin, user.getPassword(), user.getAuthorities());
    }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " + "database"));
  }

}
