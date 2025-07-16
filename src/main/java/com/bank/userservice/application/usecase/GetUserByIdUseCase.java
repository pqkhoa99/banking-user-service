package com.bank.userservice.application.usecase;

import com.bank.userservice.domain.exception.UserNotFoundException;
import com.bank.userservice.domain.model.User;
import com.bank.userservice.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class GetUserByIdUseCase {
    private final UserRepository userRepository;

    public GetUserByIdUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }
}
