package com.test.task_management_api.controller;

import com.test.task_management_api.dto.TaskDto;
import com.test.task_management_api.model.Task;
import com.test.task_management_api.service.TaskService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    public Task createTask(@RequestBody TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable long id, @RequestBody TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<Void> deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }
}