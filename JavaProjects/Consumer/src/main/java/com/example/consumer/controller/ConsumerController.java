package com.example.consumer.controller;

import com.example.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController
{
    //
    @Autowired
    private ConsumerService consumerService;

    //
    @GetMapping(value="/get/id")
    public void getId()
    {
        consumerService.getId("a");
    }
}
