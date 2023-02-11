package com.example.hw8.controller;

import com.example.hw8.thread.MyThread;
import com.example.hw8.thread.MyThreadThree;
import com.example.hw8.thread.MyThreadTwo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntertainmentController
{
    @PostMapping(value="/club")
    public String club(@RequestParam(value="age")int age,
                       @RequestParam(value="id")String id)
    {
        return "success! ";
    }

    @PostMapping(value="/club/two")
    public String clubTwo(@RequestParam(value="age")int age,
                          @RequestParam(value="id")String id)
    {
        return "success two! ";
    }

    @GetMapping(value="/thread")
    public String thread()
    {
        MyThread myThread=new MyThread();
        Thread thread1=new Thread(myThread);
        thread1.setName("thread1");
        thread1.start();

        for(int i=0;i<5;i++)
        {
            System.out.println("1:"+Thread.currentThread().getName()+":"+i);
        }


        MyThreadTwo myThreadTwo=new MyThreadTwo();
        Thread thread2=new Thread(myThreadTwo);
        thread2.setName("thread2");
        thread2.start();

        for(int i=0;i<5;i++)
        {
            System.out.println("2:"+Thread.currentThread().getName()+":"+i);
        }

        MyThreadThree myThreadThree=new MyThreadThree();
        Thread thread3=new Thread(myThreadThree);
        thread3.setName("thread3");
        thread3.start();

        for(int i=0;i<5;i++)
        {
            System.out.println("3:"+Thread.currentThread().getName()+":"+i);
        }


        return "thread";
    }
}
