package com.learning.customer_service.mapper;

import com.learning.customer_service.dto.CustomerRequest;
import com.learning.customer_service.dto.CustomerResponse;
import com.learning.customer_service.model.Customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    Customer toEntity(CustomerRequest request);

    CustomerResponse toResponse(Customer customer);
}