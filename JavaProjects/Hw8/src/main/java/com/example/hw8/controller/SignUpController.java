package com.example.hw8.controller;

import com.example.hw8.entity.Card;
import com.example.hw8.entity.Seat;
import com.example.hw8.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SignUpController
{
    //
    @Autowired
    private SignUpService signUpService;

    //
    //create
    @PostMapping(value="/create/all/things")
    public String createAllThings()
    {
        return signUpService.createAllThings();
    }

    //read
    @GetMapping(value="/read/all/seats")
    public List<Seat> readAllSeats()
    {
        return signUpService.readAllSeats();
    }

    @GetMapping(value="/read/all/cards")
    public List<Card> readAllCards()
    {
        return signUpService.readAllCards();
    }

    //delete
    @DeleteMapping(value="/delete/all/things")
    public String deleteAll()
    {
        return signUpService.deleteAllThings();
    }

    @DeleteMapping(value="/delete/seat")
    public String deleteSeat(@RequestParam(value="id")String id)
    {
        return signUpService.deleteSeat(id);
    }

    @DeleteMapping(value="/delete/card")
    public String deleteCard(@RequestParam(value="id")String id)
    {
        return signUpService.deleteCard(id);
    }

    //update
    @PutMapping(value="/signup/student/seat")
    public String signUpStudentSeat(@RequestParam(value="studentId")String studentId,
                                    @RequestParam(value="seatId")String seatId)
    {
        return signUpService.signUpStudentSeat(studentId,seatId);
    }

    @PutMapping(value="/signup/card/student")
    public String signUpCardStudent(@RequestParam(value="cardId")String cardId,
                                    @RequestParam(value="studentId")String studentId)
    {
        return signUpService.signUpCardStudent(cardId,studentId);
    }

    @PutMapping(value="/signup/student/teacher")
    public String signUpStudentTeacher(@RequestParam(value="studentId")String studentId,
                                       @RequestParam(value="teacherId")String teacherId)
    {
        return signUpService.signUpStudentTeacher(studentId,teacherId);
    }
}
