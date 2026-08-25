package com.example.student;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final List<Student> students = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    // Register student
    @PostMapping
    public ResponseEntity<Student> registerStudent(@Valid @RequestBody Student student) {
        student.setStudentId(idCounter.getAndIncrement());
        students.add(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    // View all students
    @GetMapping
    public List<Student> getAllStudents() {
        return students;
    }

    // Find by id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return students.stream()
                .filter(s -> s.getStudentId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete registration
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        boolean removed = students.removeIf(s -> s.getStudentId().equals(id));
        if (removed) {
            return ResponseEntity.ok("Registration deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
