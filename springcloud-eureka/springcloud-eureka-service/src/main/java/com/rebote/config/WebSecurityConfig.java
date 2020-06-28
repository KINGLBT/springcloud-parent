package com.rebote.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

/**
 * @ClassName: WebSecurityConfig
 * @description:
 * @author: luomeng
 * @time: 2020/6/27 23:28
 */

@Component
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 默认开启csrf，如果不过滤地址，客户端注册的时候报403错误
        http.csrf().ignoringAntMatchers("/eureka/**");
        super.configure(http);
    }
}
