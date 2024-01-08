package com.progettoweb.webmeditrackbackend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Services {
    @GetMapping("/userInfo")
    public String showUserInfo(Model model, HttpSession session) {
        Object userObject = session.getAttribute("user");

        String userType = (userObject != null) ? userObject.getClass().getSimpleName() : "Unknown";

        model.addAttribute("userType", userType);

        return "userInfo";
    }
}
