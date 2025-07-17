package com.bank.userservice.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class CreateUserResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private ZonedDateTime createdAt;
}
