package com.ecommerce.userservice;

import com.ecommerce.userservice.config.TestcontainersDatabaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestcontainersDatabaseConfig.class)
class UserApplicationTests {

	@Test
	void contextLoads() {
	}

}
