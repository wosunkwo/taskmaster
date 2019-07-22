package com.osunkwo.taskmaster;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.HashMap;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
public class TaskMasterController {

    private S3Client s3Client;

    private AmazonSESSample sesClient;

    @Autowired
    TaskMasterRepository repository;

    @Autowired
    TaskMasterController(S3Client s3Client){
        this.s3Client = s3Client;
    }


    @GetMapping("/")
    public String GetHome(){
        return "home";
    }

    @CrossOrigin
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
            configureMessage("+15809196943");
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
            sesClient.sendEmail();

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

    @PostMapping("/tasks/{id}/images")
    public void addPic(@PathVariable UUID id, HttpServletResponse response, @RequestPart(value = "file")MultipartFile file) throws IOException {
        ArrayList<String> picList = this.s3Client.uploadFile(file);
        TaskMaster task = repository.findById(id).get();
        task.setPic(picList.get(0));
        task.setThumbnailPic(picList.get(1));
        repository.save(task);
        response.sendRedirect("http://taskmaster-app.s3-website-us-west-2.amazonaws.com");
    }

    @PutMapping("/tasks/{id}/assign/{assignee}")
    public List<TaskMaster> assignUserToTask(@PathVariable UUID id, @PathVariable String assignee){
        TaskMaster task = repository.findById(id).get();
        task.setAssignee(assignee);
        task.setStatus("Assigned");
        repository.save(task);
        configureMessage("+15809196943");
        List<TaskMaster> tasks = (List) repository.findAll();
        return tasks;
    }

    @GetMapping("/tasks/{id}")
    public TaskMaster GetSingleTask(@PathVariable UUID id){
        TaskMaster tasks =  repository.findById(id).get();
        return tasks;
    }

    //SNS function to help me send messages to a person once a task has been assigned
    public static void configureMessage(String phoneNumber) {
        AmazonSNSClient snsClient = new AmazonSNSClient();
        String message = "A task has been assigned to you";
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<>();
        //<set SMS attributes>
        sendSMSMessage(snsClient, message, phoneNumber, smsAttributes);
    }

    public static void sendSMSMessage(AmazonSNSClient snsClient, String message,
                                      String phoneNumber, Map<String, MessageAttributeValue> smsAttributes) {
        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));
        System.out.println(result); // Prints the message ID.
    }


}
