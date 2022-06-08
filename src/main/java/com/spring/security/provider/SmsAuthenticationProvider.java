package com.spring.security.provider;

import com.spring.security.entity.SecurityUser;
import com.spring.security.service.SmsUserDetailsService;
import com.spring.security.token.SmsLoginAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class SmsAuthenticationProvider implements AuthenticationProvider {

    private SmsUserDetailsService smsUserDetailsService;

    public SmsAuthenticationProvider(SmsUserDetailsService smsUserDetailsService) {
        this.smsUserDetailsService = smsUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsLoginAuthenticationToken smsLoginAuthenticationToken = (SmsLoginAuthenticationToken)authentication;
        //获取电话
        Object principal = smsLoginAuthenticationToken.getPrincipal();
        //获取短信验证码
        Object credentials = smsLoginAuthenticationToken.getCredentials();

        UserDetails securityUser = smsUserDetailsService.loadUserByUsername((String) principal);

        SmsLoginAuthenticationToken token = new SmsLoginAuthenticationToken(securityUser.getAuthorities(),principal,credentials);
        token.setDetails(smsLoginAuthenticationToken.getDetails());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
