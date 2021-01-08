package com.goldcard.springboot_security_demo.controller.advice;

import com.goldcard.springboot_security_demo.exception.NotFoundException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器增加通知
 * @ControllerAdvice 定义一个控制器的通知类，允许定义一些关于增强控制器的各类通知和限定增强哪些控制器功能等
 * @InitBinder 定义控制器参数绑定规则，如转换规则、格式化等，它会在参数转换前执行
 * @ExceptionHandler 定义控制器发送异常后的操作，一般来说，发送异常后，可以跳转到指定的页面
 * @ModelAttribute 可以在控制器方法执行前，对数据模型进行操作
 */
@ControllerAdvice(
        //指定拦截的包
        basePackages = {"com.goldcard.spring_boot_redis_demo.controller.advice.test.*"},
        //限定被标注为@Controller的类才被拦截
        annotations = Controller.class)
public class MyControllerAdvice {

    // 绑定格式化、参数转换规则和增加验证器等
    @InitBinder
    public void initDataBinder(WebDataBinder binder) {
        // 自定义日期编辑器，限定格式为yyyy-MM-dd，且参数不允许为空
        CustomDateEditor dateEditor =
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false);
        // 注册自定义日期编辑器
        binder.registerCustomEditor(Date.class, dateEditor);
    }

    // 在执行控制器之前先执行，可以初始化数据模型
    @ModelAttribute
    public void projectModel(Model model) {
        model.addAttribute("project_name", "chapter10");
    }

    // 异常处理，使得被拦截的控制器方法发生异常时，都能用相同的视图响应
//    @ExceptionHandler(value = Exception.class)
    @ExceptionHandler(value = NotFoundException.class) //指向自定义异常
    @ResponseBody
    //定义为服务器错误状态
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,Object> exception(HttpServletRequest request, NotFoundException ex) {
        Map<String,Object> result = new HashMap<>();
        result.put("code",ex.getCode());
        result.put("message",ex.getCustomMsg());
        return result;
    }

}
