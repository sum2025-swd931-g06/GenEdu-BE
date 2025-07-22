package com.genedu.subscription.service;

import com.genedu.subscription.dto.usertransaction.UserTransactionRequestDTO;
import com.genedu.subscription.dto.usertransaction.UserTransactionResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserTransactionService {
    void createTransaction(UserTransactionRequestDTO requestDTO);
    List<UserTransactionResponseDTO> getTransactionsByUserId(UUID userId);
    UserTransactionResponseDTO getTransactionById(UUID txId);
}
