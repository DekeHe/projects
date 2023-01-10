package com.yjq.programmer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 */
@SpringBootApplication
@MapperScan("com.yjq.programmer.dao")
public class HospitalManageSystem
{
    public static void main( String[] args )
    {
        String tomcatHome = System.getenv("NEXUS_USER");
        System.out.println("TOMCAT_HOME: "+tomcatHome);

        SpringApplication.run(HospitalManageSystem.class, args);
    }
}
