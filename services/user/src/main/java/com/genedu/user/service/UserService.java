package com.genedu.user.service;

import com.genedu.user.dto.customer.UserCreationRequestDTO;
import com.genedu.user.model.User;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(@Valid UserCreationRequestDTO userCreationRequestDTO);
    void updateUser(@Valid UserCreationRequestDTO userCreationRequestDTO, UUID customerId);

    List<User> getAllUsers();
}
