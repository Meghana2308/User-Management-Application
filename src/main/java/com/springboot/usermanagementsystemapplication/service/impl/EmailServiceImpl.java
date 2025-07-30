package com.springboot.usermanagementsystemapplication.service.impl;

import com.springboot.usermanagementsystemapplication.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Async
    @Override
    public void sendWelcomeEmail(String toEmail) {
        log.info("Sending welcome email to: {}", toEmail);

        try {
            Thread.sleep(3000); // Simulate delay (e.g., SMTP call)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("Email sent to: {}", toEmail);
    }
}
