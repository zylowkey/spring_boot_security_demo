package com.goldcard.springboot_security_demo.controller;

import com.goldcard.springboot_security_demo.pojo.Student;
import com.goldcard.springboot_security_demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController 使用 @RestController，可以让控制器都默认转换为JSON数据集 ，此时就不需要  @ResponseBody
@Controller
@RequestMapping("/stu")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @RequestMapping("/getStu")
    @ResponseBody
    public Student getStudent(Long id){
        return studentService.getStudent(id);
    }

    /**
     * 在无注解下获取参数，要求参数名称和HTTP请求参数名称一致，参数允许为空
     * @param name
     * @param note
     * @return
     */
    @RequestMapping("/insertStu")
    @ResponseBody
    public Student insertStudent(String name,String note){
        Student student = new Student();
        student.setStudentName(name);
        student.setNote(note);
        return studentService.insertStudent(student);
    }

    /**
     * 通过注解@RequestParam获取参数，默认情况下参数不能为空
     * 如要设置参数为空 @RequestParam(value = "student_name",required = false) String name
     * @param name
     * @param note
     * @return
     */
    @RequestMapping("/insertStu2")
    @ResponseBody
    public Student insertStudent2(@RequestParam("student_name") String name, @RequestParam("mark") String note){
        Student student = new Student();
        student.setStudentName(name);
        student.setNote(note);
        return studentService.insertStudent(student);
    }

    @RequestMapping("/findStu")
    @ResponseBody
    public List<Student> findStudents(String name,String note){
        return studentService.findStudents(name,note);
    }

    @RequestMapping("/updateStu")
    @ResponseBody
    public Map<String,Object> updateStudentName(Long id,String name){
        Student student = studentService.updateStudent(id,name);
        boolean flag = student != null;
        String message = flag?"更新成功":"更新失败";
        return resultMap(flag,message);
    }

    @RequestMapping("/deleteStu")
    @ResponseBody
    public Map<String,Object> deleteStudent(Long id){
        int result = studentService.deleteStudent(id);
        boolean flag = result == 1;
        String message = flag?"删除成功":"删除失败";
        return resultMap(flag,message);
    }

    /**
     * @RequestBody 接受前端传入的JSON请求体，JSON请求体与Student之间的属性名称对应
     * @param student
     */
    @RequestMapping("/mvc")
    @ResponseBody
    public void mvcTest(@RequestBody Student student){

    }

    /**
     * 通过URL传递参数
     * {...}代表占位符，可以配置参数名称
     * @PathVariable("id")通过名称获取RUL配置的参数
     * @param id
     * @ResponseBody  响应为JSON数据集  通过MappingJackson2HttpMessageConverter将数据转换为JSON数据集
     */
    @RequestMapping("/mvc/{id}")
    @ResponseBody
    public void mvcTest1(@PathVariable("id") Long id){

    }

    /**
     * 参数格式化日期格式化和数字格式化
     * @DateTimeFormat 日期格式化
     * @NumberFormat 数字格式化
     * @param date
     * @param number
     */
    @RequestMapping("/form")
    @ResponseBody
    public void mvcFormat(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date, @NumberFormat(pattern = "#,###.##")Double number){

    }

    @RequestMapping("/convert")
    @ResponseBody
    public Student convert(Student student){
        return student;
    }

    /**
     *
     * @param studentList   1-user_name_1-note_1,2-user_name_2-note_2,3-user_name_3-note_3
     * @return
     * 请求参数传入字符串时，默认使用StringToCollectionConverter转换器，这个类实现了GenericConverter接口，并且是SprinMVC内部已经注册的数组转换器
     * 首先把字符串用逗号分隔为一个个的子字符串，然后根据原类型泛型为String，目前类型泛型为Student，找到对应的Converter进行转换，将子字符串转为Student对象。
     * 这里就用到了自定义的 StringToUserConverter 将字符串转为Student对象。这样控制器就能获得List<Student>的参数
     * */
    @RequestMapping("/list")
    @ResponseBody
    public List<Student> convert(List<Student> studentList){
        return studentList;
    }
    private Map<String,Object> resultMap(boolean success,String message){
        Map<String,Object> result = new HashMap<>();
        result.put("success",success);
        result.put("meaasge",message);
        return result;
    }

    /**
     *  获取请求头参数
     *  @RequestHeader 获取请求头信息
     * @param id
     */
    @RequestMapping("/header")
    public void headerParam(@RequestHeader("id") Long id){
        System.out.println("======="+id);
    }
}
