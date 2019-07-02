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
    public List<TaskMaster> AddTask(@RequestParam String title, @RequestParam String description){
        TaskMaster task = new TaskMaster(title, description);
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

}
