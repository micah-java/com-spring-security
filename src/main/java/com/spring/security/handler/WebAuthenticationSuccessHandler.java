package com.spring.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class WebAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse rsp, Authentication authentication) throws IOException, ServletException {
        rsp.setContentType("application/json;charset=utf-8");
        PrintWriter writer = rsp.getWriter();
        writer.write("{\"code\":\"0000\",\"message\":\"success\"}");
        writer.flush();
        writer.close();
    }
}
