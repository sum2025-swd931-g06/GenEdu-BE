package com.genedu.subscription.service.impl;

import com.genedu.subscription.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.naming.ldap.PagedResultsControl;

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
}
