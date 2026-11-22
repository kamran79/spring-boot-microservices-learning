
package com.learning.customer_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    private String loginAndGetToken(
            String username, String password) throws Exception {

        String body = """
                {
                  "username": "%s",
                  "password": "%s"
                }
                """.formatted(username, password);

        String response = mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return com.jayway.jsonpath.JsonPath.read(
                response, "$.accessToken");
    }

    @Test
    void shouldRejectRequestWithoutToken() throws Exception {

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowReaderToGetCustomers() throws Exception {

        String token = loginAndGetToken(
                "reader", "Reader@123");

        mockMvc.perform(
                        get("/api/customers")
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldForbidReaderFromCreatingCustomer() throws Exception {

        String token = loginAndGetToken(
                "reader", "Reader@123");

        mockMvc.perform(
                        post("/api/customers")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Reader Test",
                                          "email": "reader.junit@example.com"
                                        }
                                        """))
                .andExpect(status().isForbidden());
    }
    @Test
    void shouldForbidReaderFromDeletingCustomer() throws Exception {
        // Step 1: Login as reader and obtain JWT
        String token = loginAndGetToken(
                "reader", "Reader@123"
        );

        // Step 2: Attempt to delete a customer using reader JWT
        mockMvc.perform(
                        delete("/api/customers/1")
                                .header("Authorization", "Bearer " + token)
                )
                // Step 3: Verify reader is forbidden
                .andExpect(status().isForbidden());
    }
    @Test
    void shouldRejectIncorrectPassword() throws Exception {

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "username": "reader",
                                          "password": "WrongPassword"
                                        }
                                        """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectInvalidJwt() throws Exception {

        mockMvc.perform(
                        get("/api/customers")
                                .header("Authorization",
                                        "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowAdminToCreateCustomer() throws Exception {

        String token = loginAndGetToken(
                "admin", "Admin@123");

        mockMvc.perform(
                        post("/api/customers")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Admin Test",
                                          "email": "admin.junit@example.com"
                                        }
                                        """))
                .andExpect(status().isCreated());
    }
}
