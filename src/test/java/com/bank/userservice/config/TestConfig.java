package com.bank.userservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestConfig {
    // This configuration helps ensure all beans can be created during context loading
    // H2 database from application-test.properties will be used instead of PostgreSQL
}
