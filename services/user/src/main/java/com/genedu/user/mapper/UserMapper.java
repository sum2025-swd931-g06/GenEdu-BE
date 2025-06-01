package com.genedu.user.mapper;

import com.genedu.user.dto.customer.UserCreationRequestDTO;
import com.genedu.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toCustomer(UserCreationRequestDTO userCreationRequestDTO) {
        if (userCreationRequestDTO == null) {
            return null;
        }

        return User.builder()
                .firstName(userCreationRequestDTO.firstName())
                .lastName(userCreationRequestDTO.lastName())
                .email(userCreationRequestDTO.email())
                .build();
    }
}
