package com.rebote.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: springcloud-parent
 * @Package: com.rebote.controller
 * @ClassName: BusRefreshController
 * @Author: lmluo
 * @Description: bus 刷新
 * @Date: 2020/9/24 18:21
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api")
@RefreshScope
public class BusRefreshController {

    @Value("${abc}")
    private String value;

    @RequestMapping("/testRefush")
    public String testRefush(){
        return "Hello"+value;
    }

}
