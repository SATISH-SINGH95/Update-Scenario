package com.satish.controller;

import com.satish.dto.StudentDto;
import com.satish.repository.StudentRepository;
import com.satish.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public StudentController(StudentService studentService,
                             StudentRepository studentRepository){
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestBody StudentDto studentDto){
        if(studentDto.getId() != null){
            throw new RuntimeException("Student already created");
        }
        StudentDto save = studentService.save(studentDto);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable("id") Long id, @RequestBody StudentDto studentDto){
        if(studentDto.getId() == null){
            throw new RuntimeException("Invalid Id");
        }
        if(!Objects.equals(id, studentDto.getId())){
            throw new RuntimeException("Invalid Id");
        }
        if(!studentRepository.existsById(id)){
            throw new RuntimeException("Student not found");
        }

        StudentDto dto = studentService.updateStudent(studentDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable("id") Long id){
        studentService.delete(id);
        return new ResponseEntity<>("Record deleted", HttpStatus.OK);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDto> findOne(@PathVariable("id") Long id){
        StudentDto dto = studentService.findOne(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentDto>> findAll(){
        List<StudentDto> dto = studentService.findAll();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


}
