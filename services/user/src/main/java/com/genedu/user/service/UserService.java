package com.genedu.user.service;

import com.genedu.user.dto.customer.UserCreationRequestDTO;
import com.genedu.user.dto.customer.UserListResponseDTO;
import com.genedu.user.dto.customer.UserResponseDTO;
import com.genedu.user.model.User;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public UserResponseDTO getUserProfile(String userId);

    UserListResponseDTO getAllUsers(int pageNo);

    boolean isUserExists(String userId);

    void clearUserSession(String name);
}
