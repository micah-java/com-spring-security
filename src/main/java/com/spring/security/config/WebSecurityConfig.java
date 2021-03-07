package com.spring.security.config;

import com.spring.security.handler.WebAuthenticationFailureHandler;
import com.spring.security.handler.WebAuthenticationSuccessHandler;
import com.spring.security.service.LubanUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LubanUserDetailsService lubanUserDetailsService;

    @Autowired
    private WebAuthenticationSuccessHandler webAuthenticationSuccessHandler;
    @Autowired
    private WebAuthenticationFailureHandler webAuthenticationFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(lubanUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        //表单登录
        httpSecurity.formLogin()
                //用户未登录时，访问任何资源都跳转到该页面
                .loginPage("/login.html")
                //登录表单提交的action路径
                .loginProcessingUrl("/login")
                //自定义登录名参数
                .usernameParameter("username")
                //自定义密码参数
                .passwordParameter("password")
                //登录成功跳转页面，必须是post请求，针对from表单
                //.successForwardUrl("/system/index")
                //登录失败跳转页面，针对from表单
                //.failureForwardUrl("/system/fail")
                .successHandler(webAuthenticationSuccessHandler)
                .failureHandler(webAuthenticationFailureHandler)
        ;
        //授权
        httpSecurity.authorizeRequests()
                //放行login.html页面
                .antMatchers("/login.html").permitAll()
                //放行失败页面
                .antMatchers("/failure.html").permitAll()
                .antMatchers("/js/**").permitAll()
                //.antMatchers("/system/**").permitAll()
                .antMatchers("/image/**").hasAuthority("admin")
                //其它请求的访问都需要登录
                .anyRequest().authenticated();

        httpSecurity.csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
