package com.goldcard.springboot_security_demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/file")
public class FileController {

    /**
     * 使用 HttpServletRequest 作为参数
     *
     * @param request
     * @return
     */
    @RequestMapping("/upload/request")
    @ResponseBody
    public Map<String, Object> uploadRequest(HttpServletRequest request) {
        MultipartHttpServletRequest mreq;
        //强制转换为 MultipartHttpServletRequest 接口对象
        if (request instanceof MultipartHttpServletRequest) {
            mreq = (MultipartHttpServletRequest) request;
        } else {
            return resultMap(false, "上传失败");
        }
        MultipartFile mf = mreq.getFile("file");
        //获取源文件名称
        String filename = mf.getOriginalFilename();
        File file = new File(filename);
        //保存文件
        try {
            mf.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            return resultMap(false, "上传失败");
        }
        return resultMap(true, "上传成功");
    }

    /**
     * 使用Spring MVC的 MultipartFile 作为参数
     *
     * @param file
     * @return
     */
    @RequestMapping("/upload/multipart")
    @ResponseBody
    public Map<String, Object> uploadRequest(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        File dest = new File(fileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return resultMap(false, "上传失败");
        }
        return resultMap(true, "上传成功");
    }

    /**
     * 推荐使用
     *
     * @param file
     * @return
     */
    @RequestMapping("/upload/part")
    @ResponseBody
    public Map<String, Object> uploadRequest(Part file) {
        //获取提交文件名称
        String fileName = file.getSubmittedFileName();
        try {
            file.write(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return resultMap(false, "上传失败");
        }
        return resultMap(true, "上传成功");
    }

    private Map<String, Object> resultMap(boolean success, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("meaasge", message);
        return result;
    }
}
