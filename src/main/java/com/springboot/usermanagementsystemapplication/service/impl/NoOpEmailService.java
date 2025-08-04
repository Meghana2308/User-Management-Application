package com.springboot.usermanagementsystemapplication.service.impl;

import com.springboot.usermanagementsystemapplication.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(
        prefix = "features",
        name = "email-enabled",
        havingValue = "false",
        matchIfMissing = true
)
public class NoOpEmailService implements EmailService {

    @Override
    public void sendWelcomeEmail(String toEmail) {
        log.info("Email feature is disabled. Skipping email for: {}", toEmail);
    }
}

