package com.bank.userservice.application.usecase;

import com.bank.userservice.application.mapper.UserMapper;
import com.bank.userservice.domain.dto.request.CreateUserRequest;
import com.bank.userservice.domain.dto.response.CreateUserResponse;
import com.bank.userservice.domain.exception.UserExistedException;
import com.bank.userservice.domain.model.User;
import com.bank.userservice.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public CreateUserUseCase(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public CreateUserResponse execute(CreateUserRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new UserExistedException("User with email " + request.getEmail() + " already exists");
        }

        User newUser = userMapper.toUser(request);
        User savedUser = userRepository.save(newUser);

        return userMapper.toCreateUserResponse(savedUser);
    }
}
