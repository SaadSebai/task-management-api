package com.test.task_management_api.service;

import com.test.task_management_api.dto.UserDto;
import com.test.task_management_api.exception.UsernameDuplicationException;
import com.test.task_management_api.exception.WrongCredentialsException;
import com.test.task_management_api.model.User;
import com.test.task_management_api.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void createUser(UserDto userDto) {

        if (userRepository.findByUsername((userDto.username())).isPresent())
        {
            throw new UsernameDuplicationException(userDto.username());
        }

        User user = new User();
        user.setUsername(userDto.username());
        user.setPassword(passwordEncoder.encode(userDto.password()));

        userRepository.save(user);
    }

    public void login(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.username())
                .orElseThrow(WrongCredentialsException::new);

        if (!passwordEncoder.matches(userDto.password(), user.getPassword()))
        {
            throw new WrongCredentialsException();
        }
    }
}
