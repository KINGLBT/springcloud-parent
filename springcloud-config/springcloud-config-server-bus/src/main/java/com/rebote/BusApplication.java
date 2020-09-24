package com.rebote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @ProjectName: springcloud-parent
 * @Package: com.rebote
 * @ClassName: BusApplication
 * @Author: lmluo
 * @Description: bus消息总线
 * @Date: 2020/9/23 20:42
 * @Version: 1.0
 */
@SpringBootApplication
@EnableConfigServer
public class BusApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusApplication.class, args);
    }
}
