package com.test.task_management_api.exception;

public class UsernameDuplicationException extends RuntimeException {
    public UsernameDuplicationException(String username) {
        super("Username " + username + " is already taken");
    }
}
