package com.goldcard.springboot_security_demo.pojo;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Alias("stu")
public class Student implements Serializable {
    private Long id;

    private String studentName;

    private String note;

    public Student() {
    }

    public Student(Long id, String studentName, String note) {
        this.id = id;
        this.studentName = studentName;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
