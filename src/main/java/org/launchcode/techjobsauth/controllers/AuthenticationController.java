package org.launchcode.techjobsauth.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.launchcode.techjobsauth.models.User;
import org.launchcode.techjobsauth.models.data.UserRepository;
import org.launchcode.techjobsauth.models.dto.LoginFormDTO;
import org.launchcode.techjobsauth.models.dto.RegisterFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


public class AuthenticationController {
    @Autowired
    UserRepository userRepository;

    public static final String userSessionKey ="user";

    public User getUserSession(HttpSession session){
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if(userId == null){
            return null;
        }
        Optional<User> user =userRepository.findById(userId);
        if(user.isEmpty()){
            return null;
        }

        return user.get();
    }

    public static void setUserSessionKey(HttpSession session,User user){
            session.setAttribute(userSessionKey,user.getId());
    }

    @GetMapping("/register")
    public String displayRegistrationForm(Model model){

        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title","Register");
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors, HttpServletRequest request,
                                          Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");
            return "register";
        }

        User existingUser = userRepository.findByUserName(registerFormDTO.getUserName());
        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists");
            model.addAttribute("title", "Register");
            return "register";
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Register");
            return "register";
        }

        User newUser = new User(registerFormDTO.getUserName(), registerFormDTO.getPassword());
        userRepository.save(newUser);
        setUserSessionKey(request.getSession(), newUser);

        return "redirect:";
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDtO, Errors errors,
                                   HttpServletRequest request, Model model){

        if(errors.hasErrors()){
            model.addAttribute("title","Log In");
            return "login";
        }

        User theUser = userRepository.findByUserName(loginFormDtO.getUserName());

        if(theUser == null){
            errors.rejectValue("userName","user.invalid","Invalid user name!");
            model.addAttribute("title","Log In");
            return "login";
        }

        String password = loginFormDtO.getPassword();
        if(!theUser.isMatchingPassword(password)){
            errors.rejectValue("password","user.invalid","Invalid password!");
            model.addAttribute("title","Log In");
            return "login";
        }

        setUserSessionKey(request.getSession(), theUser);

        return "redirect:";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
