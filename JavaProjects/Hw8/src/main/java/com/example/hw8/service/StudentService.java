package com.example.hw8.service;

import com.example.hw8.entity.Student;
import com.example.hw8.information.Resource;
import com.example.hw8.repository.JPAStudentRepository;
import org.hibernate.annotations.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.hw8.information.Resource.studentList;

@Service
public class StudentService
{

    //
    @Autowired
    private JPAStudentRepository jpaStudentRepository;

    //
    private Logger logger= LoggerFactory.getLogger("StudentService");

    //
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    //
    //read
    @Cacheable(cacheNames={"student"})
    public Student readStudent(String id)
    {
        return jpaStudentRepository.findById(id).orElse(null);
    }

    @Cacheable(cacheNames={"student"})
    public Student readStudentVersionTwo(String id)
    {
        return jpaStudentRepository.getReferenceById(id);
    }

    @CachePut(cacheNames={"students"})
    public List<Student> readAllStudents()
    {
        logger.info("here is info. ");
        logger.warn("here is warn. ");
        logger.error("here is error. ");
        logger.debug("here is debug. ");
        return jpaStudentRepository.findAll();
    }

    @CachePut(cacheNames={"countingOfStudentByName"})
    public int countStudentByName(String name)
    {
        return jpaStudentRepository.countStudentByName(name);
    }

    @CachePut(cacheNames={"studentsByName"})
    public List<Student> readStudentByName(String name)
    {
        return jpaStudentRepository.readStudentByName(name);
    }

    @CachePut(cacheNames={"studentsByNameWithPageable"})
    public List<Student> readStudentByName(String name,int page,int size)
    {
        Pageable pageable=PageRequest.of(page,size, Sort.by("address"));
        return jpaStudentRepository.readStudentByName(name,pageable);

    }

    @CachePut(cacheNames={"studensByName"})
    public List<Student> getToFindStudentByName(String name)
    {
        return jpaStudentRepository.getToFindStudentByName(name);
    }

    @CachePut(cacheNames={"studentsByName"})
    public List<Student> goToFindStudentByName(String name)
    {
        return jpaStudentRepository.goToFindStudentByName(name);
    }


    //create
    @Cacheable(cacheNames={"student"})
    public String createStudent(Student student)
    {
        if(jpaStudentRepository.findById(student.getId()).orElse(null)==null)
        {
            jpaStudentRepository.save(student);
            return "saved. ";
        }
        else
        {
            return "already exists, cannot create! ";
        }

    }

    @Cacheable(cacheNames={"student"})
    public String createSixStudents()
    {
        jpaStudentRepository.saveAll(Resource.studentList);
        return "created. ";
    }

    public void sendMessage(Student student)
    {
        String id=student.getId();
        String name=student.getName();
        kafkaTemplate.send("topicId",id);
        kafkaTemplate.send("topicName",name);
    }

    //delete
    @CacheEvict(cacheNames={"student"},key="#id")
    public String deleteStudent(String id)
    {

        jpaStudentRepository.deleteById(id);
        return "deleted. ";
    }

    @CacheEvict(cacheNames={"students"})
    public String deleteAllStudents()
    {
        jpaStudentRepository.deleteAll();
        return "deleted. ";
    }

    //update
    @CachePut(cacheNames={"student"},key="#student.id")
    public String updateStudent(Student student)
    {
        if(jpaStudentRepository.findById(student.getId()).orElse(null)!=null)
        {
            jpaStudentRepository.save(student);
            return "updated. ";
        }
        else
        {
            return "not exists, cannot update! ";
        }
    }
}
