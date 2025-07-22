package com.genedu.subscription.controller;

import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.subscription.dto.userbillingaccount.UserBillingAccountResponseDTO;
import com.genedu.subscription.service.UserBillingAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions/user-billing-accounts")
@Tag(name = "User Billing Account", description = "User Billing Account API")
public class UserBillingAccountController {
    private final UserBillingAccountService userBillingAccountService;

    @GetMapping
    public ResponseEntity<String> getUserId() {
        String userId = AuthenticationUtils.getUserId().toString();
        return ResponseEntity.ok(userId);
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<UserBillingAccountResponseDTO> getByUserId(@PathVariable String userId) {
//        Optional<UserBillingAccountResponseDTO> result = userBillingAccountService.findByUserId(userId);
//        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{userId}/payment-gateway-customer-id")
//    public ResponseEntity<Void> updatePaymentGatewayCustomerId(@PathVariable String userId, @RequestParam String customerId) {
//        userBillingAccountService.updatePaymentGatewayCustomerId(userId, customerId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping("/{userId}/subscription-status")
//    public ResponseEntity<Void> updateSubscriptionStatus(@PathVariable String userId, @RequestParam Boolean status) {
//        userBillingAccountService.updateSubscriptionStatus(userId, status);
//        return ResponseEntity.noContent().build();
//    }
}
