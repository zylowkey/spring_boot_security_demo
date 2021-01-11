package com.goldcard.springboot_security_demo.controller;

import com.goldcard.springboot_security_demo.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("async")
public class AsyncController {
    @Autowired
    private AsyncService asyncService;

    @RequestMapping("/page")
    public String asyncPage(){
        System.out.println("请求线程名称:"+"【"+Thread.currentThread().getName()+"】");
        //调用异步服务
        asyncService.generateReport();
        return "async";
    }
}
