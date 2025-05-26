package com.genedu.customer.service.impl;

import com.genedu.customer.CustomerApplication;
import com.genedu.customer.dto.customer.CustomerRequestDTO;
import com.genedu.customer.exception.CustomerNotFoundException;
import com.genedu.customer.mapper.CustomerMapper;
import com.genedu.customer.model.Customer;
import com.genedu.customer.repository.CustomerRepository;
import com.genedu.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Customer createCustomer(CustomerRequestDTO customerRequestDTO) {
        return customerRepository.save(customerMapper.toCustomer(customerRequestDTO));
    }

    @Override
    public void updateCustomer(CustomerRequestDTO customerRequestDTO, String customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID must not be null for update");
        }
        Customer customer = customerMapper.toCustomer(customerRequestDTO);
        Customer existingCustomer = customerRepository.findById(customer.getId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customer.getId()));

        mergerCustomer(existingCustomer, customer);
        customerRepository.save(existingCustomer);
    }

    private void mergerCustomer(Customer existingCustomer, Customer customer) {
        if(StringUtils.isNotBlank(customer.getFirstName())) {
            existingCustomer.setFirstName(customer.getFirstName());
        }
        if(StringUtils.isNotBlank(customer.getLastName())) {
            existingCustomer.setLastName(customer.getLastName());
        }
        if(StringUtils.isNotBlank(customer.getEmail())) {
            existingCustomer.setEmail(customer.getEmail());
        }
    }
}
