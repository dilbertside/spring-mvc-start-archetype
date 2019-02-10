package ${package}.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import ${package}.entity.Authority;
import ${package}.entity.User;
import ${package}.repository.AuthorityRepository;
import ${package}.repository.UserRepository;
import ${package}.service.UserAppDetailsService;
import ${package}.service.UserService;

import static java.util.function.Predicate.isEqual;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

@Tag("service")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserService userService;// = new userService();
	
	@InjectMocks
	private UserAppDetailsService userDetailsService;

	@Mock
	private UserRepository userRepositoryMock;
	
	@Mock
	private AuthorityRepository authorityRepositoryMock;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("raise exception when user not found")
	public void shouldThrowExceptionWhenUserNotFound() {
		
	  Throwable exception = assertThrows(UsernameNotFoundException.class,
        ()->{
          userDetailsService.loadUserByUsername("user@example.com");
        });
		assertEquals("User user@example.com was not found in the database", exception.getMessage());
	}

	@Test
	@DisplayName("user service  load by user name")
	public void shouldReturnUserDetails() {
		
	  when(authorityRepositoryMock.findOneByName("ROLE_USER")).thenReturn(Optional.of(new Authority("ROLE_USER")));
		User demoUser = new User("user", "user@example.com", "demo", Collections.singleton(authorityRepositoryMock.findOneByName("ROLE_USER").get()));
		demoUser.setActivated(true);
		//when(userRepositoryMock.findOneByEmail("user@example.com")).thenReturn(Optional.of(demoUser));

		when(userRepositoryMock.findOneByLogin("user")).thenReturn(Optional.of(demoUser));
		
		UserDetails userDetails = userDetailsService.loadUserByUsername("user");

		assertThat(demoUser.getLogin()).isEqualTo(userDetails.getUsername());
		assertThat(demoUser.getPassword()).isEqualTo(userDetails.getPassword());
		assertThat(hasAuthority(userDetails, demoUser.getRoles().iterator().next().getName())).isTrue();
	}

	private boolean hasAuthority(UserDetails userDetails, String role) {
		return userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(isEqual(role));
	}
}
