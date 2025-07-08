package com.genedu.subscription.dto;

public record WebhookRequest (String payload, String signature) {}
