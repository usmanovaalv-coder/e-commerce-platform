package com.ecommerce.productservice;

import com.ecommerce.productservice.config.TestcontainersDatabaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestcontainersDatabaseConfig.class)
class ProductManagerServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
