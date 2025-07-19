package com.genedu.subscription.dto.usertransaction;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public record UserTransactionRequestDTO(String accountId,
                                        BigDecimal amount,
                                        String status) {
}
