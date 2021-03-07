package com.spring.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system")
public class SystemController {

    @RequestMapping("/index")
    private String index(){
        return "index";
    }

    @RequestMapping("/failure")
    private String failure(){
        return "failure";
    }

    @RequestMapping("/customer")
    private String customer(){
        return "customer";
    }

    @RequestMapping("/role")
    private String role(){
        return "role";
    }

    @RequestMapping("/menu")
    private String menu(){
        return "menu";
    }
}
