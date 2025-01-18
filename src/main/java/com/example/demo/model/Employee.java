package com.example.demo.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // Конструктор без аргументов (аналог @NoArgsConstructor)
    public Employee() {
    }

    // Конструктор со всеми аргументами (аналог @AllArgsConstructor)
    public Employee(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Геттеры и сеттеры (аналог @Data)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
