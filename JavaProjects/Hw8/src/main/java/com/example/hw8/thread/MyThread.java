package com.example.hw8.thread;

public class MyThread extends Thread
{
    public void run()
    {
        for(int i=0;i<5;i++)
        {
            System.out.println(Thread.currentThread().getName()+":"+i);
        }
    }
}
