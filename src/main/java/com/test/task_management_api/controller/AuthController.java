package com.test.task_management_api.controller;

import com.test.task_management_api.dto.UserDto;
import com.test.task_management_api.service.JwtService;
import com.test.task_management_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public String register(@RequestBody @Valid UserDto userDto) {
        userService.createUser(userDto);

        return "success";
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid UserDto userDto) {
        userService.login(userDto);

        return jwtService.generateToken(userDto.username());
    }
}
