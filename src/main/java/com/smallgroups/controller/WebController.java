package com.smallgroups.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping("/")
    public String index() {
        return "redirect:/find";
    }
    
    @GetMapping("/find")
    public String findGroups() {
        return "find-groups";
    }
    
    @GetMapping("/create")
    public String createGroup() {
        return "create-group";
    }
}
