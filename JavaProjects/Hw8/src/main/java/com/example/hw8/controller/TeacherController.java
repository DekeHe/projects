package com.example.hw8.controller;

import com.example.hw8.entity.Teacher;
import com.example.hw8.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeacherController
{
    //
    @Autowired
    private TeacherService teacherService;

    //
    //read
    @GetMapping(value="/read/teacher")
    public Teacher readTeacher(@RequestParam(value="id")String id)
    {
        return this.teacherService.readTeacher(id);
    }

    @GetMapping(value="read/all/teachers")
    public List<Teacher> readAllTeachers()
    {
        return this.teacherService.readAllTeachers();
    }

    //create
    @PostMapping(value="/create/teacher")
    public String createTeacher(@RequestBody Teacher teacher)
    {
        return this.teacherService.createTeacher(teacher);
    }

    @PostMapping(value="/create/six/teachers")
    public String createSixTeachers()
    {
        return this.teacherService.createSixTeachers();
    }

    //delete
    @DeleteMapping(value="/delete/teacher")
    public String deleteTeacher(@RequestParam(value="id")String id)
    {
        return this.teacherService.deleteTeacher(id);
    }

    @DeleteMapping(value="/delete/all/teachers")
    public String deleteAllTeacher()
    {
        return this.teacherService.deleteAllTeachers();
    }

    //update
    @PutMapping(value="/update/teacher")
    public String updateTeacher(@RequestBody Teacher teacher)
    {
        return this.teacherService.updateTeacher(teacher);
    }
}
