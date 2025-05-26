package com.genedu.customer.controller;

import com.genedu.customer.dto.customer.CreateCustomerValidatorGroup;
import com.genedu.customer.dto.customer.CustomerRequestDTO;
import com.genedu.customer.model.Customer;
import com.genedu.customer.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(
        @RequestBody @Validated({Default.class, CreateCustomerValidatorGroup.class}) CustomerRequestDTO customerRequestDTO
    ) {
        return ResponseEntity.ok(customerService.createCustomer(customerRequestDTO));
    }

    @PutMapping("{customerId}")
    public ResponseEntity<Void> updateCustomer(
        @PathVariable String customerId,
        @RequestBody @Validated({Default.class}) CustomerRequestDTO customerRequestDTO
    ) {
        customerService.updateCustomer(customerRequestDTO, customerId);
        return ResponseEntity.accepted().build();
    }
}
