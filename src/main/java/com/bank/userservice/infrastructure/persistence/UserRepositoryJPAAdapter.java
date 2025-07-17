package com.bank.userservice.infrastructure.persistence;

import com.bank.userservice.domain.model.User;
import com.bank.userservice.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryJPAAdapter implements UserRepository {

    private final UserRepositoryJPA userRepositoryJPA;

    public UserRepositoryJPAAdapter(UserRepositoryJPA userRepositoryJPA) {
        this.userRepositoryJPA = userRepositoryJPA;
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepositoryJPA.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepositoryJPA.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepositoryJPA.findFirstByEmail(email);
    }
}
