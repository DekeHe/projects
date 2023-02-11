package com.example.hw8.cacheAOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect
{
    @Pointcut("execution(* com.example.hw8.service.StudentService.*(..))")
    public void aa()
    {

    }

    @Before(value="aa()")
    public void before(JoinPoint joinPoint)
    {
        System.out.println("before...");

        Signature signature=joinPoint.getSignature();
        System.out.println("signature.getName():"+signature.getName()+". ");

        for(Object object:joinPoint.getArgs())
        {
            System.out.println("object.toString():"+object.toString());
        }
    }

    @After(value="aa()")
    public void after(JoinPoint joinPoint)
    {
        System.out.println("after..., all set. ");
    }
}
