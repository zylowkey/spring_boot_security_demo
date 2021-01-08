package com.goldcard.springboot_security_demo.converter;

import com.goldcard.springboot_security_demo.pojo.Student;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * 控制器参数转换
 * 自定义字符串-实体类转换
 * Converter 是一对一转换器，也就是从一种类型转换为另外一种类型
 * GenericConverter 数组转换器
 */
@Component
public class StringToUserConverter implements Converter<String, Student> {
    @Override
    public Student convert(String s) {
        Student student = new Student();
        String []strArr = s.split("-");
        Long id = Long.parseLong(strArr[0]);
        String name = strArr[1];
        String note = strArr[2];
        student.setId(id);
        student.setStudentName(name);
        student.setNote(note);
        return student;
    }
}
