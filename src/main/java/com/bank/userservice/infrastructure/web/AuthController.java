package com.bank.userservice.infrastructure.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping
    String hello(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return "Hello %s".formatted(oAuth2User.getName());
    }
}
