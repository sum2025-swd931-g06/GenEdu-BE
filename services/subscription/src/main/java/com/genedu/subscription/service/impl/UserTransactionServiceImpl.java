package com.genedu.subscription.service.impl;

import com.genedu.commonlibrary.exception.InternalServerErrorException;
import com.genedu.subscription.dto.usertransaction.UserTransactionRequestDTO;
import com.genedu.subscription.dto.usertransaction.UserTransactionResponseDTO;
import com.genedu.subscription.model.UserTransaction;
import com.genedu.subscription.repository.UserTransactionRepository;
import com.genedu.subscription.service.UserBillingAccountService;
import com.genedu.subscription.service.UserTransactionService;
import com.genedu.subscription.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTransactionServiceImpl implements UserTransactionService {

    private final UserTransactionRepository transactionRepository;
    private final UserBillingAccountService billingAccountService;

    @Override
    public void createTransaction(UserTransactionRequestDTO requestDTO) {
        var account = billingAccountService.findByStripeCustomerId(requestDTO.accountId());

        try {
            ZoneId saigonZone = ZoneId.of("Asia/Saigon");
            var transaction = UserTransaction.builder()
                    .id(UUID.randomUUID())
                    .account(account)
                    .amount(requestDTO.amount())
                    .status(requestDTO.status())
                    .createdAt(ZonedDateTime.now(saigonZone).toLocalDateTime())
                    .build();
            transactionRepository.save(transaction);
        } catch (Exception e) {
            log.error("Error creating transaction for account {}: {}", requestDTO.accountId(), e.getMessage(), e);
            throw new InternalServerErrorException(Constants.ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateTransactionStatus(UUID txId, String status) {

    }

    @Override
    public List<UserTransactionResponseDTO> getTransactionsByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public UserTransactionResponseDTO getTransactionById(UUID txId) {
        return null;
    }
}
