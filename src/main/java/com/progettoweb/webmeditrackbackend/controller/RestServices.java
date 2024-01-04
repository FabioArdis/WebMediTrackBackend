package com.progettoweb.webmeditrackbackend.controller;

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
