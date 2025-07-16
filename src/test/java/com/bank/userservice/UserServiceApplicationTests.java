package com.bank.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
		// This test verifies that the Spring context can be loaded successfully
		// Using test profile to avoid database connection issues in CI
	}
}
