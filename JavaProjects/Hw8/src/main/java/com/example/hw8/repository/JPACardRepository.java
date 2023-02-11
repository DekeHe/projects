package com.example.hw8.repository;

import com.example.hw8.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPACardRepository extends JpaRepository<Card,String>
{

}
