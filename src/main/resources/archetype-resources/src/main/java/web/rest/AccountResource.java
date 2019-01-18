package ${package}.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import ${package}.entity.User;
import ${package}.repository.UserRepository;

import java.security.Principal;

@RestController
public class AccountResource {

  private final UserRepository userRepository;

  @Autowired
  public AccountResource(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("account/current")
  @ResponseStatus(value = HttpStatus.OK)
  @Secured({ "ROLE_USER", "ROLE_ADMIN" })
  public User currentAccount(Principal principal) {
    Assert.notNull(principal, "principal cannot be null at this point");
    return userRepository.findOneByEmail(principal.getName()).get();
  }

  @GetMapping("account/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  @Secured("ROLE_ADMIN")
  public User account(@PathVariable("id") Long id) {
    return userRepository.getOne(id);
  }
}
