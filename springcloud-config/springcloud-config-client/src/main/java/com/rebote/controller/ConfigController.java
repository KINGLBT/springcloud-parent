package com.rebote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ProjectName: springcloud-parent
 * @Package: com.rebote.controller
 * @ClassName: ConfigController
 * @Author: lmluo
 * @Description: 测试
 * @Date: 2020/9/17 15:16
 * @Version: 1.0
 */
@Controller
@RequestMapping("api")
public class ConfigController {

    @GetMapping("/home")
    public String home(){
        return "hello,world";
    }

}
