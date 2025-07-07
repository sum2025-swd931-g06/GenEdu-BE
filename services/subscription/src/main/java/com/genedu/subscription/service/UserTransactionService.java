package com.genedu.subscription.service;

import com.genedu.subscription.dto.usertransaction.UserTransactionRequestDTO;
import com.genedu.subscription.dto.usertransaction.UserTransactionResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserTransactionService {
    UserTransactionResponseDTO createTransaction(UserTransactionRequestDTO requestDTO);
    void updateTransactionStatus(UUID txId, String status);
    List<UserTransactionResponseDTO> getTransactionsByUserId(UUID userId);
    UserTransactionResponseDTO getTransactionById(UUID txId);
}
