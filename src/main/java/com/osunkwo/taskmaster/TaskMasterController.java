package com.osunkwo.taskmaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class TaskMasterController {

    @Autowired
    TaskMasterRepository repository;


    @GetMapping("/")
    public String GetHome(){
        return "home";
    }


    @GetMapping("/tasks")
    public List<TaskMaster> GetTask(){
        List<TaskMaster> tasks = (List) repository.findAll();
        return tasks;
    }

    @PostMapping("/tasks")
    public List<TaskMaster> AddTask(@RequestParam String title, @RequestParam String description, @RequestParam(required = false, defaultValue = "") String assignee){
        TaskMaster task;
        if(assignee.equals(""))
        {
            task = new TaskMaster(title, description);
        }else
        {
            task = new TaskMaster(title, description, assignee);
        }
        repository.save(task);
        List<TaskMaster> tasks = (List) repository.findAll();
        return tasks;
    }

    @PutMapping("/tasks/{id}/state")
    public List<TaskMaster> putState(@PathVariable UUID id){
        TaskMaster task = repository.findById(id).get();

        if(task.getStatus().equals("Available")){
            task.setStatus("Assigned");
        }
        else if(task.getStatus().equals("Assigned")){
            task.setStatus("Accepted");
        }
        else if(task.getStatus().equals("Accepted")){
            task.setStatus("Finished");
        }
        repository.save(task);
        List<TaskMaster> tasks = (List) repository.findAll();
        return tasks;
    }

    @GetMapping("/users/{name}/tasks")
    public List<TaskMaster> getUserTask(@PathVariable String name){
        List<TaskMaster> allUserTask = repository.findByAssignee(name);
        return allUserTask;
    }

    @PutMapping("/tasks/{id}/assign/{assignee}")
    public List<TaskMaster> assignUserToTask(@PathVariable UUID id, @PathVariable String assignee){
        TaskMaster task = repository.findById(id).get();
        task.setAssignee(assignee);
        task.setStatus("Assigned");
        repository.save(task);
        List<TaskMaster> tasks = (List) repository.findAll();
        return tasks;
    }

}
