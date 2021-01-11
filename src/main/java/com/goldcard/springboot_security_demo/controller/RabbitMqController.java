package com.goldcard.springboot_security_demo.controller;

import com.goldcard.springboot_security_demo.pojo.Student;
import com.goldcard.springboot_security_demo.service.RabbitMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rabbitmq")
public class RabbitMqController {
    // 注入Spring Boot自定生成的对象
    @Autowired
    private RabbitMqService rabbitMqService = null;
    
    @GetMapping("/msg") // 字符串
    public Map<String, Object> msg(String message) {
        rabbitMqService.sendMsg(message);
        return resultMap("message", message);
    }
    
    @GetMapping("/user") // 用户
    public Map<String, Object> user(Long id, String userName, String note) {
        Student student = new Student(id, userName, note);
        rabbitMqService.sendUser(student);
        return resultMap("student", student);
    }
    // 结果Map
    private Map<String, Object> resultMap(String key, Object obj) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put(key, obj);
        return result;
    }
}
