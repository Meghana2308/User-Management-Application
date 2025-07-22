package com.springboot.usermanagementsystemapplication.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigTestController {
    @Value("${app.environment}")
    private String environment;

    @GetMapping("/env")
    public String getEnvironment() {
        return "Current profile: " + environment;
    }
}
