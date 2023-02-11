package com.example.hw8.service;

import com.example.hw8.repository.JPASeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatService
{
    @Autowired
    private JPASeatRepository jpaSeatRepository;
}
