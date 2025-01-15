package com.hospital.controller;

import com.hospital.model.User;
import com.hospital.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        User user = userService.getUserByUsername(username);
        if (user != null && password.equals(user.getPassword())) {
            // Store user info in session
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("userName", user.getName());


            switch (user.getRole()) {
                case ADMIN:
                    return "redirect:/admin/dashboard";
                case DOCTOR:
                    return "redirect:/doctor/dashboard";
                case PATIENT:
                    return "redirect:/patient/dashboard";
                case RADIOLOGIST:
                    return "redirect:/radiologist/dashboard";
                case FINANCE:
                    return "redirect:/finance/dashboard";
                default:
                    return "redirect:/login?error";
            }
        }
        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}