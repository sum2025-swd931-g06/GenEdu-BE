package com.genedu.subscription.dto.usertransaction;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public record UserTransactionRequestDTO(String accountId,
                                        BigDecimal amount,
                                        String status) {
}
