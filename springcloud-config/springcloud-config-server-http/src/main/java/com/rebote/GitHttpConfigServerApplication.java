package com.rebote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @ProjectName: springcloud-parent
 * @Package: com.rebote
 * @ClassName: GitHttpConfigServerApplication
 * @Author: lmluo
 * @Description: git基于HTTP方式
 * @Date: 2020/9/17 9:55
 * @Version: 1.0
 */
@SpringBootApplication
@EnableConfigServer
public class GitHttpConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitHttpConfigServerApplication.class, args);
    }
}
