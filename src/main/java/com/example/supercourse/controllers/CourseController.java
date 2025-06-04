package com.example.supercourse.controllers;

import com.example.supercourse.dao.CourseRequest;
import com.example.supercourse.models.Course;
import com.example.supercourse.models.Photo;
import com.example.supercourse.models.User;
import com.example.supercourse.repositories.CourseRepository;
import com.example.supercourse.repositories.PhotoRepository;
import com.example.supercourse.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {
    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private PhotoRepository photoRepository;
    private final Path uploadRoot = Paths.get("uploads/photos");
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> courses(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        User user1 = userRepository.findByEmail(user.getUsername()).orElseThrow();
        List<Course> courses = user1.getCourses();
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }
    @GetMapping("/course-rate")
    public ResponseEntity<List<Course>> courseRate() {
        List<Course> courses = courseRepository.findAll().stream().sorted(Comparator.comparing(Course::getScore)).toList();
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Course> courseResponseEntity(@PathVariable long id,
                                                       @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        Course course = courseRepository.findById(id).orElseThrow();
        if (!course.getUser().getEmail().equals(user.getUsername())) {
            course.incrementScore();
            course = courseRepository.save(course);
        }
        return ResponseEntity.status(HttpStatus.OK).body(course);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createCourse(
            @RequestPart("course") CourseRequest courseRequest,
            @RequestPart("files") MultipartFile[] files,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal
    ) throws IOException {
        // Найдём пользователя по email
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Создаём курс
        Course course = new Course();
        course.setName(courseRequest.getTitle());
        course.setDesc(courseRequest.getDescription());
        course.setUser(user);
        course = courseRepository.save(course);

        // Создаём папку при необходимости
        Files.createDirectories(uploadRoot);

        // Обработка файлов
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadRoot.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Photo photo = new Photo();
            photo.setPath(filePath.toString());
            photo.setCourse(course);
            photoRepository.save(photo);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Курс создан");
    }
}
