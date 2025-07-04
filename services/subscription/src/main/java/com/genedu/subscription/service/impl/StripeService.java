package com.genedu.subscription.service.impl;

import com.genedu.subscription.dto.WebhookRequest;
import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.dto.userbillingaccount.UserBillingAccountResponseDTO;
import com.genedu.subscription.model.SubscriptionPlan;
import com.genedu.subscription.model.UserBillingAccount;
import com.genedu.subscription.service.PaymentGatewayService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService implements PaymentGatewayService {
    @Override
    public Map<String, Object> createCheckoutSession(UserBillingAccountResponseDTO billing, SubscriptionPlanResponseDTO subscriptionPlan) throws StripeException {

//        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                .setName("Subscription for " + billing.userId())
//                .build();
//
//        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
//                .setCurrency("VND") // Assuming VND for Vietnamese Dong
//                .setUnitAmount(subscriptionPlan.price().longValue()) // Example amount, replace with actual value
//                .setProductData(productData)
//                .build();
//
//        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
//                .setQuantity(1L) // Assuming quantity is 1 for subscription
//                .setPriceData(priceData)
//                .build();
//
//        SessionCreateParams params = SessionCreateParams.builder()
//                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
//                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.ALIPAY)
//                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
//                .setSuccessUrl("") // Replace with actual success URL
//                .setCancelUrl("") // Replace with actual cancel URL
//                .addLineItem(lineItem)
//                .build();
//        SessionCreateParams params = SessionCreateParams.builder()
//                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
//                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
//                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.PAYPAL)
//                .setCustomer(billing.paymentGatewayCustomerId())
//                .setSuccessUrl("")
//                .setCancelUrl("")
//                .addLineItem(
//                        SessionCreateParams.LineItem.builder()
//                                .setPriceData(
//                                        SessionCreateParams.LineItem.PriceData.builder()
//                                                .setCurrency("VND")
//                                                .setUnitAmount(subscriptionPlan.price().longValue() * 100) // Convert to smallest currency unit
//                                                .setProductData(
//                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                                                                .setName("Subscription for " + billing.userId())
//                                                                .build()
//                                                )
//                                                .build()
//                                )
//                                .setQuantity(1L)
//                                .build()
//                )
//                .build();
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.PAYPAL)
                .setCustomer(billing.paymentGatewayCustomerId())
                .setSuccessUrl("")
                .setCancelUrl("")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("VND") // Assuming VND for Vietnamese Dong
                                                .setUnitAmount(subscriptionPlan.price().longValue() * 100) // Convert to smallest currency unit
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Subscription for " + billing.userId())
                                                                .build()
                                                )
                                                .build()
                                )
                                .setQuantity(1L)
                                .build()
                )
                .build();
        Session session = Session.create(params);
        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", session.getId());
        return result;
    }

    @Override
    public void cancelSubscription(String gatewaySubscriptionId) {

    }

    @Override
    public void handleWebhookEvent(WebhookRequest webhookRequest) {

    }

    @Override
    public boolean isWebhookSignatureValid(WebhookRequest webhookRequest) {
        return false;
    }
}
