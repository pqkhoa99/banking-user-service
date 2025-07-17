package com.bank.userservice.repository;

import com.bank.userservice.domain.model.User;
import com.bank.userservice.infrastructure.persistence.UserRepositoryJPA;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
//@Testcontainers
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryJPATest {

//    @Container
//    @ServiceConnection
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
//            .withDatabaseName("testdb")
//            .withUsername("test")
//            .withPassword("test");

    @Autowired
    private UserRepositoryJPA userRepository;

    @Test
    void shouldSaveAndFindUserById() {
        // Given
        User user = User.builder()
                .email("john.doe@example.com")
                .passwordHash("hashed-password")
                .firstName("John")
                .lastName("Doe")
                .kycStatus(User.KYCStatus.VERIFIED)
                .createdAt(ZonedDateTime.now())
                .build();

        // When
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());


        // Then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(foundUser.get().getFirstName()).isEqualTo("John");
        assertThat(foundUser.get().getLastName()).isEqualTo("Doe");
        assertThat(foundUser.get().getKycStatus()).isEqualTo(User.KYCStatus.VERIFIED);
    }

    @Test
    void shouldFindUserByEmail() {
        // Given
        User user = User.builder()
                .email("jane.smith@example.com")
                .passwordHash("hashed-password")
                .firstName("Jane")
                .lastName("Smith")
                .kycStatus(User.KYCStatus.PENDING)
                .createdAt(ZonedDateTime.now())
                .build();

        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findFirstByEmail("jane.smith@example.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("jane.smith@example.com");
        assertThat(foundUser.get().getFirstName()).isEqualTo("Jane");
        assertThat(foundUser.get().getKycStatus()).isEqualTo(User.KYCStatus.PENDING);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        // When
        Optional<User> foundUser = userRepository.findFirstByEmail("nonexistent@example.com");

        // Then
        assertThat(foundUser).isEmpty();
    }

}

