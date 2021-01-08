package com.goldcard.springboot_security_demo.service;


import com.goldcard.springboot_security_demo.pojo.Student;

import java.util.List;

public interface StudentService {
    Student getStudent(Long id);

    Student insertStudent(Student student);

    Student updateStudent(Long id, String name);

    //查询学生信息，指定Mybatis的参数名称
    List<Student> findStudents(String studentName, String note);

    int deleteStudent(Long id);
}
