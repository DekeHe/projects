package com.example.hw8.repository;

import com.example.hw8.entity.Student;
import com.example.hw8.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface JPATeacherRepository extends JpaRepository<Teacher,String>
{

}
