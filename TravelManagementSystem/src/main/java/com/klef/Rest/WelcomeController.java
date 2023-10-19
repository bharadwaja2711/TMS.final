package com.klef.Rest;


import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WelcomeController {

    @Autowired
    private ReCaptchaValidationService validator;

    @Autowired
    private RegistrationRepository registrationRepository;

    @RequestMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("registrationEntity", new RegistrationEntity());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registrationEntity") RegistrationEntity registrationEntity,
                           @RequestParam(name = "g-recaptcha-response") String captcha, Model model) {
        try {
            if (validator.validateCaptcha(captcha)) {
                registrationRepository.save(registrationEntity);
                model.addAttribute("registrationEntity", new RegistrationEntity());
                model.addAttribute("message", "Registration successful!!");
            } else {
                model.addAttribute("message", "Please verify captcha.");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Error occurred during registration.");
        }

        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginEntity", new LoginEntity());
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginEntity loginEntity, Model model) {
        try {
            // Check credentials against data in the login table
            RegistrationEntity user = registrationRepository.findByUsernameAndPassword(loginEntity.getUsername(), loginEntity.getPassword());

            if (user != null) {
                if ("bharadwaja1.618@gmail.com".equals(loginEntity.getUsername()) && "123456789".equals(loginEntity.getPassword())) {
                    // Special case for specific username and password
                    return "indexLogin";
                } else {
                    // Normal successful login, redirect to the welcome page
                    return "welcome";
                }
            } else {
                // Incorrect credentials, show an error message
                model.addAttribute("message", "Incorrect username or password");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Error occurred during login.");
        }

        return "login";
    }


    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }
    
    @RequestMapping("/aboutLogin")
    public String aboutLogin()
    {
    	return "aboutLogin";
    }
    
    @RequestMapping("/aboutUser")
    public String aboutUser()
    {
    	return "aboutUser";
    }
    
    @RequestMapping("/indexLogin")
    public String indexLogin()
    {
    	return "indexLogin";
    }
    
    @RequestMapping("/admin")
    public String admin()
    {
    	return "admin";
    }
    
    @RequestMapping("/welcome")
    public String welcome()
    {
    	return "welcome";
    }
    
    @RequestMapping("/maps")
    public String maps()
    {
    	return "maps";
    }
    
    @RequestMapping("/flightsearch")
    public String flightsearch()
    {
    	return "flightsearch";
    }
    
    @RequestMapping("/UsersRegister")
    public String UsersRegister(Model model) {
        try {
            List<RegistrationEntity> userList = registrationRepository.findAll();
            model.addAttribute("userList", userList);
        } catch (Exception e) {
            model.addAttribute("message", "Error occurred while fetching user data.");
        }
        return "UsersRegister";
    }
    
    @RequestMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("forgotEntity", new ForgotEntity());
        return "forgot-password";
    }
    
    @PostMapping("/forgot-password")
    public String forgotPasswordSubmit(@ModelAttribute ForgotEntity forgotEntity, Model model) {
        try {
            // Check credentials against data in the registration table
            RegistrationEntity user = registrationRepository.findByUsernameAndSecurityQuestion(
                    forgotEntity.getUsername(), forgotEntity.getSecurityQuestion());

            if (user != null) {
                // Valid username and security question, you can implement password reset logic here
                // For now, we'll redirect to a success page
                return "welcome";
            } else {
                // Incorrect credentials, show an error message
                model.addAttribute("message", "Incorrect username or security question");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Error occurred during password reset.");
        }

        return "forgot-password";
    }
    
}
