package com.example.supercourse.controllers;


import com.example.supercourse.dao.UpdateRequest;
import com.example.supercourse.models.User;
import com.example.supercourse.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private UserRepository userRepository;
    @GetMapping("/my")
    public ResponseEntity<User> getUser1(@AuthenticationPrincipal User user) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> getDelete(@AuthenticationPrincipal User user) {
        userRepository.delete(user);
        return ResponseEntity.ok("Delete User");
    }
    @PutMapping("/update")
    public ResponseEntity<?> getUpdate(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateRequest updateRequest
    ) {

        if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }

        if (updateRequest.getPassword() != null) {
            // Хешируем новый пароль
            String encoded = new BCryptPasswordEncoder().encode(updateRequest.getPassword());
            user.setPassword(encoded);
        }
        if (updateRequest.getRole() != null) {
            user.setRole(updateRequest.getRole());
        }
        userRepository.save(user);
        return ResponseEntity.ok("Пользователь обновлён");
    }
}
