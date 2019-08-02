package ${package}.service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${package}.dto.SignupForm;
import ${package}.dto.UserDto;
import ${package}.entity.Authority;
import ${package}.entity.User;
import ${package}.repository.AuthorityRepository;
import ${package}.repository.UserRepository;
import ${package}.web.support.SecurityUtils;

/**
 * Service class for managing users.
 */
@Service
@Transactional
@lombok.extern.slf4j.Slf4j
@lombok.RequiredArgsConstructor
public class UserService {


  final PasswordEncoder passwordEncoder;
  final UserRepository userRepository;
  final AuthorityRepository authorityRepository;

  
  @Transactional(readOnly = true)
  public long count() {
    return userRepository.count();
  }

  /**
   * 
   * @param login to set
   * @param password to set
   * @param firstName to set
   * @param lastName to set
   * @param email to set
   * @param langKey to set
   * @return {@link User} created
   */
  public User createUser(String login, String password, String firstName, String lastName, String email, String langKey) {
    User newUser = new User();
    Authority authority = authorityRepository.findOneByName(SecurityUtils.USER).get();
    Set<Authority> authorities = new HashSet<>();
    String encryptedPassword = passwordEncoder.encode(password);
    newUser.setLogin(login);
    // new user gets initially a generated password
    newUser.setPassword(encryptedPassword);
    newUser.setFirstName(firstName);
    newUser.setLastName(lastName);
    newUser.setEmail(email);
    newUser.setLangKey(langKey);
    // new user is not active
    newUser.setActivated(false);
    // new user gets registration key
    authorities.add(authority);
    newUser.setRoles(authorities);
    userRepository.save(newUser);
    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  /**
   * 
   * @param login to set
   * @param password to set
   * @param firstName to set
   * @param lastName to set
   * @param email to set
   * @param langKey to set
   * @param authorities array
   * @return {@link User} created
   */
  public User createUser(String login, String password, String firstName, String lastName, String email, String langKey, String[] authorities) {
    User user = createUser(login, password, firstName, lastName, email, langKey);
    authorities = ArrayUtils.removeAllOccurences(authorities, SecurityUtils.USER);// already set in previous step
    for (String authority : authorities) {
      user.getRoles().add(authorityRepository.findOneByName(authority).get());
    }
    user = userRepository.save(user);
    return user;
  }
  
  /**
   * 
   * @param dto {@link UserDto}
   * @return {@link User}
   */
  public User createUser(UserDto dto) {
    User user = new User();
    user.setLogin(dto.getLogin());
    user.setFirstName(dto.getFirstName());
    user.setLastName(dto.getLastName());
    user.setEmail(dto.getEmail());
    if (dto.getLangKey() == null) {
      user.setLangKey("en"); // default language
    } else {
      user.setLangKey(dto.getLangKey());
    }
    if (dto.getAuthorities() != null) {
      Set<Authority> authorities = new HashSet<>();
      dto.getAuthorities().stream().forEach(authority -> authorities.add(authorityRepository.findOneByName(StringUtils.prependIfMissing(authority, "ROLE_")).get()));
      authorities.add(authorityRepository.findOneByName("ROLE_USER").get());
      user.setRoles(authorities);
    }
    String encryptedPassword = passwordEncoder.encode(dto.getPassword());
    user.setPassword(encryptedPassword);
    user.setActivated(true);
    userRepository.save(user);
    log.debug("Created Information for User: {}", user);
    return user;
  }
  
  /**
   * 
   * @param request {@link UserDto}
   * @return {@link ErrorCode} success if ok
   */
  public boolean updateUser(UserDto request) {
    boolean s = true; 
    Optional<User> user = userRepository.findOneByLogin(request.getLogin());
    if (user.isPresent()) {
      User u = user.get();
      if (StringUtils.isNotBlank(request.getFirstName())) {
        u.setFirstName(request.getFirstName());
      }
      if (StringUtils.isNotBlank(request.getLastName())) {
        u.setLastName(request.getLastName());
      }
      if (StringUtils.isNotBlank(request.getEmail())) {
        u.setEmail(request.getEmail());       
      }
      if (StringUtils.isNotBlank(request.getLangKey())) {
        u.setLangKey(request.getLangKey());
      }
      if (StringUtils.isNotBlank(request.getPassword())) {
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        u.setPassword(encryptedPassword);
      }
      if (!request.getAuthorities().isEmpty()) {
        for (String authority : request.getAuthorities()) {
          authority = StringUtils.prependIfMissing(authority, "ROLE_");
          Optional<Authority> auth = authorityRepository.findOneByName(authority);
          if (auth.isPresent()) {
            user.filter(item -> !item.hasAuthority(auth.get()))
              .ifPresent(item -> item.getRoles().add(auth.get()));
          }
        }
      }
      u = userRepository.save(u);
      log.debug("Changed Information for User: {}", u);
    } else {
      s = false; 
    }
    return s;
  }

  /**
   * 
   * @param firstName to set
   * @param lastName to set 
   * @param email to set
   * @param langKey to set
   */
  public void updateUser(String firstName, String lastName, String email, String langKey) {
    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent( u -> {
      u.setFirstName(firstName);
      u.setLastName(lastName);
      u.setEmail(email);       
      u.setLangKey(langKey);
      userRepository.save(u);
      log.debug("Changed Information for User: {}", u);
    });
  }

  /**
   * 
   * @param id to use
   * @param login to set
   * @param firstName to set
   * @param lastName to set
   * @param email to set
   * @param langKey to set
   * @param authorities to set
   */
  public void updateUser(Long id, String login, String firstName, String lastName, String email, boolean activated, String langKey, Set<String> authorities) {
    userRepository.findOneById(id).ifPresent(u -> {
      u.setLogin(login);
      u.setFirstName(firstName);
      u.setLastName(lastName);
      u.setEmail(email);
      u.setActivated(activated);
      u.setLangKey(langKey);
      Set<Authority> managedAuthorities = u.getRoles();
      managedAuthorities.clear();
      authorities.stream().forEach(authority -> managedAuthorities.add(authorityRepository.findOneByName(authority).get()));
      log.debug("Changed Information for User: {}", u);
    });
  }

  /**
   * 
   * @param login to delete
   */
  public void deleteUser(String login) {
    userRepository.findOneByLogin(login).ifPresent(u -> {
      userRepository.delete(u);
      log.debug("Deleted User: {}", u);
    });
  }

  /**
   * 
   * @param password to set
   */
  public void changePassword(String password) {
    userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
      String encryptedPassword = passwordEncoder.encode(password);
      u.setPassword(encryptedPassword);
      userRepository.save(u);
      log.debug("Changed password for User: {}", u);
    });
  }

  /**
   * 
   * @param login to set
   * @return {@link User}
   */
  @Transactional(readOnly = true)
  public Optional<User> getUserWithAuthoritiesByLogin(String login) {
    return userRepository.findOneByLogin(login).map(u -> {
      u.getAuthorities().size();
      return u;
    });
  }

  /**
   * 
   * @param id to set
   * @return {@link User}
   */
  @Transactional(readOnly = true)
  public User getUserWithAuthorities(Long id) {
    User user = userRepository.findOneById(id).get();
    user.getAuthorities().size(); // eagerly load the association
    return user;
  }

  /**
   * 
   * @return {@link User}
   */
  @Transactional(readOnly = true)
  public User getUserWithAuthorities() {
    User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
    user.getAuthorities().size(); // eagerly load the association
    return user;
  }

  /**
   * Not activated users should be automatically deleted after 3 days.
   * <p>
   * This is scheduled to get fired everyday, at 01:00 (am).
   * </p>
   */
  @Scheduled(cron = "0 0 1 * * ?")
  public void removeNotActivatedUsers() {
    Instant now = Instant.now();
    List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
    for (User user : users) {
      log.debug("Deleting not activated user {}", user.getLogin());
      userRepository.delete(user);
    }
  }

  /**
   * 
   * @param authorities to set
   */
  public void createDefaultAuthorities(String... authorities) {
    for (String authority : authorities) {
      authorityRepository.save(new Authority(authority));
    }
  }

  /**
   * 
   * @param login user
   */
  public void activateUser(String login) {
    userRepository.findOneByLogin(login).ifPresent(u -> {
      u.setActivated(true);
      userRepository.save(u);
    });
    
  }

  public long countByEmail(String email) {
    return userRepository.countByEmail(email);
  }
  
  public long countByLogin(String login) {
    return userRepository.countByLogin(login);
  }
  
  public void signin(User account) {
    SecurityContextHolder.getContext().setAuthentication(authenticate(account));
  }
  
  private Authentication authenticate(User user) {
    return new UsernamePasswordAuthenticationToken(user.getLogin(), null, user.getAuthorities());   
  }

  public User createUser(@Valid SignupForm sf) {
    User u = new User(StringUtils.substringBefore(sf.getEmail(), "@"), sf.getEmail(), sf.getPassword(), Collections.singleton(authorityRepository.findOneByName("ROLE_USER").get()));
    u.setActivated(true);
    return u;
  }
}
