package com.test.task_management_api.dto;

public class TaskDto {
    private final String title;
    private final String description;

    public TaskDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
