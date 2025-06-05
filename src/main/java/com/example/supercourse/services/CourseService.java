package com.example.supercourse.services;

import com.example.supercourse.models.Course;
import com.example.supercourse.models.User;
import com.example.supercourse.repositories.CourseRepository;
import com.example.supercourse.repositories.PhotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
 public class CourseService {
     @Autowired
     private CourseRepository courseRepository;
     @Autowired
     private PhotoRepository photoRepository; // Still needed if you kept manual photo deletion

     @Transactional // This is crucial!
     public void deleteCourse(long id, User user) {
         System.out.println("SERVICE: Attempting to find course with ID: " + id);
         Course course = courseRepository.findById(id)
                 .orElseThrow(() -> {
                     System.err.println("SERVICE: Course with ID " + id + " not found.");
                     return new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
                 });

         System.out.println("SERVICE: Found course: " + course.getName() + ". Verifying owner...");
         if (!course.getUser().equals(user)) { // Убедитесь, что User.equals() реализован корректно (сравнение по ID)!
             System.err.println("SERVICE: Forbidden. User " + user.getEmail() + " is not owner of course " + course.getName());
             throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: You are not the owner of this course");
         }

         System.out.println("SERVICE: DEEEEEEEEEEEEEEEEEEEEELLLLLLLLLLL for course: " + course.getName());
         System.out.println("SERVICE: About to delete course: " + course); // course.toString() может вызвать загрузку фото, если LAZY

         try {
             courseRepository.delete(course); // Hibernate должен сам удалить связанные фото из-за CascadeType.ALL и orphanRemoval=true
             // courseRepository.flush(); // Можно добавить для немедленной отправки SQL в БД (но коммит все равно решает)
             System.out.println("SERVICE: courseRepository.delete(course) called. If transaction commits, course and photos should be deleted.");
         } catch (Exception e) {
             System.err.println("SERVICE: Exception DURING courseRepository.delete() or flush(): " + e.getMessage());
             e.printStackTrace(); // ВАЖНО! Посмотреть этот стектрейс
             throw e; // Перебрасываем, чтобы транзакция точно откатилась
         }
     }
 }