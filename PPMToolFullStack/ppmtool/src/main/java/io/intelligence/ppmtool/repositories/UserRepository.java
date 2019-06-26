package io.intelligence.ppmtool.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.intelligence.ppmtool.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	User findByUsername(String username);
	User getById(Long id);
	
	//Optional<User> findById(Long id); // same is getById but prevent null pointer exception
	
}
