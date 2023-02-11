package com.example.hw8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableCaching
@EnableEurekaServer
public class Hw8Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Hw8Application.class, args);
    }

}
