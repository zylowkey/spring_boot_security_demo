package com.goldcard.springboot_security_demo.repository.impl;

import com.goldcard.springboot_security_demo.mapper.StudentMapper;
import com.goldcard.springboot_security_demo.pojo.Student;
import com.goldcard.springboot_security_demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepositoryImpl implements StudentRepository {
    @Autowired
    private StudentMapper studentMapper;
    @Override
    public Student getStudent(Long id) {
        return studentMapper.getStudent(id);
    }

    @Override
    public int insertStudent(Student student) {
        return studentMapper.insertStudent(student);
    }

    @Override
    public int updateStudent(Student student) {
        return studentMapper.updateStudent(student);
    }

    @Override
    public List<Student> findStudents(String studentName, String note) {
        return studentMapper.findStudents(studentName,note);
    }

    @Override
    public int deleteStudent(Long id) {
        return studentMapper.deleteStudent(id);
    }
}
