package com.example.demo.services;

import com.example.demo.model.Employee;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(EmployeeRepository employeeRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String username, String password) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Сотрудник не найден"));

        if (passwordEncoder.matches(password, employee.getPassword())) {
            return jwtUtil.generateToken(username, 600_000);  // Токен на 10 минут
        } else {
            throw new RuntimeException("Неверный логин или пароль");
        }
    }

    public String refresh(String oldToken) {
        if (jwtUtil.validateToken(oldToken)) {
            String username = jwtUtil.extractUsername(oldToken);
            return jwtUtil.generateToken(username, 1_800_000);  // Refresh токен на 30 минут
        } else {
            throw new RuntimeException("Некорректный токен");
        }
    }
}
