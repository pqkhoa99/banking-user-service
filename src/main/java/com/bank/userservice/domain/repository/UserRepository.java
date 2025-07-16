package com.bank.userservice.domain.repository;

import com.bank.userservice.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(String id);
    
    User save(User user);

    Optional<User> findByEmail(String email);
}
