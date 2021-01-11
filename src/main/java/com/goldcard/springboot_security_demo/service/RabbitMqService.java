package com.goldcard.springboot_security_demo.service;


import com.goldcard.springboot_security_demo.pojo.Student;

public interface RabbitMqService {
	// 发送字符消息
    public void sendMsg(String msg);
    
    // 发送用户消息
    public void sendUser(Student student);
}
