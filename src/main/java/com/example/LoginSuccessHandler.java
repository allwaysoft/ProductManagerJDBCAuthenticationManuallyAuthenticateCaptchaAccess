package com.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
//获取用户认证信息
        System.out.println("onAuthenticationSuccess");
        System.out.println(authentication.getAuthorities());
        Object principal = authentication.getPrincipal();
        System.out.println(principal.getClass());
        //判断数据是否为空 以及类型是否正确
        if (null != principal && principal instanceof org.springframework.security.core.userdetails.User) {
            String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
            System.out.println(username);
            User user = userRepository.getByUsername(username);
            System.out.println(request.getContextPath());
            String redirectURL = request.getContextPath();

            if (user.getEmail().equals("new")) {

                redirectURL = "/new";
                System.out.println(redirectURL);
            }

            response.sendRedirect(redirectURL);

        }

    }

}
