# taskmaster
This is a Task Manager Repo

# Task Manager: S3 Programmatic Uploads

## Overview
Build up on taskmaster to give users ability to add images on the tasks that are already existing.

## Setup
Continue working in your taskmaster repo. Use as many DynamoDB features as you can, to make your queries as efficient as possible.

## Feature Tasks
- Users should be able to upload images that are associated with tasks.
- This ability should be at a route like POST /tasks/{id}/images. (This means it only needs to work for existing tasks, not as part of the initial creation of a task.)
- Your server should programmatically upload this image to S3.
- Your server should store the image URL (on S3) somewhere in its database, associated with the task.
- Fetching a single task (at GET /tasks/{id}) should also include the image URLs associated with that image.

## Description of the taskMaster application:
The app uses postman to create, update and get tasks. The app has ability to add imges assosiated with each tasks after the task is created.
Above mentioned feature tasks are implemented on the app.

## Link to the deployed application:
[Deployed Application](http://taskmaster-dev.us-east-2.elasticbeanstalk.com)


## Link to the deployed frontend:
[Deployed Front End](http://taskmaster-app.s3-website-us-west-2.amazonaws.com)

## Routes:
- GET request to /tasks and receive JSON data representing all of the tasks
- POST request to /tasks with body parameters for title and description to add a new task.
- PUT request to /tasks/{id}/state to advance the status of that task
- GET request to /users/{name}/tasks and receive JSON data representing all of the tasks assigned to that user.
- POST request to /tasks with body parameters for title, description, and assignee to add a new task.
- PUT request to /tasks/{id}/assign/{assignee} to assign a particular user to a task.
- POST request to /tasks/{id}/images to save an image to a particular task with that id.
- GET request to /tasks/{id} to display one particular task with the image assosiated with it.


## Issues faced:
- Having to create a personal AWS account to set up deployment fo rs3.

Resources:
- https://github.com/codefellows/seattle-java-401d4/tree/master/class-26
- https://github.com/codefellows/seattle-java-401d4/tree/master/class-29/demo



