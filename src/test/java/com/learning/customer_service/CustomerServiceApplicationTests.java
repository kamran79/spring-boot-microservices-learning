
package com.learning.customer_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
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
