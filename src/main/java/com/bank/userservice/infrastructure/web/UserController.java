package com.bank.userservice.infrastructure.web;

import com.bank.userservice.application.usecase.CreateUserUseCase;
import com.bank.userservice.application.usecase.GetUserByIdUseCase;
import com.bank.userservice.domain.dto.request.CreateUserRequest;

import com.bank.userservice.domain.dto.response.CreateUserResponse;
import com.bank.userservice.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final GetUserByIdUseCase getUserByIdUseCase;
    private final CreateUserUseCase createUserUseCase;

    public UserController(GetUserByIdUseCase getUserByIdUseCase, CreateUserUseCase createUserUseCase) {
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.createUserUseCase = createUserUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        log.info("Receive request GET /api/user/{}", id);
        User user = getUserByIdUseCase.execute(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
        log.info("Receive request POST /api/user/create");
        CreateUserResponse response = createUserUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

}
