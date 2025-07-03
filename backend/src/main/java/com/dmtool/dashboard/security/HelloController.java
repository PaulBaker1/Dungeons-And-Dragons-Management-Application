package com.dmtool.dashboard.security;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String hello(Authentication auth) {
        return "Hello, " + auth.getName() + "! You are in role(s): " + auth.getAuthorities();
    }
}