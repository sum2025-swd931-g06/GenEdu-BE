package com.genedu.customer.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document   // This annotation indicates that this class is a MongoDB document
public class Customer {
    @Id
    private String id;         // Unique identifier for the customer, typically a UUID or similar
    private String firstName;  // First name of the customer
    private String lastName;   // Last name of the customer
    private String email;      // Email address of the customer
}
