package com.example;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@Controller

public class LoginController {

    private static final long PASSWORD_EXPIRATION_TIME = 30L * 24L * 60L * 60L * 1000L;    // 30 days

    @Autowired
    private UserService userService;

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
        User userCheck = userRepository.getByUsername(username);
        if (userCheck != null) {
            if (!userCheck.isAccountNonLocked()) {
                if (userService.unlockWhenTimeExpired(userCheck)) {

                    System.out.println("Your account has been unlocked. Please try to login again.");
                }
            } else {

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

                            if (user.getFailedAttempt() > 0) {
                                userService.resetFailedAttempts(user.getUsername());
                            }

                            if (user.getPasswordChangedTime() == null) {
                                return "redirect:/change/password";
                            }

                            long currentTime = System.currentTimeMillis();
                            long lastChangedTime = user.getPasswordChangedTime().getTime();

                            if (currentTime > lastChangedTime + PASSWORD_EXPIRATION_TIME) {
                                return "redirect:/change/password";
                            }

                            String homepage = "redirect:/" + user.getHomepage();

                            return homepage;

                        }
                        return "redirect:/";

                    } else {

                        User user = userService.findUserByUsername(username);

                        if (user != null) {
                            if (user.isEnabled() == true && user.isAccountNonLocked()) {
                                if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                                    userService.increaseFailedAttempts(user);
                                } else {
                                    userService.lock(user);
                                    System.out.println("Your account has been locked due to 3 failed attempts."
                                            + " It will be unlocked after 24 hours.");
                                }
                            } else if (!user.isAccountNonLocked()) {
                                if (userService.unlockWhenTimeExpired(user)) {
                                    System.out.println("Your account has been unlocked. Please try to login again.");
                                }
                            }

                        }

                        session.setAttribute("errorMsg", "Login failed");
                        return "login";
                    }
                } catch (BadCredentialsException ex) {
                    System.out.println("BadCredentialsException");
                    User user = userService.findUserByUsername(username);

                    if (user != null) {
                        if (user.isEnabled() == true && user.isAccountNonLocked()) {
                            System.out.println("user.getEnabled() == 1");
                            if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                                userService.increaseFailedAttempts(user);
                            } else {
                                userService.lock(user);
                                System.out.println("Your account has been locked due to 3 failed attempts."
                                        + " It will be unlocked after 24 hours.");
                            }
                        } else if (!user.isAccountNonLocked()) {
                            if (userService.unlockWhenTimeExpired(user)) {
                                System.out.println("Your account has been unlocked. Please try to login again.");
                            }
                        }

                    }
//            ex.printStackTrace();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model
    ) {
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user
    ) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model
    ) {
        List<User> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Integer id, Model model
    ) {
        User user = userService.get(id);
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user
    ) {
        userService.save(user);

        return "redirect:/users";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        String name = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        System.out.println(name);
        modelAndView.addObject("userName",
                "Welcome " + user.getUsername());
        modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
        modelAndView.setViewName("home");
        return modelAndView;
    }
}
