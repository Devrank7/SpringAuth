package com.example.supercourse.services;

import com.example.supercourse.dao.RegisterRequest;
import com.example.supercourse.models.User;
import com.example.supercourse.models.enums.Roles;
import com.example.supercourse.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email уже используется");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Если роль не передана — по умолчанию USER
        user.setRole(request.getRole() != null ? request.getRole() : Roles.USER);

        return userRepository.save(user);
    }

}
