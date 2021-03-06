package io.intelligence.ppmtool.services;

import io.intelligence.ppmtool.domain.Backlog;
import io.intelligence.ppmtool.domain.Project;
import io.intelligence.ppmtool.domain.ProjectTask;
import io.intelligence.ppmtool.exceptions.ProjectIdException;
import io.intelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.intelligence.ppmtool.repositories.BacklogRepository;
import io.intelligence.ppmtool.repositories.ProjectRepository;
import io.intelligence.ppmtool.repositories.ProjectTaskRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

	@Autowired
	private ProjectTaskRepository projectTaskRepository;

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private ProjectRepository projectRepository;

	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

		// Exceptions: Project not found

		try { // PTs to be added to a specific project, project != null, BL exists
			Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
			// set the bl to pt
			System.out.println(backlog);
			projectTask.setBacklog(backlog);
			// we want our project sequence to be like this: IDPRO-1 IDPRO-2 ...100 101
			Integer BacklogSequence = backlog.getPTSequence();
			// Update the BL SEQUENCE
			BacklogSequence++;

			// Update BacklogSequence
			backlog.setPTSequence(BacklogSequence);

			// Add Sequence to Project Task
			projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);

			// INITIAL priority when priority null
			if (projectTask.getPriority()==0 || projectTask.getPriority() == null) { // this needs to be done in future projectTask.getPriority()==0 to
														// handle form // load form with select priority for user display
				projectTask.setPriority(3);
			}
			// INITIAL status when status is null
			if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO_DO");
			}

			return projectTaskRepository.save(projectTask);
		} catch (Exception e) {
			throw new ProjectNotFoundException("Project not Found");
		}

	}

	public Iterable<ProjectTask> findBacklogById(String id) {

		Project project = projectRepository.findByProjectIdentifier(id);
		if (project == null) {
			throw new ProjectNotFoundException("Project with ID: '" + id + "' does not exist");
		}
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}

	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {
		
		//make sure we are searching on the right backlog - USER usecase
		
		//make sure that we are searching on an existing backlog
		Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
		if(backlog == null) {
			throw new ProjectNotFoundException("Project with ID: "+backlog_id+" does not exist");
		}
		// make sure that our task exists
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		if(projectTask == null) {
			throw new ProjectNotFoundException("Project Task "+pt_id+" not found");
		}
		// make sure that the backlog/project id in path corresponds to the right project
		if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task "+pt_id+" does not exist in project: "+backlog_id);
		}
		
		
		return projectTask; // or projectTaskRepository.findByProjectSequence(pt_id);
	}
	
	public ProjectTask updateProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id) {
		
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
		projectTask = updatedTask;
		return projectTaskRepository.save(projectTask);
	}
	
	//update project task
	
	//find existing project task
	
	//replace it with updated task
	
	//save update
	
	public void deletePTByProjectSequence(String backlog_id, String pt_id) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
//		Backlog backlog = projectTask.getBacklog();
//		List<ProjectTask> pts = backlog.getProjectTasks();
//		pts.remove(projectTask);
//		backlogRepository.save(backlog);
		
		projectTaskRepository.delete(projectTask);
	}
	
}