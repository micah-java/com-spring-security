package com.spring.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.token.SmsLoginAuthenticationToken;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class SmsLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final static AntPathRequestMatcher ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/sms/login","POST");

    public SmsLoginAuthenticationFilter() {
        super(ANT_PATH_REQUEST_MATCHER);
    }

    public SmsLoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(!request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("请求方式错误");
        }

        Map<String,String> map = new ObjectMapper().readValue(request.getInputStream(), Map.class);
        String mobile = map.get("mobile");
        String smsCode = map.get("smsCode");
        SmsLoginAuthenticationToken token = new SmsLoginAuthenticationToken(mobile,smsCode);
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(token);
    }
}
