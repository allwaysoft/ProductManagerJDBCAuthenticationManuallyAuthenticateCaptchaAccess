package com.example;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.apache.commons.text.similarity.LevenshteinDistance;
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
        repoUser.setEnabled(user.isEnabled());
        repoUser.setAccountNonLocked(user.isAccountNonLocked());
        repoUser.setHomepage(user.getHomepage());
        repoUser.setRoles(user.getRoles());
        userRepository.save(repoUser);

        return "redirect:/user";
    }

    @GetMapping("/user/resetpassword/{id}")
    public String showResetPasswordForm(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        UserResetPasswordDTO userResetPasswordDTO = new UserResetPasswordDTO();
        userResetPasswordDTO.setId(user.getId());
        userResetPasswordDTO.setUsername(user.getUsername());
        model.addAttribute("user", userResetPasswordDTO);
        return "user/reset_password";
    }

    @PostMapping("/user/savepassword")
    public String savePassword(@Valid @ModelAttribute("user") UserResetPasswordDTO user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/reset_password";
        }
        User repoUser = userRepository.findById(user.getId()).orElse(null);

        repoUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Date passwordChangedTime = new Date();

        repoUser.setPasswordChangedTime(passwordChangedTime);
        userRepository.save(repoUser);

        return "redirect:/user";
    }

    @RequestMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id) {
        userRepository.deleteById(id);

        return "redirect:/user";
    }

//    @RequestMapping(value = "user/info", method = RequestMethod.GET)
//    public ModelAndView userProfile() {
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(auth.getName());
//        User user = userRepository.getByUsername(auth.getName());
//        modelAndView.addObject("user", user);
//        modelAndView.addObject("userName", user.getUsername());
//        modelAndView.setViewName("user-profile");
//        return modelAndView;
//    }
    @GetMapping("user/info")
    public String userProfile(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
        User user = userRepository.getByUsername(auth.getName());
        model.addAttribute("user", user);

        return "user/user_profile";
    }

//    @RequestMapping(value = "/change/password", method = RequestMethod.GET)
//    public ModelAndView changePassword() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("userDTO", new UserChangePasswordDTO());
//        modelAndView.setViewName("password-update");
//        return modelAndView;
//    }
    @GetMapping("/change/password")

    public String changePassword(Model model) {

        model.addAttribute("userDTO", new UserChangePasswordDTO());
        return "user/password_update";

    }

//    @RequestMapping(value = "/new/password", method = RequestMethod.POST)
//    public ModelAndView newPassword(@Valid @ModelAttribute("userDTO") UserChangePasswordDTO userChangePasswordDTO, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            ModelAndView modelAndView = new ModelAndView();
//            modelAndView.addObject("userDTO", new UserChangePasswordDTO());
//            modelAndView.setViewName("password-update");
//            return modelAndView;
//        }
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (userChangePasswordDTO.getNewPass().equals(userChangePasswordDTO.getConfirmPass())) {
//            User user = userService.findUserByUsername(auth.getName());
//            Boolean status = userService.isPasswordValid(userChangePasswordDTO.getPassword(), user.getPassword());
//            if (status == true) {
//
//                userService.changePassword(user, userChangePasswordDTO);
//                modelAndView.setViewName("login");
//            } else {
//                modelAndView.addObject("wrongPass", "Current possword was wrong..!");
//                modelAndView.setViewName("password-update");
//            }
//
//        } else {
//            modelAndView.addObject("passMatched", "Password doesn't matched..!");
//            modelAndView.setViewName("password-update");
//        }
//        return modelAndView;
//    }
    @PostMapping("/new/password")
    public String
            newPassword(@Valid @ModelAttribute("userDTO") UserChangePasswordDTO userChangePasswordDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/password_update";

        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (userChangePasswordDTO.getNewPass().equals(userChangePasswordDTO.getConfirmPass())) {
            User user = userService.findUserByUsername(auth.getName());
            Boolean status = userService.isPasswordValid(userChangePasswordDTO.getPassword(), user.getPassword());

            if (status == true) {

                LevenshteinDistance levenshteinWithThreshold = new LevenshteinDistance(3);
                // Returns -1 since the actual distance, 4, is higher than the threshold
                System.out.println("Levenshtein distance: " + levenshteinWithThreshold.apply(userChangePasswordDTO.getNewPass(), userChangePasswordDTO.getPassword()));
                if (levenshteinWithThreshold.apply(userChangePasswordDTO.getNewPass(), userChangePasswordDTO.getPassword()) == -1) {
                    Set<History> setHistorysCheck = user.getHistorys();
                    Boolean check = true;
                    for (History hist : setHistorysCheck) {
                        System.out.print(hist.getPassword());
                        if (bCryptPasswordEncoder.matches(userChangePasswordDTO.getNewPass(), hist.getPassword())) {
                            check = false;
                            break;
                        }
                    }

                    if (check == true) {

                        System.out.println(user.getHistorys());
                        History history = new History();
                        System.out.println("userChangePasswordDTO.getNewPass()=" + userChangePasswordDTO.getNewPass());
                        history.setPassword(bCryptPasswordEncoder.encode(userChangePasswordDTO.getNewPass()));
                        System.out.println(history);
                        Set<History> setHistorys = user.getHistorys();
                        setHistorys.add(history);
                        user.setHistorys(setHistorys);
                        System.out.println(user.getHistorys());
                        userService.changePassword(user, userChangePasswordDTO);
                        return "login";
                    } else {
                        model.addAttribute("passMatched", "New password was same as history..!");
                        return "user/password_update";
                    }
                } else {
                    model.addAttribute("passMatched", "New password need 4 diff wtih Current password..!");
                    return "user/password_update";
                }
            } else {

                model.addAttribute("wrongPass", "Current password was wrong..!");
                return "user/password_update";
            }

        } else {
            model.addAttribute("passMatched", "Password doesn't matched..!");
            return "user/password_update";
        }

    }

}
