package com.osunkwo.taskmaster;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.UUID;


@DynamoDBTable(tableName = "Task")
public class TaskMaster {
    private UUID id;
    private String title;
    private String description;
    private String status;
    private String assignee;
    private String pic;
    private String thumbnailPic;

    public TaskMaster(){

    }

    public TaskMaster(String pic){
        this.pic = pic;
    }

    public TaskMaster(String title, String description, String assignee){
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.status = "Assigned";
    }

    public TaskMaster(String title, String description){
        this.title = title;
        this.description = description;
        this.status = "Available";
    }

    @DynamoDBAttribute
    public String getThumbnailPic() {
        return thumbnailPic;
    }

    public void setThumbnailPic(String thumbnailPic) {
        this.thumbnailPic = thumbnailPic;
    }


    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    public UUID getId() {
        return id;
    }


    public void setId(UUID id) {
        this.id = id;
    }

    @DynamoDBAttribute
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute
    public String getStatus() {
        return status;
    }
    @DynamoDBAttribute
    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

   public void setStatus(String status) {
        this.status = status;
    }

    @DynamoDBAttribute
    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
