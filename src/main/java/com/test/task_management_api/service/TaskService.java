package com.test.task_management_api.service;

import com.test.task_management_api.dto.TaskDto;
import com.test.task_management_api.exception.TaskNotFoundException;
import com.test.task_management_api.model.Task;
import com.test.task_management_api.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Page<Task> getAllTasks(Pageable pageable, String search) {

        if (search == null || search.trim().isEmpty()) {
            return taskRepository.findAll(pageable);
        }

        return taskRepository.findByTitleContainingIgnoreCase(
                search.trim(),
                pageable
        );
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
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
