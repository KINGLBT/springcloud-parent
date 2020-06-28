package com.rebote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @ClassName: Application
 * @description:
 * @author: luomeng
 * @time: 2020/6/28 12:30
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaClusterApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClusterApplication.class, args);
    }
}
