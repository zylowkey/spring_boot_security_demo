package com.goldcard.springboot_security_demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 设置映射关系
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 使得"/login/page"映射为"login.jsp"
        registry.addViewController("/login/page").setViewName("login");
        // 使得"/logout/page"映射为"logout_welcome.jsp"
        registry.addViewController("/logout/page").setViewName("logout_welcome");
        // 使得"/logout"映射为"logout.jsp"
        registry.addViewController("/logout").setViewName("logout");
    }
}
