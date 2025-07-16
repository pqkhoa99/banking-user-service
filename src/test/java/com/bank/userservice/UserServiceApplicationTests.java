package com.bank.userservice;

import com.bank.userservice.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Import(TestConfig.class)
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
		// This test verifies that the Spring context can be loaded successfully
		// Using test profile with H2 database to avoid connection issues in CI
	}
}
