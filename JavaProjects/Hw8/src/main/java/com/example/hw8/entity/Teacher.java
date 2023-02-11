package com.example.hw8.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="Teacher")
public class Teacher
{
    //
    @Id  @Column(name="id") @Getter @Setter private String id;
    @Column(name="name") @Getter @Setter private String name;
    @Column(name="age") @Getter @Setter private int age;
    @Column(name="address") @Getter @Setter private String address;
    @Column(name="email") @Getter @Setter private String email;
    @Column(name="phoneNumber") @Getter @Setter private String phoneNumber;


    //
    @JsonIgnore
    @ManyToMany()
    @JoinTable(name="Teacher_Student",
    joinColumns={@JoinColumn(name="student_id",referencedColumnName="id"),},
    inverseJoinColumns={@JoinColumn(name="teacher_id",referencedColumnName="id")})
    @Getter @Setter private Set<Student> students;
    //
    public Teacher()
    {

    }

    public Teacher(String id,String name,int age,String address,String email,String phoneNumber)
    {
        this.id=id;
        this.name=name;
        this.age=age;
        this.address=address;
        this.email=email;
        this.phoneNumber=phoneNumber;
    }



}
