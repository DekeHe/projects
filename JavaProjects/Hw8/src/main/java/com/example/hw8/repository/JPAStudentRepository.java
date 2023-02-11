package com.example.hw8.repository;

import com.example.hw8.entity.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface JPAStudentRepository extends JpaRepository<Student,String>
{
    Student getReferenceById(String id);

    int countStudentByName(String name);

    List<Student> readStudentByName(String name);

    @Query(value="select s from Student s where s.name=?1")
    List<Student> getToFindStudentByName(String name);

    @Query(value="select Student.* from Student where name=:name",nativeQuery=true)
    List<Student> goToFindStudentByName(@Param(value="name") String nameadsafsdf);

    //
    List<Student> readStudentByName(String name, Pageable pageable);
}
