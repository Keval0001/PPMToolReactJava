package io.intelligence.ppmtool.services;

import io.intelligence.ppmtool.domain.Backlog;
import io.intelligence.ppmtool.domain.ProjectTask;
import io.intelligence.ppmtool.repositories.BacklogRepository;
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

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        //Exceptions: Project not found

    	System.out.println(projectIdentifier);
    	System.out.println(projectTask);
        //PTs to be added to a specific project, project != null, BL exists
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        //set the bl to pt
        System.out.println(backlog);
        projectTask.setBacklog(backlog);
        //we want our project sequence to be like this: IDPRO-1  IDPRO-2  ...100 101
        Integer BacklogSequence = backlog.getPTSequence();
        // Update the BL SEQUENCE
        BacklogSequence++;
        
        //Update BacklogSequence
        backlog.setPTSequence(BacklogSequence);
        
        //Add Sequence to Project Task
        projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        //INITIAL priority when priority null
        if(projectTask.getPriority()==null){ // this needs to be done in future projectTask.getPriority()==0 to handle form
            projectTask.setPriority(3);
        }
        //INITIAL status when status is null
        if(projectTask.getStatus()==""|| projectTask.getStatus()==null){
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }

	public Iterable<ProjectTask> findBacklogById(String id) {
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
}