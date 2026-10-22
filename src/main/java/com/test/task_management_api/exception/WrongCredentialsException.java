package com.test.task_management_api.exception;

public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException() {
        super("Wrong credentials");
    }
}
