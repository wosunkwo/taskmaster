# Overview
This readme contains a brief description of how I wrote a lambda warmer for a image resizer lambda function I created.

# Feature Tasks
In this task I made my Lambda function that creates a thumbnail versions of images to never be cold. By creating a 
CloudWatch timer to help keep it alive.

## Initial CloudWatch warmer setup
![alt_text](https://github.com/wosunkwo/taskmaster/blob/master/assest/Screen%20Shot%202019-07-12%20at%209.40.45%20AM.png)

## Final CloudWatch warmer setup
![alt_text](https://github.com/wosunkwo/taskmaster/blob/master/assest/Screen%20Shot%202019-07-12%20at%209.40.56%20AM.png)

## Testing my pings
![alt_text](https://github.com/wosunkwo/taskmaster/blob/master/assest/Screen%20Shot%202019-07-12%20at%209.59.21%20AM.png)

## Graphs showing my ping requests to my Lambda function
![alt_text](https://github.com/wosunkwo/taskmaster/blob/master/assest/Screen%20Shot%202019-07-12%20at%2010.53.13%20AM.png)
