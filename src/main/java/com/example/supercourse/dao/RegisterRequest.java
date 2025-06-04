package com.example.supercourse.dao;

import com.example.supercourse.models.enums.Roles;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Roles role; // добавляем роль
}