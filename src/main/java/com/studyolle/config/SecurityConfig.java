package com.studyolle.config;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //EnableWebSecurity -> 직접 스프링 시큐리티 설정을 하겠다는 어노테이션
    // websecurity 설정을 좀 손쉽게 하려면 WebSecurityConfigurerAdapter를 상속

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 원하는 특정 요청들을 authorize 체크를 하지 않도록 걸러낼 수 있음
        http.authorizeRequests()
                .mvcMatchers("/", "/login", "/sign-up", "/check-email", "/check-email-token",
                "/email-login", "/check-email-login", "/login-link").permitAll()       // mvcMatchers로 해당 링크는 모두 허용한다.
                .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll()      // profile 링크는 GET만 허용한다.
                .anyRequest().authenticated();                                          // 그 외 링크는 모두 인증해야 쓸 수 있다.

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers("/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
