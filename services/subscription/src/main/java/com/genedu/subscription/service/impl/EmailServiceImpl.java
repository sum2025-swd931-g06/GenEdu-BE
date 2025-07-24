package com.genedu.subscription.service.impl;

import com.genedu.subscription.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;


    @Override
    public void sendConfirmationEmail(String to, String userName, String planName, String endDate, String price) {
        log.info("Sending confirmation email to: {}, User: {}, Plan: {}, End Date: {}, Price: {}",
                to, userName, planName, endDate, price);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("You have successfully signed up for the GenEdu subscription.");
            message.setText(String.format("Dear %s,\n\n" +
                    "Thank you for subscribing to our service.\n" +
                    "You have chosen the %s plan, which will end on %s.\n" +
                    "The total price is %s.\n\n" +
                    "Best regards,\n" +
                    "The GenEdu Team", userName, planName, endDate, price));

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send confirmation email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send confirmation email", e);
        }
    }

    @Override
    public void sendCancellationEmail(String to, String userName, String planName, String endDate) {
        log.info("Sending cancellation email to: {}, User: {}, Plan: {}, End Date: {}",
                to, userName, planName, endDate);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your GenEdu subscription has been cancelled.");
            message.setText(String.format("Dear %s,\n\n" +
                    "Your subscription plan (%s) has been successfully cancelled for automatic renewal.\n" +
                    "You can continue to enjoy the service until: %s.\n\n" +
                    "Thank you for being with us.\n" +
                    "Best regards,\n" +
                    "The GenEdu Team", userName, planName, endDate));

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send cancellation email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send cancellation email", e);
        }
    }

    @Override
    public void sendExpiredEmail(String to, String userName, String planName, String endDate) {
        log.info("Sending expired email to: {}, User: {}, Plan: {}, End Date: {}",
                to, userName, planName, endDate);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your GenEdu subscription has expired.");
            message.setText(String.format("Dear %s,\n\n" +
                    "We regret to inform you that your subscription plan (%s) has expired on %s.\n" +
                    "We hope you enjoyed our service and consider renewing your subscription.\n\n" +
                    "Best regards,\n" +
                    "The GenEdu Team", userName, planName, endDate));

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send expired email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send expired email", e);
        }

    }
}
