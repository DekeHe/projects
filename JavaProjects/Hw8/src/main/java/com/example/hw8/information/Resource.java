package com.example.hw8.information;

import com.example.hw8.entity.Card;
import com.example.hw8.entity.Seat;
import com.example.hw8.entity.Student;
import com.example.hw8.entity.Teacher;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Resource
{
    public final static List<Student> studentList=new LinkedList<>(Arrays.asList(
            new Student("00s1","Aaron",111,"addressAaron1",
                    "Aaron@gmail.com","6311234561"),
            new Student("00s2","Bob",11,"addressAaron",
                    "Aaron@gmail.com","6311234561"),
            new Student("00s3","Aaron",11,"addressAaron3",
                    "Aaron@gmail.com","6311234561"),
            new Student("00s4","Davlid",11,"addressAaron",
                    "Aaron@gmail.com","6311234561"),
            new Student("00s5","Ethan",11,"addressAaron",
                    "Aaron@gmail.com","6311234561"),
            new Student("00s6","Frank",11,"addressAaron",
                    "Aaron@gmail.com","6311234561")
    ));

    public final static List<Teacher> teacherList=new LinkedList<>(Arrays.asList(
            new Teacher("00t1","Grey",77,"addressGrey",
                    "Grey@gmail.com","6311234507"),
            new Teacher("00t2","Hayden",77,"addressGrey",
                    "Grey@gmail.com","6311234507"),
            new Teacher("00t3","Ivan",77,"addressGrey",
                    "Grey@gmail.com","6311234507"),
            new Teacher("00t4","Jane",77,"addressGrey",
                    "Grey@gmail.com","6311234507"),
            new Teacher("00t5","Kelly",77,"addressGrey",
                    "Grey@gmail.com","6311234507"),
            new Teacher("00t6","Lambert",77,"addressGrey",
                    "Grey@gmail.com","6311234507")
    ));

    public final static List<Card> cardList=new LinkedList<>(Arrays.asList(
            new Card("00c1","bank","1231"),
            new Card("00c2","restaurant","1232"),
            new Card("00c3","school","1233")
    ));

    public final static List<Seat> seatList=new LinkedList<>(Arrays.asList(
            new Seat("00e1","A1"),
            new Seat("00e2","A2"),
            new Seat("00e3","A3")
    ));
}
