package com.test.task_management_api.service;

import com.test.task_management_api.dto.TaskDto;
import com.test.task_management_api.model.Task;
import com.test.task_management_api.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void shouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("Task 2");

        Page<Task> taskPage = new PageImpl<>(List.of(task1, task2));

        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepository.findAll(any(Pageable.class)))
                .thenReturn(taskPage);

        Page<Task> tasksList = taskService.getAllTasks(pageable, null);

        assertEquals(taskPage.getTotalElements(), tasksList.getTotalElements());
    }

    @Test
    public void shouldReturnTaskById() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Task 1");

        when(taskRepository.findById(nullable(Long.class))).thenReturn(Optional.of(task));

        Task taskById = taskService.getTaskById(task.getId());

        assertEquals(task, taskById);
    }

    @Test
    public void shouldCreateTask() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Task 1");
        task.setDescription("Task Description");

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDto taskDto = new TaskDto(task.getTitle(), task.getDescription());

        Task createdTask = taskService.createTask(taskDto);

        assertEquals(task, createdTask);
    }

    @Test
    public void shouldUpdateTask() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Task 1");
        task.setDescription("Task Description");

        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TaskDto taskDto = new TaskDto("New title", "New description");
        Task updatedTask = taskService.updateTask(task.getId(), taskDto);

        assertEquals(updatedTask.getId(), task.getId());
        assertEquals("New title", updatedTask.getTitle());
        assertEquals("New description", updatedTask.getDescription());
    }

    @Test
    public void shouldDeleteTask() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Task 1");
        task.setDescription("Task Description");

        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).delete(any(Task.class));
    }
}
