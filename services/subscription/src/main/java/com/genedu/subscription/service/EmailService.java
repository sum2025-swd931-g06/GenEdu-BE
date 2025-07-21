package com.genedu.subscription.service;

public interface EmailService {
//    void sendSubscriptionSuccess(String to, String userName, String planName, String endDate, String price, S);
    void sendConfirmationEmail(String to, String userName, String planName, String endDate, String price);
}
