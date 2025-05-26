package com.genedu.customer.service;

import com.genedu.customer.dto.customer.CustomerRequestDTO;
import com.genedu.customer.model.Customer;
import jakarta.validation.Valid;

public interface CustomerService {
    Customer createCustomer(@Valid CustomerRequestDTO customerRequestDTO);
    void updateCustomer(@Valid CustomerRequestDTO customerRequestDTO, String customerId);
}
