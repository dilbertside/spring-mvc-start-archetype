package ${package}.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ${package}.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByEmail(String email);

	@Query("select count(a) > 0 from User a where a.email = :email")
	boolean exists(@Param("email") String email);
	
	List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

  Optional<User> findOneByLogin(String login);

  Optional<User> findOneById(Long userId);

  @Query(value = "select distinct user from User user join fetch user.roles", countQuery = "select count(user) from User user")
  Page<User> findAllWithAuthorities(Pageable pageable);

  @Override
  void delete(User t);

  long countByEmail(String email);
  
  long countByLogin(String login);
}