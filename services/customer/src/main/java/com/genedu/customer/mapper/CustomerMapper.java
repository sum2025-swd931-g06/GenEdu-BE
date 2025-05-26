package com.genedu.customer.mapper;

import com.genedu.customer.dto.customer.CustomerRequestDTO;
import com.genedu.customer.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public Customer toCustomer(CustomerRequestDTO customerRequestDTO) {
        if (customerRequestDTO == null) {
            return null;
        }

        return Customer.builder()
                .firstName(customerRequestDTO.firstName())
                .lastName(customerRequestDTO.lastName())
                .email(customerRequestDTO.email())
                .build();
    }
}
