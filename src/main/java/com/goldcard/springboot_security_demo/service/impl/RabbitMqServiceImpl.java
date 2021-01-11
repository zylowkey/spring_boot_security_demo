package com.goldcard.springboot_security_demo.service.impl;

import com.goldcard.springboot_security_demo.pojo.Student;
import com.goldcard.springboot_security_demo.service.RabbitMqService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 实现  ConfirmCallback 接口。可以用来回调
 */
@Service
public class RabbitMqServiceImpl implements RabbitTemplate.ConfirmCallback, RabbitMqService {

    @Value("${rabbitmq.queue.msg}")
    private String msgRouting = null;

    @Value("${rabbitmq.queue.user}")
    private String userRouting = null;

    // 注入由Spring Boot自动配置的RabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate = null;

    /**
     * 发送字符串消息
     *
     * @param msg
     */
    @Override
    public void sendMsg(String msg) {
        System.out.println("发送消息: 【" + msg + "】");
        // 设置回调
        rabbitTemplate.setConfirmCallback(this);
        // 发送消息，通过msgRouting确定队列
        rabbitTemplate.convertAndSend(msgRouting, msg);
    }

    /**
     * 发送对象
     *
     * @param student
     */
    @Override
    public void sendUser(Student student) {
        System.out.println("发送用户消息: 【" + student + "】");
        // 设置回调
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.convertAndSend(userRouting, student);
    }

    /**
     * 回调确认方法
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println("消息成功消费");
        } else {
            System.out.println("消息消费失败:" + cause);
        }
    }
}
