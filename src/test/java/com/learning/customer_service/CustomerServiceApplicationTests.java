
package com.learning.customer_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(
		classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
)
class CustomerServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void testGetAllCustomers() throws Exception {
		mockMvc.perform(
						MockMvcRequestBuilders.get("/api/customers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Ali"))
				.andExpect(jsonPath("$[1].name").value("Sara"));
	}

	@Test
	void testGetCustomerById() throws Exception {
		mockMvc.perform(
						MockMvcRequestBuilders.get("/api/customers/2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(2))
				.andExpect(jsonPath("$.name").value("Sara"))
				.andExpect(jsonPath("$.email")
						.value("sara@example.com"));
	}

	@Test
	void testUpdateCustomerById() throws Exception {
		String requestBody = """
        {
            "name": "Ali Updated",
            "email": "ali.updated@example.com"
        }
        """;

		mockMvc.perform(
				MockMvcRequestBuilders.put("/api/customers/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
		).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Ali Updated"))
				.andExpect(jsonPath("$.email")
						.value("ali.updated@example.com"));

		// Verify updated data using GET
		mockMvc.perform(
						MockMvcRequestBuilders.get("/api/customers/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Ali Updated"));
	}

	@Test
	void testUpdateCustomerNotFound() throws Exception {

		String requestBody = """
        {
            "name": "Unknown",
            "email": "unknown@example.com"
        }
        """;

		mockMvc.perform(
						MockMvcRequestBuilders.get("/api/customers/99")
								.contentType(MediaType.APPLICATION_JSON)
								.content(requestBody)
				)
				.andExpect(status().isNotFound());
	}

	@Test
	void testDeleteCustomer() throws Exception {

		// Delete customer ID 2
		mockMvc.perform(
						MockMvcRequestBuilders.delete("/api/customers/2")
				).andExpect(status().isNotFound());

		// Verify customer no longer exists
		mockMvc.perform(
						MockMvcRequestBuilders.get("/api/customers/2"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testDeleteCustomerNotFound() throws Exception {

		// Verify customer no longer exists
		mockMvc.perform(
						MockMvcRequestBuilders.get("/api/customers/777"))
				.andExpect(status().isNotFound());

	}

	@Test
	void testCustomerNotFound() throws Exception {
		mockMvc.perform(
						MockMvcRequestBuilders.get("/api/customers/99"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testCustomerCount() throws Exception {
		mockMvc.perform(
						MockMvcRequestBuilders.get("/api/customers/count"))
				.andExpect(status().isOk())
				.andExpect(content().string("2"));
	}
}
