package com.example.supercourse.controllers;

import com.example.supercourse.models.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @GetMapping("/dashboard")
    public String adminDashboard(@AuthenticationPrincipal User user) {

        return "Welcome, admin with name " + user.getUsername() + "!";
    }
}
