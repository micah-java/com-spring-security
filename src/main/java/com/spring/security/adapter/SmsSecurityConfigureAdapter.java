package com.spring.security.adapter;

import com.spring.security.filter.SmsLoginAuthenticationFilter;
import com.spring.security.handler.WebAuthenticationSuccessHandler;
import com.spring.security.provider.SmsAuthenticationProvider;
import com.spring.security.service.SmsUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Configuration
public class SmsSecurityConfigureAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private WebAuthenticationSuccessHandler webAuthenticationSuccessHandler;
    @Autowired
    private SmsUserDetailsService smsUserDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsLoginAuthenticationFilter smsLoginAuthenticationFilter = new SmsLoginAuthenticationFilter();
        smsLoginAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsLoginAuthenticationFilter.setAuthenticationSuccessHandler(webAuthenticationSuccessHandler);
       // smsLoginAuthenticationFilter.setFilterProcessesUrl("/sms/login");

        SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider(smsUserDetailsService);

        http.authenticationProvider(smsAuthenticationProvider)
                .addFilterAfter(smsLoginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
