package com.goldcard.springboot_security_demo.service.impl;

import com.goldcard.springboot_security_demo.pojo.Student;
import com.goldcard.springboot_security_demo.repository.StudentRepository;
import com.goldcard.springboot_security_demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @CachePut表示将方法结果返回存放到缓存中
 * @Cacheable表示先从缓存中通过定义的键查询，如果查到数据，就返回，否则执行该方法，返回数据，并且将返回结果保存到缓存中
 * @CacheEvict表示通过定义的键删除缓存，它有个Boolean类型的配置项beforeInvocation，表示在方法之前或者方法之后移除缓存，默认值为false，表示默认在方法之后移除缓存 #id 代表参数，它是通过参数名称来匹配
 * #a[0]或者#p[0] 表示第一个参数
 * #result 表示返回的结果对象
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    /**
     * 获取id，取参数id缓存用户
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    @Cacheable(value = "redisCache", key = "'redis_user_'+#id")
    public Student getStudent(Long id) {
        return studentRepository.getStudent(id);
    }


    /**
     * 插入用户，最后Mybatis会回填id,取结果id缓存用户
     *
     * @param student
     * @return
     */
    @Override
    @Transactional
    @CachePut(value = "redisCache", key = "'redis_user_'+#result.id")
    public Student insertStudent(Student student) {
        studentRepository.insertStudent(student);
        return student;
    }

    /**
     * 更新数据后，更新缓存，如果condition配置项结果返回null，不缓存
     *
     * @param id
     * @param name
     * @return
     */
    @Override
    @Transactional
    @CachePut(value = "redisCache", condition = "#result != null", key = "'redis_user_'+#id")
    public Student updateStudent(Long id, String name) {
        Student student = this.getStudent(id);
        if (null == student) {
            return student;
        }
        student.setStudentName(name);
        studentRepository.updateStudent(student);
        return student;
    }

    /**
     * 命中率低，不采用缓存
     *
     * @param studentName
     * @param note
     * @return
     */
    @Override
    @Transactional
    public List<Student> findStudents(String studentName, String note) {
        return studentRepository.findStudents(studentName, note);
    }

    /**
     * 删除缓存
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    @CacheEvict(value = "redisCache", key = "'redis_user_'+#id", beforeInvocation = false)
    public int deleteStudent(Long id) {
        return studentRepository.deleteStudent(id);
    }

    public static void main(String[] args) {
        Student s = new Student();
        s.setStudentName("222");
        s = Optional.ofNullable(s).orElse(creatStudent());
        System.out.println(s.getStudentName());
        s = Optional.ofNullable(s).orElseGet(() -> creatStudent());
        System.out.println(s.getStudentName());
    }

    public static Student creatStudent() {
        Student student = new Student();
        student.setStudentName("111");
        return student;
    }
}
