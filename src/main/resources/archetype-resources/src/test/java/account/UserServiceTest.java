package ${package}.account;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
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

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	//@Test
	public void shouldInitializeWithTwoDemoUsers() {
		// act
		//userService.initialize();
		// assert
		verify(userRepositoryMock, times(2)).save(any(User.class));
	}

	@Test
	public void shouldThrowExceptionWhenUserNotFound() {
		// arrange
		thrown.expect(UsernameNotFoundException.class);
		thrown.expectMessage("User user@example.com was not found in the database");

		//when(userRepositoryMock.findOneByEmail("user@example.com")).thenReturn(null);
		// act
		userDetailsService.loadUserByUsername("user@example.com");
	}

	@Test
	public void shouldReturnUserDetails() {
		// arrange
	  when(authorityRepositoryMock.findOneByName("ROLE_USER")).thenReturn(Optional.of(new Authority("ROLE_USER")));
		User demoUser = new User("user", "user@example.com", "demo", Collections.singleton(authorityRepositoryMock.findOneByName("ROLE_USER").get()));
		demoUser.setActivated(true);
		//when(userRepositoryMock.findOneByEmail("user@example.com")).thenReturn(Optional.of(demoUser));

		when(userRepositoryMock.findOneByLogin("user")).thenReturn(Optional.of(demoUser));
		// act
		UserDetails userDetails = userDetailsService.loadUserByUsername("user");

		// assert
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
