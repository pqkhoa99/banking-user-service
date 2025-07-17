package com.bank.userservice.infrastructure.persistence;

import com.bank.userservice.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryJPA extends JpaRepository<User, String> {

    Optional<User> findFirstByEmail(String email);

    // Additional JPA-specific query methods can be added here if needed
}
