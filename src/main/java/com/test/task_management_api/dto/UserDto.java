package com.test.task_management_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(min = 3, max = 50) String password
) {
}
