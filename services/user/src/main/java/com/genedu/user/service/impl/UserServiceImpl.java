package com.genedu.user.service.impl;

import com.genedu.user.dto.customer.UserCreationRequestDTO;
import com.genedu.user.exception.UserNotFoundException;
import com.genedu.user.mapper.UserMapper;
import com.genedu.user.model.User;
import com.genedu.user.repository.UserRepository;
import com.genedu.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User createUser(UserCreationRequestDTO userCreationRequestDTO) {
        return userRepository.save(userMapper.toCustomer(userCreationRequestDTO));
    }

    @Override
    public void updateUser(UserCreationRequestDTO userCreationRequestDTO, UUID customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID must not be null for update");
        }
        User user = userMapper.toCustomer(userCreationRequestDTO);
        User existingCustomer = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Customer not found with ID: " + user.getUserId()));

        mergerUser(existingCustomer, user);
        userRepository.save(existingCustomer);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private void mergerUser(User existingCustomer, User customer) {
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
