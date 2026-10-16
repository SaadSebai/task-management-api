package com.test.task_management_api.controller;

import com.test.task_management_api.dto.TaskDto;
import com.test.task_management_api.exception.TaskNotFoundException;
import com.test.task_management_api.model.Task;
import com.test.task_management_api.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@WebMvcTest(controllers = TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    public void shouldReturnEmptyListWhenNoTasksExist() throws Exception {
        when(taskService.getAllTasks(any(Pageable.class), nullable(String.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    public void shouldReturnListOfTasks() throws Exception {
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("Task 2");

        Page<Task> taskPage = new PageImpl<>(List.of(task1, task2));

        when(taskService.getAllTasks(any(Pageable.class), nullable(String.class)))
                .thenReturn(taskPage);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    public void shouldReturnTaskById() throws Exception {
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task 1");

        when(taskService.getTaskById(anyLong())).thenReturn(task1);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    public void shouldReturnNotFoundWhenCallingGetTaskById() throws Exception {
        when(taskService.getTaskById(anyLong())).thenThrow(new TaskNotFoundException(1));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateTaskSuccessfully() throws Exception {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Task 1");
        task.setDescription("Task Description");

        when(taskService.createTask(any(TaskDto.class))).thenReturn(task);

        mockMvc.perform(
                    post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title": "Task 1",
                                        "description": "Task Description"
                                    }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    public void shouldReturnValidationErrorInTaskCreate() throws Exception {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Task 1");
        task.setDescription("Task Description");

        when(taskService.createTask(any(TaskDto.class))).thenReturn(task);

        mockMvc.perform(
                post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "",
                            "description": "Task Description"
                        }
                        """)
        )
        .andExpect(status().isBadRequest());

    }

    @Test
    public void shouldUpdateTaskSuccessfully() throws Exception {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Updated Task 1");
        task.setDescription("Task Description");

        when(taskService.updateTask(eq(1L), any(TaskDto.class))).thenReturn(task);

        mockMvc.perform(
                put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "Updated Task 1",
                            "description": "Task Description"
                        }
                        """)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("Updated Task 1"));
    }

    @Test
    public void shouldDeleteTaskSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(eq(1L));
    }
}
