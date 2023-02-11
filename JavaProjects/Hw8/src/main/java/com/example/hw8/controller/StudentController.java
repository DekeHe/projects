package com.example.hw8.controller;

import com.example.hw8.entity.Student;
import com.example.hw8.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestControllerAdvice
@RestController
public class StudentController
{
    //
    @Autowired
    private StudentService studentService;

    //
    @RequestMapping(value="/index")
    public String start()
    {
        return "Hello World! ";
    }
    //
////    read
//    @GetMapping(value="/read/student")
//    public Student readStudent(@RequestParam(value="id")String id)
//    {
//        if(id.length()<=3)
//        {
//            throw new HttpClientErrorException(HttpStatusCode.valueOf(400),"Bad request: id should be at least length of 4. ");
//        }
//        return studentService.readStudent(id);
//    }

    @GetMapping(value="read/student/versiontwo")
    public Student readStudentVersionTwo(@RequestParam(value="id")String id)
    {
        return studentService.readStudentVersionTwo(id);
    }

    @GetMapping(value="/read/all/students")
    public List<Student> readAllStudents()
    {
        return studentService.readAllStudents();
    }

    @GetMapping(value="/count/student/by/name")
    public int countStudentByName(@RequestParam(value="name")String name)
    {
        return studentService.countStudentByName(name);
    }

    @GetMapping(value="/read/student/by/name")
    public List<Student> readStudentByName(@RequestParam(value="name")String name)
    {
        return studentService.readStudentByName(name);
    }

    @GetMapping(value="/read/student/by/name/with/pageable")
    public List<Student> readStudentByNameWithPageable(@RequestParam(value="name")String name,
                                           @RequestParam(value="page")int page,
                                           @RequestParam(value="size")int size)
    {
        return studentService.readStudentByName(name,page,size);
    }

    @GetMapping(value="/get/to/find/student/by/name")
    public List<Student> getToFindStudentByName(@RequestParam(value="name")String name)
    {
        return studentService.getToFindStudentByName(name);
    }

    @GetMapping(value="/go/to/find/student/by/name")
    public List<Student> goToFindStudentByName(@RequestParam(value="name")String name)
    {
        return studentService.goToFindStudentByName(name);
    }
    //create
    @PostMapping(value="/create/student")
    public String createStudent(@RequestBody Student student)
    {
        return studentService.createStudent(student);
    }

    @PostMapping(value="/create/six/students")
    public String createSixStudents()
    {
        return studentService.createSixStudents();
    }

    @PostMapping(value="/student/two")
    public String studentTwo(@RequestParam(value="age")int age,
                             @RequestParam(value="id")String id)
    {
        return "success two! ";
    }

    @PostMapping(value="/send/message")
    public String sendMessage(@RequestBody Student student)
    {
        studentService.sendMessage(student);
        return "sent. ";
    }

    //delete
    @DeleteMapping(value="/delete/student")
    public String deleteStudent(@RequestParam(value="id")String id)
    {
        return studentService.deleteStudent(id);
    }

    @DeleteMapping(value="/delete/all/students")
    public String deleteAllStudents()
    {
        return studentService.deleteAllStudents();
    }

    //update
    @PutMapping(value="/update/student")
    public String updateStudent(@RequestBody Student student)
    {
        return studentService.updateStudent(student);
    }

    //
    //1
    @ExceptionHandler(HttpClientErrorException.class)
    public String handleHttpClientException(HttpClientErrorException e)
    {
        int value=e.getStatusCode().value();
        System.out.println(value);
        switch(value)
        {
            case 400:
                return "Bad request. Error message: "+e.getMessage();
            case 412:
                return "No permission or not authorize to make this call. ";
        }
        return "Sorry, there was an issue no known. ";
    }

    //2
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e)
    {
        System.err.println(e.getMessage());
        return "Sorry there was an issue. "+e.getMessage();
    }
}
