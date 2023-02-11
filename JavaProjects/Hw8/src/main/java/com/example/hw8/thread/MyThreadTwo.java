package com.example.hw8.thread;

public class MyThreadTwo implements Runnable
{
    public void run()
    {
        for(int i=0;i<5;i++)
        {
            System.out.println(Thread.currentThread().getName()+":"+i);
        }
    }
}
