package com.test.task_management_api.service;

import com.test.task_management_api.dto.TaskDto;
import com.test.task_management_api.exception.TaskNotFoundException;
import com.test.task_management_api.exception.UserNoFoundException;
import com.test.task_management_api.model.Task;
import com.test.task_management_api.model.User;
import com.test.task_management_api.repository.TaskRepository;
import com.test.task_management_api.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Page<Task> getAllTasks(Pageable pageable, String search) {
        if (search == null || search.trim().isEmpty()) {
            return taskRepository.findByUser_Username(pageable, findUsername());
        }

        return taskRepository.findAllByUser_UsernameAndTitleContainsIgnoreCase(
                findUsername(),
                search.trim(),
                pageable
        );
    }

    public Task getTaskById(long id) {
        return findTaskOrThrowNotFound(id);
    }

    public Task createTask(TaskDto taskDto) {
        User user = userRepository.findByUsername(findUsername())
                .orElseThrow(UserNoFoundException::new);

        return taskRepository.save(new Task(taskDto.title(), taskDto.description(), user));
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

    /**
     * Find a Task by id, also the task should belong to the auth user.
     *
     * @param id long
     * @return User
     */
    private Task findTaskOrThrowNotFound(long id) {
        return taskRepository.findByIdAndUser_Username(id, findUsername())
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * Get auth Username.
     *
     * @return User
     */
    private String findUsername() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
    }
}
