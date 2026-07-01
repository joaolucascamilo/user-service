package com.usuario;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"brevo.api.key=test-key",
		"brevo.remetente.email=test@example.com"
})
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
