package com.example;

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
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller

public class LoginController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 注入身份认证管理器
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping(value = "/verify")
    public String login(@RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("verifyCode") String verifyCode,
            HttpSession session) {
        System.out.println("username is:" + username);
        System.out.println("password is:" + password);
        System.out.println("verifyCode is:" + verifyCode);
        if (StringUtils.isEmpty(verifyCode)) {
            session.setAttribute("errorMsg", "The verification code cannot be empty");
            return "login";
        }
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "User name or password cannot be empty");
            return "login";
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        System.out.println("kaptchaCode is:" + kaptchaCode);
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            session.setAttribute("errorMsg", "Verification code error");
            return "login";
        }
//        User user = userService.login(userName, password);

        System.out.println(username + "==" + password + "==" + verifyCode);
        // 创建用户名与密码认证对象
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            // 调用认证方法，返回认证对象
            Authentication authenticate = authenticationManager.authenticate(token);
            // 判断是否认证成功
            if (authenticate.isAuthenticated()) {
                // 设置用户认证成功，往Session中添加认证通过信息
                SecurityContextHolder.getContext().setAuthentication(authenticate);
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(authenticate);
                session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
                // 重定向到登录成功页面
                System.out.println(authenticate.getAuthorities());
                Object principal = authenticate.getPrincipal();
                System.out.println(principal.getClass());
                //判断数据是否为空 以及类型是否正确
                if (null != principal && principal instanceof org.springframework.security.core.userdetails.User) {
//                    String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
//                    System.out.println(username);
                    User user = userRepository.getByUsername(username);

                    if (user.getEmail().equals("new")) {

                        return "redirect:/new";

                    }

                }
                return "redirect:/";

            } else {
                session.setAttribute("errorMsg", "Login failed");
                return "login";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "login";
    }
}
