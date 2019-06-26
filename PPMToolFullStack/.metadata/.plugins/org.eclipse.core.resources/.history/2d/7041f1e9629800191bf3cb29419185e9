package io.intelligence.ppmtool.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.intelligence.ppmtool.domain.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>{
	
//	@Override
//	Iterable<Project> findAllById(Iterable<Long> iterable);

	Project findByProjectIdentifier(String projectId);
	//Project findAllByid(Long id);
	//*JPA SPECIALITY* it will automatically search based on field when new add findAllBy______ <- it can be any attribute from Project class as mentioned above;
	
	@Override
	Iterable<Project> findAll();
}
