package com.bank.userservice.usecase;

import com.bank.userservice.application.usecase.GetUserByIdUseCase;
import com.bank.userservice.domain.exception.UserNotFoundException;
import com.bank.userservice.domain.model.User;
import com.bank.userservice.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetUserByIdUseCaseTest {

    @InjectMocks
    private GetUserByIdUseCase getUserByIdUseCase;

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id("user-123")
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .passwordHash("hashed-password")
                .kycStatus(User.KYCStatus.VERIFIED)
                .createdAt(ZonedDateTime.now())
                .build();
    }

    @Test
    void shouldReturnUserWhenUserExists() {
        // Given
        String userId = "user-123";
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // When
        User result = getUserByIdUseCase.execute(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("user-123");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getKycStatus()).isEqualTo(User.KYCStatus.VERIFIED);
        
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        // Given
        String userId = "non-existing-user";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> getUserByIdUseCase.execute(userId)
        );

        assertThat(exception.getMessage()).isEqualTo("User not found with id: " + userId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldNotCallRepositoryMultipleTimesForSameRequest() {
        // Given
        String userId = "user-123";
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // When
        getUserByIdUseCase.execute(userId);

        // Then
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }
}
