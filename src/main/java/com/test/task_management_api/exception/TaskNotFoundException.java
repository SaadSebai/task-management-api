package com.test.task_management_api.exception;

public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException(long id){
        super("Task with id " + id + " not found!");
    }
}
