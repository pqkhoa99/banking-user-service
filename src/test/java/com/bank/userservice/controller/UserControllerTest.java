package com.bank.userservice.controller;

import com.bank.userservice.application.usecase.CreateUserUseCase;
import com.bank.userservice.application.usecase.GetUserByIdUseCase;
import com.bank.userservice.domain.dto.request.CreateUserRequest;
import com.bank.userservice.domain.dto.response.CreateUserResponse;
import com.bank.userservice.domain.model.User;
import com.bank.userservice.infrastructure.web.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
public class UserControllerTest {
    
    private MockMvc mockMvc;

    @Mock
    private GetUserByIdUseCase getUserByIdUseCase;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("GET /api/user/{id}")
    class GetUserByIdTests {

        @Test
        @DisplayName("Should return user when user exists")
        void shouldReturnUserWhenUserExists() throws Exception {
            // Given
            String userId = "test-user-id";
            User expectedUser = User.builder()
                    .id(userId)
                    .email("john.doe@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .passwordHash("hashed-password")
                    .kycStatus(User.KYCStatus.VERIFIED)
                    .createdAt(ZonedDateTime.now())
                    .build();

            when(getUserByIdUseCase.execute(userId)).thenReturn(expectedUser);

            // When & Then
            mockMvc.perform(get("/api/user/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(userId)))
                    .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                    .andExpect(jsonPath("$.firstName", is("John")))
                    .andExpect(jsonPath("$.lastName", is("Doe")))
                    .andExpect(jsonPath("$.kycStatus", is("VERIFIED")));

            verify(getUserByIdUseCase).execute(userId);
        }

        @Test
        @DisplayName("Should handle valid user ID")
        void shouldHandleValidUserId() throws Exception {
            // Given
            String userId = "123e4567-e89b-12d3-a456-426614174000";
            User expectedUser = User.builder()
                    .id(userId)
                    .email("test@example.com")
                    .firstName("Test")
                    .lastName("User")
                    .passwordHash("hashed-password")
                    .kycStatus(User.KYCStatus.PENDING)
                    .createdAt(ZonedDateTime.now())
                    .build();

            when(getUserByIdUseCase.execute(userId)).thenReturn(expectedUser);

            // When & Then
            mockMvc.perform(get("/api/user/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(userId)))
                    .andExpect(jsonPath("$.kycStatus", is("PENDING")));

            verify(getUserByIdUseCase).execute(userId);
        }
    }

    @Nested
    @DisplayName("POST /api/user/create")
    class CreateUserTests {

        @Test
        @DisplayName("Should create user successfully with valid request")
        void shouldCreateUserSuccessfullyWithValidRequest() throws Exception {
            // Given
            CreateUserRequest request = CreateUserRequest.builder()
                    .firstName("Jane")
                    .lastName("Smith")
                    .email("jane.smith@example.com")
                    .password("securePassword123")
                    .kycStatus(User.KYCStatus.PENDING)
                    .build();

            CreateUserResponse expectedResponse = CreateUserResponse.builder()
                    .id("new-user-id")
                    .email("jane.smith@example.com")
                    .firstName("Jane")
                    .lastName("Smith")
                    .createdAt(ZonedDateTime.now())
                    .build();

            when(createUserUseCase.execute(any(CreateUserRequest.class))).thenReturn(expectedResponse);

            // When & Then
            mockMvc.perform(post("/api/user/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is("new-user-id")))
                    .andExpect(jsonPath("$.email", is("jane.smith@example.com")))
                    .andExpect(jsonPath("$.firstName", is("Jane")))
                    .andExpect(jsonPath("$.lastName", is("Smith")))
                    .andExpect(jsonPath("$.createdAt").exists());

            verify(createUserUseCase).execute(any(CreateUserRequest.class));
        }

        @Test
        @DisplayName("Should handle create user with all KYC statuses")
        void shouldHandleCreateUserWithAllKycStatuses() throws Exception {
            // Given - Testing VERIFIED status
            CreateUserRequest request = CreateUserRequest.builder()
                    .firstName("Bob")
                    .lastName("Johnson")
                    .email("bob.johnson@example.com")
                    .password("strongPassword456")
                    .kycStatus(User.KYCStatus.VERIFIED)
                    .build();

            CreateUserResponse expectedResponse = CreateUserResponse.builder()
                    .id("bob-user-id")
                    .email("bob.johnson@example.com")
                    .firstName("Bob")
                    .lastName("Johnson")
                    .createdAt(ZonedDateTime.now())
                    .build();

            when(createUserUseCase.execute(any(CreateUserRequest.class))).thenReturn(expectedResponse);

            // When & Then
            mockMvc.perform(post("/api/user/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is("bob-user-id")))
                    .andExpect(jsonPath("$.email", is("bob.johnson@example.com")))
                    .andExpect(jsonPath("$.firstName", is("Bob")))
                    .andExpect(jsonPath("$.lastName", is("Johnson")));

            verify(createUserUseCase).execute(any(CreateUserRequest.class));
        }

        @Test
        @DisplayName("Should handle empty request body gracefully")
        void shouldHandleEmptyRequestBody() throws Exception {
            // Given
            CreateUserResponse expectedResponse = CreateUserResponse.builder()
                    .id("empty-user-id")
                    .email(null)
                    .firstName(null)
                    .lastName(null)
                    .createdAt(ZonedDateTime.now())
                    .build();

            when(createUserUseCase.execute(any(CreateUserRequest.class))).thenReturn(expectedResponse);

            // When & Then
            mockMvc.perform(post("/api/user/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andDo(print())
                    .andExpect(status().isOk());

            verify(createUserUseCase).execute(any(CreateUserRequest.class));
        }
    }
}
