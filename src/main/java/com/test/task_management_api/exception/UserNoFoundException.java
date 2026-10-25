package com.test.task_management_api.exception;

public class UserNoFoundException extends RuntimeException {
    public UserNoFoundException() {
        super("User not found");
    }
}
