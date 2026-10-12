package com.test.task_management_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @GetMapping
    public ArrayList<Integer> getTasks() {
        return new ArrayList<>();
    }
}