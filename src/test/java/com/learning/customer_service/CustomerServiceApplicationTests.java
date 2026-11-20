
package com.learning.customer_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.learning.customer_service.model.Customer;
import com.learning.customer_service.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
@DirtiesContext(
		classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
)
class CustomerServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CustomerRepository customerRepository;

	private Long aliId;
	private Long saraId;

	@BeforeEach
	void setUp() {

		customerRepository.deleteAll();

		Customer ali = customerRepository.save(
				new Customer(null, "Ali", "ali@example.com")
		);

		Customer sara = customerRepository.save(
				new Customer(null, "Sara", "sara@example.com")
		);

		aliId = ali.getId();
		saraId = sara.getId();
	}

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
				MockMvcRequestBuilders.get("/api/customers/" + saraId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(saraId))
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
				MockMvcRequestBuilders.put("/api/customers/"+aliId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
		).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(aliId))
				.andExpect(jsonPath("$.name").value("Ali Updated"))
				.andExpect(jsonPath("$.email")
						.value("ali.updated@example.com"));

		// Verify updated data using GET
		mockMvc.perform(
						MockMvcRequestBuilders.get("/api/customers/" + aliId))

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
						MockMvcRequestBuilders.put("/api/customers/99")
								.contentType(MediaType.APPLICATION_JSON)
								.content(requestBody)
				)
				.andExpect(status().isNotFound());
	}

	@Test
	void testDeleteCustomer() throws Exception {

		// Delete customer ID 2
		mockMvc.perform(
						MockMvcRequestBuilders.delete("/api/customers/"+saraId)
		).andExpect(status().isNoContent());

		// Verify customer was deleted
		mockMvc.perform(
						MockMvcRequestBuilders.get("/api/customers/"+saraId)
				)
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


	@Test
	void testCreateCustomerValidationFailed() throws Exception {

		// Arrange: prepare invalid request data
		String requestBody = """
        {
            "name": "",
            "email": "wrong-email"
        }
        """;

		// Act: send a simulated HTTP POST request
		mockMvc.perform(
						MockMvcRequestBuilders.post("/api/customers")
								.contentType(MediaType.APPLICATION_JSON)
								.content(requestBody)
				)

				// Assert: verify the response
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message")
						.value("Request validation failed"))
				.andExpect(jsonPath("$.fieldErrors.name")
						.value("Customer name is required"))
				.andExpect(jsonPath("$.fieldErrors.email")
						.value("Email must be valid"));
	}

	@Test
	void testGetCustomerNotFoundErrorResponse() throws Exception {

		// Arrange: prepare invalid request data
		String requestBody = """
        {
            "name": "Unknown Customer",
             "email": "unknown@example.com"
        }
        
        """;

		// Act: send a simulated HTTP get request
		mockMvc.perform(
						MockMvcRequestBuilders.get("/api/customers/999")
								.contentType(MediaType.APPLICATION_JSON)
								.content(requestBody)
				)

				// Assert: verify the response
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message")
						.value("Customer not found with ID: 999"));

	}

	@Test
	void testCreateCustomerMalformedJson() throws Exception {

		// Arrange: prepare invalid request data
		String requestBody = """
        {
           "name": "Ahmed", "email":
        }
        
        """;

		// Act: send a simulated HTTP get request
		mockMvc.perform(
						MockMvcRequestBuilders.post("/api/customers")
								.contentType(MediaType.APPLICATION_JSON)
								.content(requestBody)
				)

				// Assert: verify the response
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message")
						.value("Malformed or missing JSON request body"));

	}

}
