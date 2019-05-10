package io.intelligence.ppmtool.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.intelligence.ppmtool.domain.Backlog;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {
	
	// findBy attribute (Can be any variable from Backlog POJO ex - id , project Identifier etc)
	Backlog findByProjectIdentifier(String Identifier);

}
