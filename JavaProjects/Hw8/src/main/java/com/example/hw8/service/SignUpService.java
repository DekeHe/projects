package com.example.hw8.service;

import com.example.hw8.entity.Card;
import com.example.hw8.entity.Seat;
import com.example.hw8.entity.Student;
import com.example.hw8.entity.Teacher;
import com.example.hw8.information.Resource;
import com.example.hw8.repository.JPACardRepository;
import com.example.hw8.repository.JPASeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SignUpService
{
    //
    @Autowired
    private JPASeatRepository jpaSeatRepository;
    @Autowired
    private JPACardRepository jpaCardRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    //
    //create
    public String createAllThings()
    {
        jpaSeatRepository.saveAll(Resource.seatList);
        jpaCardRepository.saveAll(Resource.cardList);
        studentService.createSixStudents();
        teacherService.createSixTeachers();
        return "created. ";
    }

    //read
    public List<Seat> readAllSeats()
    {
        return jpaSeatRepository.findAll();
    }

    public List<Card> readAllCards()
    {
        return jpaCardRepository.findAll();
    }

    //delete
    public String deleteAllThings()
    {
        jpaSeatRepository.deleteAll();
        jpaCardRepository.deleteAll();
        studentService.deleteAllStudents();
        teacherService.deleteAllTeachers();
        return "deleted. ";
    }

    public String deleteSeat(String id)
    {
        jpaSeatRepository.deleteById(id);
        return "deleted. ";
    }

    public String deleteCard(String id)
    {
        jpaCardRepository.deleteById(id);
        return "deleted. ";
    }

    //update
    public String signUpStudentSeat(String studentId,String seatId)
    {
        Student student=studentService.readStudent(studentId);
        Seat seat=jpaSeatRepository.getReferenceById(seatId);

        student.setSeat(seat);
        studentService.updateStudent(student);

        seat.setStudent(student);
        jpaSeatRepository.save(seat);
        return "signed up. ";
    }

    public String signUpCardStudent(String cardId,String studentId)
    {
        Card card=jpaCardRepository.getReferenceById(cardId);
        Student student=studentService.readStudent(studentId);

        Set<Card> cards=student.getCards();
        cards.add(card);
        student.setCards(cards);
        studentService.updateStudent(student);

        card.setStudent(student);
        jpaCardRepository.save(card);
        return "signed up. ";

    }

    public String signUpStudentTeacher(String studentId,String teacherId)
    {
        Student student=studentService.readStudent(studentId);
        Teacher teacher=teacherService.readTeacher(teacherId);

        Set<Teacher> teachers=student.getTeachers();
        teachers.add(teacher);
        teacherService.updateTeacher(teacher);

        Set<Student> students=teacher.getStudents();
        students.add(student);
        studentService.updateStudent(student);
        return "signed up. ";
    }

}
