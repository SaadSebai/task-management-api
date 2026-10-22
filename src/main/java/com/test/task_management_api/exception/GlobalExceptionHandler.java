package com.test.task_management_api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TaskNotFoundException.class)
    public ProblemDetail taskNotFoundException(TaskNotFoundException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );

        problemDetail.setTitle("Task not found");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(UsernameDuplicationException.class)
    public ProblemDetail usernameDuplicationException(UsernameDuplicationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );

        problemDetail.setTitle("Duplicate username");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ProblemDetail wrongCredentialsException(WrongCredentialsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );

        problemDetail.setTitle("Wrong credentials");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (error1, error2) -> error1
                ));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "One or more fields have invalid values"
        );

        problemDetail.setTitle("Validation error");
        problemDetail.setProperty("errors", errors);
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail runtimeException(RuntimeException e) {

        logger.error("Unexpected error while processing request", e);

        ProblemDetail problemDetail = ProblemDetail.forStatus(
                HttpStatus.INTERNAL_SERVER_ERROR
        );

        problemDetail.setTitle("An unexpected error occurred");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}
