package com.example;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

@Controller

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 注入身份认证管理器
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/user/new")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());

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
    public String saveUser(@Valid @ModelAttribute("user") UserDTO user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/new_user";
        }
        userService.saveUser(user);

        return "redirect:/user";
    }

    @PostMapping("/user/update")
    public String updateUser(User user) {
        User repoUser = userRepository.findById(user.getId()).orElse(null);
        repoUser.setUsername(user.getUsername());
        repoUser.setEmail(user.getEmail());
        repoUser.setName(user.getName());
        repoUser.setEnabled(user.getEnabled());
        repoUser.setHomepage(user.getHomepage());
        repoUser.setRoles(user.getRoles());
        userRepository.save(repoUser);

        return "redirect:/user";
    }

    @GetMapping("/user/resetpassword/{id}")
    public String showResetPasswordForm(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "user/reset_password";
    }

    @PostMapping("/user/savepassword")
    public String savePassword(User user) {
        User repoUser = userRepository.findById(user.getId()).orElse(null);

        repoUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(repoUser);

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
