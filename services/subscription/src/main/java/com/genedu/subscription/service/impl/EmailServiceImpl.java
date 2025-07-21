package com.genedu.subscription.service.impl;

import com.genedu.subscription.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendConfirmationEmail(String to, String userName, String planName, String endDate, String price) {
        log.info("Sending confirmation email to: {}, User: {}, Plan: {}, End Date: {}, Price: {}",
                to, userName, planName, endDate, price);
    }
}
