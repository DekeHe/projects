package com.example.hw8.cacheAOP;

import com.example.hw8.entity.Student;
import com.example.hw8.entity.Teacher;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Aspect
@Component
public class StudentServiceCache
{
    @Autowired
    private ConcurrentMapCacheManager concurrentMapCacheManager;

    @Pointcut(value="execution(public * com.example.hw8.service.StudentService.createStudent(..))")
    public void aa()
    {

    }

    @Before(value="aa()")
    public void f(JoinPoint joinPoint)
    {
        System.out.println("aaaa"+joinPoint.getArgs().length+"\n");
        System.out.println("\naaaa");
        Student student=new Student();

        for(Object object:joinPoint.getArgs())
        {
            if(object instanceof Student)
            {
                student=(Student)object;
                String id=student.getId();
                Cache cache=concurrentMapCacheManager.getCache("student");
                if(cache!=null)
                {
                    cache.put(id,student);
                    System.out.print("\nbbbbb\n");
                }
            }
        }
    }

//    @AfterReturning(value="aa()")
//    public void f2(JoinPoint joinPoint)
//    {
//        System.out.println("cccc"+joinPoint.getArgs().length+"\n");
//        System.out.println("cccc"+"\n");
//    }
//
//    @After(value="aa()")
//    public void f3(JoinPoint joinPoint)
//    {
//        System.out.println("dddd"+joinPoint.getArgs().length+"\n");
//        System.out.println("dddd"+"\n");
//    }
}
