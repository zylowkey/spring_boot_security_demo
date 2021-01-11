package com.goldcard.springboot_security_demo.service.impl;

import com.goldcard.springboot_security_demo.service.AsyncService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncServiceImpl implements AsyncService {
    @Override
    //声明使用异步调用
    @Async
    public void generateReport() {
        System.out.println("业务线程名称:"+"【"+Thread.currentThread().getName()+"】");
    }
}
