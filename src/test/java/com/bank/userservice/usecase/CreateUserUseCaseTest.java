package com.bank.userservice.usecase;

import com.bank.userservice.application.mapper.UserMapper;
import com.bank.userservice.application.usecase.CreateUserUseCase;
import com.bank.userservice.domain.dto.request.CreateUserRequest;
import com.bank.userservice.domain.dto.response.CreateUserResponse;
import com.bank.userservice.domain.exception.UserExistedException;
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
public class CreateUserUseCaseTest {

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private User testUser;
    private CreateUserRequest request;
    private CreateUserResponse expectedResponse;

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

        request = CreateUserRequest.builder()
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("plain-password")
                .kycStatus(User.KYCStatus.VERIFIED)
                .build();

        expectedResponse = CreateUserResponse.builder()
                .id("user-123")
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .createdAt(ZonedDateTime.now())
                .build();
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Given
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toUser(request)).thenReturn(testUser);
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userMapper.toCreateUserResponse(testUser)).thenReturn(expectedResponse);

        // When
        CreateUserResponse result = createUserUseCase.execute(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("user-123");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(userMapper, times(1)).toUser(request);
        verify(userRepository, times(1)).save(testUser);
        verify(userMapper, times(1)).toCreateUserResponse(testUser);
    }

    @Test
    void shouldThrowUserExistsExceptionWhenUserAlreadyExists() {
        // Given
        String email = "john.doe@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // When & Then
        UserExistedException exception = assertThrows(
                UserExistedException.class,
                () -> createUserUseCase.execute(request)
        );

        assertThat(exception.getMessage()).isEqualTo("User with email " + email + " already exists");
        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, never()).toUser(any());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toCreateUserResponse(any());
    }

    @Test
    void shouldNotSaveUserWhenEmailAlreadyExists() {
        // Given
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(testUser));

        // When & Then
        assertThrows(UserExistedException.class, () -> createUserUseCase.execute(request));

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }
}
