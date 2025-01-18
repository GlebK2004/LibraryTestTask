package com.example.demo.controllers;

import com.example.demo.services.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return authService.login(username, password);
    }

    @PostMapping("/refresh")
    public String refresh(@RequestParam String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
