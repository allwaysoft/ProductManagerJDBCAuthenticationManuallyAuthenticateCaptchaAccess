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

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * 注入身份认证管理器
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/user/new")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "user/new_user";
    }

    @GetMapping("/user")
    public String listUsers(Model model) {
        List<User> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);

        return "user/list_user";
    }

    @GetMapping("/user/edit/{id}")
    public String editUser(@PathVariable("id") Integer id, Model model) {
        User user = userService.get(id);
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "user/edit_user";
    }

    @PostMapping("/user/save")
    public String saveUser(User user) {
        userService.save(user);

        return "redirect:/user";
    }

    @RequestMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id) {
        userRepository.deleteById(id);

        return "redirect:/user";
    }

    @RequestMapping(value = "user/info", method = RequestMethod.GET)
    public ModelAndView userProfile() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
        User user = userRepository.getByUsername(auth.getName());
        modelAndView.addObject("user", user);
        modelAndView.addObject("userName", user.getUsername());
        modelAndView.setViewName("user-profile");
        return modelAndView;
    }

    @RequestMapping(value = "/change/password", method = RequestMethod.GET)
    public ModelAndView changePassword() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userDTO", new UserDTO());
        modelAndView.setViewName("password-update");
        return modelAndView;
    }

    @RequestMapping(value = "/new/password", method = RequestMethod.POST)
    public ModelAndView newPassword(UserDTO userDTO) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (userDTO.getNewPass().equals(userDTO.getConfirmPass())) {
            User user = userService.findUserByUsername(auth.getName());
            Boolean status = userService.isPasswordValid(userDTO.getPassword(), user.getPassword());
            if (status == true) {

                userService.changePasswor(user, userDTO);
                modelAndView.setViewName("login");
            } else {
                modelAndView.addObject("wrongPass", "Current possword was wrong..!");
                modelAndView.setViewName("password-update");
            }

        } else {
            modelAndView.addObject("passMatched", "Password doesn't matched..!");
            modelAndView.setViewName("password-update");
        }
        return modelAndView;
    }

}
