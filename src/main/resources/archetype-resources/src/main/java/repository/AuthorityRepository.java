package ${package}.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ${package}.entity.Authority;


/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
  
  Optional<Authority> findOneByName(String name);
}
