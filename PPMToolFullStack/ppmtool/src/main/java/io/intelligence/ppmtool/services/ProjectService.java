package io.intelligence.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.intelligence.ppmtool.domain.Backlog;
import io.intelligence.ppmtool.domain.Project;
import io.intelligence.ppmtool.domain.User;
import io.intelligence.ppmtool.exceptions.ProjectIdException;
import io.intelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.intelligence.ppmtool.repositories.BacklogRepository;
import io.intelligence.ppmtool.repositories.ProjectRepository;
import io.intelligence.ppmtool.repositories.UserRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private UserRepository userRepository;

	public Project saveOrUpdate(Project project, String username) {

		try {

			User user = userRepository.findByUsername(username);

			project.setUser(user);
			project.setProjectLeader(user.getUsername());

			String projectIdentifier = project.getProjectIdentifier().toUpperCase();
			project.setProjectIdentifier(projectIdentifier);

			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(projectIdentifier);
			}

			if (project.getId() != null) {
				project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
			}

			return projectRepository.save(project);

		} catch (Exception e) {
			throw new ProjectIdException(
					"Project Id" + project.getProjectIdentifier().toUpperCase() + " already exists.");
		}
	}

	public Project findProjectByIdentifier(String projectId, String username) {

		// Only want to return the project if the user looking for it is the owner

		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

		// Check , if project exists
		if (project == null) {
			throw new ProjectIdException("Project Id " + projectId + " does not exists.");
		}

		// Check if user owns the project

		// ProjectLeader and principal are same/ personal tool
		if (!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("Project not found in your account");
		}

		return project;
	}

	public Iterable<Project> findAll(String username) {
		return projectRepository.findAllByProjectLeader(username);
	}

	public void deleteProjectByIdentifier(String projectId, String username) {
		projectRepository.delete(findProjectByIdentifier(projectId, username));
	}

}
