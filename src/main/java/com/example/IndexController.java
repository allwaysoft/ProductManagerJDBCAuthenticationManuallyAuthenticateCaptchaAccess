package com.example;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@Controller

public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/index2")
    public String index2() {
        return "index2";
    }

    @GetMapping("/index3")
    public String index3() {
        return "index3";
    }

    @GetMapping("/header")
    public String header() {
        return "fragments/header";
    }

    @GetMapping("/footer")
    public String footer() {
        return "fragments/footer";
    }

    @GetMapping("/sidebar")
    public String sidebar() {
        return "fragments/sidebar";
    }
}
