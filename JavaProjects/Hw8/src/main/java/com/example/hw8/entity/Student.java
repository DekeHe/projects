package com.example.hw8.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="Student")
@ToString
public class Student
{
    //
    @Id @Column(name="id") @Getter @Setter private String id;
    @Column(name="name") @Getter @Setter private String name;
    @Column(name="age") @Getter @Setter private int age;
    @Column(name="address") @Getter @Setter private String address;
    @Column(name="email") @Getter @Setter private String email;
    @Column(name="phoneNumber") @Getter @Setter private String phoneNumber;


//    @JsonIgnore
    @OneToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="seat_id",referencedColumnName="id")
    @Getter @Setter private Seat seat;

    //
//    @JsonIgnore
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="cards",referencedColumnName="id")
    @Getter @Setter private Set<Card> cards;


    //
//    @JsonIgnore
    @ManyToMany()
    @JoinTable( name="Teacher_Student",
                joinColumns={@JoinColumn(name="teacher_id",referencedColumnName="id")},
                inverseJoinColumns={@JoinColumn(name="student_id",referencedColumnName="id")})
    @Getter @Setter private Set<Teacher> teachers;
    //
    public Student()
    {

    }

    public Student(String id,String name,int age,String address,String email,String phoneNumber)
    {
        this.id=id;
        this.name=name;
        this.age=age;
        this.address=address;
        this.email=email;
        this.phoneNumber=phoneNumber;
    }




}
