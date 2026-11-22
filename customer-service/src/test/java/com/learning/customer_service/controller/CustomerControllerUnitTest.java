package com.learning.customer_service.controller;

import com.learning.customer_service.service.CustomerService;
import com.learning.customer_service.model.Customer;
import com.learning.customer_service.mapper.CustomerMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private CustomerMapper customerMapper;

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnPagedCustomersSuccessfully() throws Exception {

        // Arrange
        Page<Customer> mockPage = new PageImpl<>(List.of());

        when(customerService.getCustomersPage(any(Pageable.class)))
                .thenReturn(mockPage);

        // Act and Assert
        mockMvc.perform(get("/api/customers/paged")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk());
    }
}