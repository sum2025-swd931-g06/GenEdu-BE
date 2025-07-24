package com.genedu.subscription.configuration;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class StripeConfig {
    @Value("${stripe.secretKey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }
    @Value("${stripe.publicKey}")
    private String publicKey;
    @Value("${stripe.webhookSecret}")
    private String webhookSecret;
    @Value("${stripe.successUrl}")
    private String successUrl;
    @Value("${stripe.cancelUrl}")
    private String cancelUrl;
}
