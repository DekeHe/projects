package com.example.hw8.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//import javax.persistence.*;

@Entity
@Table(name="Card")
public class Card
{
    //
    @Id @Column(name="id") @Getter @Setter private String id;
    @Column(name="name") @Getter @Setter private String name;
    @Column(name="number") @Getter @Setter private String number;


    //1
//    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name="student_id",referencedColumnName="id")
    @Getter
    @Setter
    private Student student;

    //
    public Card()
    {

    }

    public Card(String id,String name,String number)
    {
        this.id=id;
        this.name=name;
        this.number=number;
    }


}
