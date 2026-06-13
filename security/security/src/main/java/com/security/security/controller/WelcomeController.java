package com.security.security.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin(origins = "*")  // ← YE ADD KARO

@RestController
@RequestMapping("/api/v1/admin")
public class WelcomeController {
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
    @GetMapping("/hi")
    public String hi(){
        return "hi";
    }
}
