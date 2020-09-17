package com.rebote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @ProjectName: springcloud-parent
 * @Package: com.rebote
 * @ClassName: GitConfigServerApplication
 * @Author: lmluo
 * @Description: 非对称加密
 * @Date: 2020/9/17 11:46
 * @Version: 1.0
 */
@SpringBootApplication
@EnableConfigServer
public class GitConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitConfigServerApplication.class, args);
    }
}
