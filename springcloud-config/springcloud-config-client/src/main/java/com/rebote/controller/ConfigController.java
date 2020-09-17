package com.rebote.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: springcloud-parent
 * @Package: com.rebote.controller
 * @ClassName: ConfigController
 * @Author: lmluo
 * @Description: 测试
 * @Date: 2020/9/17 15:16
 * @Version: 1.0
 */
@RestController
@RequestMapping("api")
public class ConfigController {

    @Value("${abc}")
    private String value;

    @GetMapping("/home")
    public String home(){
        return "hello,world"+value;
    }

}
