package com.genedu.subscription.controller;

import com.genedu.commonlibrary.exception.AccessDeniedException;
import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.subscription.service.PaymentGatewayService;
import com.genedu.subscription.service.SubscriptionPlanService;
import com.genedu.subscription.service.UserBillingAccountService;
import com.genedu.subscription.utils.Constants;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/subscriptions/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentGatewayService paymentGatewayService;
    private final UserBillingAccountService userBillingAccountService;
    private final SubscriptionPlanService subscriptionPlanService;

    @PostMapping("/create-checkout-session/{subscriptionPlanId}")
    public Map<String, Object> createCheckoutSession(@PathVariable String subscriptionPlanId) throws StripeException {
        var userId = AuthenticationUtils.getUserId();
        if (userId == null) {
            throw new AccessDeniedException(Constants.ErrorCode.UNAUTHENTICATED);
        }
        var billingAccount = userBillingAccountService.getOrCreateUserBillingAccount(userId.toString());

        var subscriptionPlan = subscriptionPlanService.getSubscriptionPlan(subscriptionPlanId)
                .orElseThrow(() -> new BadRequestException(Constants.ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND, subscriptionPlanId));
        var result = paymentGatewayService.createCheckoutSession(billingAccount, subscriptionPlan);
        return ResponseEntity.ok(result).getBody();
    }
}
