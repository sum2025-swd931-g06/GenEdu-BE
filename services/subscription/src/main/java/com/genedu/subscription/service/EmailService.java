package com.genedu.subscription.service;

public interface EmailService {
    void sendConfirmationEmail(String to, String userName, String planName, String endDate, String price);
    void sendCancellationEmail(String to, String userName, String planName, String endDate);
    void sendExpiredEmail(String to, String userName, String planName, String endDate);
}
