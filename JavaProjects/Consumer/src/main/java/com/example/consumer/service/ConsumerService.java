package com.example.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService
{
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @KafkaListener(topics="topicId")
    public void getId(String message)
    {
        System.out.println("now id:"+message);
    }

    @KafkaListener(topics="topicName")
    public void getName(String message)
    {
        System.out.println("now name:"+message);
    }
}
