package com.example.hw8.repository;

import com.example.hw8.entity.Seat;
import com.example.hw8.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPASeatRepository extends JpaRepository<Seat,String>
{

}
