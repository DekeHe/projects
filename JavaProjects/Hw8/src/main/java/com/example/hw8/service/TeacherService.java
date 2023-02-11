package com.example.hw8.service;

import com.example.hw8.entity.Teacher;
import com.example.hw8.information.Resource;
import com.example.hw8.repository.JPATeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService
{
    //
    @Autowired
    private JPATeacherRepository jpaTeacherRepository;

    //
    //read
    public Teacher readTeacher(String id)
    {
        return jpaTeacherRepository.findById(id).orElse(null);
    }

    //
    public List<Teacher> readAllTeachers()
    {
        return jpaTeacherRepository.findAll();
    }

    //create
    public String createTeacher(Teacher teacher)
    {
        if(jpaTeacherRepository.findById(teacher.getId()).orElse(null)==null)
        {
            jpaTeacherRepository.save(teacher);
            return "created. ";
        }
        else
        {
            return "already exists, cannot create! ";
        }

    }

    public String createSixTeachers()
    {
        jpaTeacherRepository.saveAll(Resource.teacherList);
        return "created. ";
    }


    //delete
    public String deleteTeacher(String id)
    {
        jpaTeacherRepository.deleteById(id);
        return "deleted. ";
    }

    public String deleteAllTeachers()
    {
        jpaTeacherRepository.deleteAll();
        return "deleted. ";
    }

    //update
    public String updateTeacher(Teacher teacher)
    {
        if(jpaTeacherRepository.findById(teacher.getId()).orElse(null)!=null)
        {
            jpaTeacherRepository.save(teacher);
            return "updated. ";
        }
        else
        {
            return "not exists, cannot update! ";
        }
    }
}
