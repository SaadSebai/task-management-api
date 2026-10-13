package com.test.task_management_api.service;

import com.test.task_management_api.dto.TaskDto;
import com.test.task_management_api.model.Task;
import com.test.task_management_api.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(long id) {
        return findTaskOrThrowNotFound(id);
    }

    public Task createTask(TaskDto taskDto) {
        return taskRepository.save(new Task(taskDto.title(), taskDto.description()));
    }

    public Task updateTask(long id, TaskDto taskDto) {
        Task task = findTaskOrThrowNotFound(id);

        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());

        return taskRepository.save(task);
    }

    public void deleteTask(long id) {
        Task task = findTaskOrThrowNotFound(id);

        taskRepository.delete(task);
    }

    private Task findTaskOrThrowNotFound(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found with id: " + id
                ));
    }
}
