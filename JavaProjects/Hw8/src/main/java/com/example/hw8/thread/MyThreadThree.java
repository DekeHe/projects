package com.example.hw8.thread;

public class MyThreadThree implements Runnable
{
    public static Object object=new Object();
    public void run()
    {
        synchronized(object)
        {
            for(int i=0;i<5;i++)
            {
                System.out.println(Thread.currentThread().getName()+":"+i);
            }
        }
    }
}
