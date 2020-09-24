package com.rebote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ProjectName: springcloud-parent
 * @Package: com.rebote
 * @ClassName: ConfigClientApplication
 * @Author: lmluo
 * @Description: 配置中心客户端
 * @Date: 2020/9/16 19:51
 * @Version: 1.0
 */
@SpringBootApplication
public class ConfigClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }
}
