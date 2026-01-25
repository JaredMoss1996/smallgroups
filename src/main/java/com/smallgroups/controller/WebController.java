package com.smallgroups.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
    
    @GetMapping("/find")
    public String findGroups() {
        return "find-groups";
    }
    
    @GetMapping("/create")
    public String createGroup() {
        return "create-group";
    }
    
    @GetMapping("/joined-groups")
    public String joinedGroups() {
        return "joined-groups";
    }
}
