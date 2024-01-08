package com.progettoweb.webmeditrackbackend.controller;

import com.progettoweb.webmeditrackbackend.persistence.model.Doctor;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestServices {
    @GetMapping("/hello")
    public String hello()
    {
        return "hello";
    }
}
