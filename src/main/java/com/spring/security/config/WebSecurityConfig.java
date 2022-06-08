package com.spring.security.config;

import com.spring.security.adapter.SmsSecurityConfigureAdapter;
import com.spring.security.filter.JsonLoginAuthenticationFilter;
import com.spring.security.filter.SmsLoginAuthenticationFilter;
import com.spring.security.handler.WebAuthenticationSuccessHandler;
import com.spring.security.provider.SmsAuthenticationProvider;
import com.spring.security.service.SmsUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WebAuthenticationSuccessHandler webAuthenticationSuccessHandler;

    @Autowired
    private SmsUserDetailsService smsUserDetailsService;

    @Autowired
    private SmsSecurityConfigureAdapter smsSecurityConfigureAdapter;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("java")
                .password("{noop}java")
                .roles("admin")
                ;
    }

 /*   @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }*/

    @Bean
    public JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter() throws Exception {
        JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter = new JsonLoginAuthenticationFilter();
        jsonLoginAuthenticationFilter.setAuthenticationSuccessHandler(webAuthenticationSuccessHandler);
        //jsonLoginAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        jsonLoginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        jsonLoginAuthenticationFilter.setFilterProcessesUrl("/customer/login");
        return jsonLoginAuthenticationFilter;
    }

    //@Bean
    public SmsLoginAuthenticationFilter smsLoginAuthenticationFilter() throws Exception {
        SmsLoginAuthenticationFilter smsLoginAuthenticationFilter = new SmsLoginAuthenticationFilter();
        smsLoginAuthenticationFilter.setAuthenticationSuccessHandler(webAuthenticationSuccessHandler);
        smsLoginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        smsLoginAuthenticationFilter.setFilterProcessesUrl("/sms/login");

        return smsLoginAuthenticationFilter;
    }

    //@Bean
    JdbcTokenRepositoryImpl jdbcTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    /**
     * JSON登录
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //.and()
                //.rememberMe()
                //.tokenRepository(jdbcTokenRepository())
                .and()
                .csrf().disable();
        //将LoginFilter添加到UsernamePasswordAuthenticationFilter位置
        http.addFilterAt(jsonLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.apply(smsSecurityConfigureAdapter);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/js/**")
                .antMatchers("/*.html");
    }
}
