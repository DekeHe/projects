package com.example.hw8.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//import javax.persistence.*;

@Entity
@Table(name="Seat")
public class Seat
{
    //
    @Id @Column(name="id") @Getter @Setter private String id;
    @Column(name="location") @Getter @Setter private String location;


    //1
    @JsonIgnore
    @OneToOne(cascade={CascadeType.ALL},mappedBy="seat")
    @JoinColumn(name="student_id",referencedColumnName="id")
    @Getter @Setter private Student student;

    //
    public Seat()
    {

    }

    public Seat(String id,String location)
    {
        this.id=id;
        this.location=location;
    }

}
