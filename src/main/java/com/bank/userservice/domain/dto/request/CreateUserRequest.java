package com.bank.userservice.domain.dto.request;

import com.bank.userservice.domain.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {

    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public User.KYCStatus kycStatus;
    
}
