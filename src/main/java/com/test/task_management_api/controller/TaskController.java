package com.test.task_management_api.controller;

import com.test.task_management_api.dto.TaskDto;
import com.test.task_management_api.model.Task;
import com.test.task_management_api.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ArrayList<Integer> getTasks() {
        return new ArrayList<>();
    }

    @PostMapping
    public Task createTask(@RequestBody TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }
}