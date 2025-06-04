package com.example.supercourse.dao;

import com.example.supercourse.models.enums.Roles;
import lombok.Data;

@Data
public class UpdateRequest {
    private String email;
    private String password;
    private Roles role;
}
