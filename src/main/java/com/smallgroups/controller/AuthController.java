package com.smallgroups.controller;

import com.smallgroups.model.User;
import com.smallgroups.service.CustomUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final CustomUserDetailsService userDetailsService;
    
    public AuthController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> signupRequest) {
        try {
            String email = signupRequest.get("email");
            String password = signupRequest.get("password");
            String name = signupRequest.get("name");
            String homeChurchIdStr = signupRequest.get("homeChurchId");
            
            if (email == null || password == null || name == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email, password, and name are required"));
            }
            
            Long homeChurchId = null;
            if (homeChurchIdStr != null && !homeChurchIdStr.isEmpty()) {
                homeChurchId = Long.parseLong(homeChurchIdStr);
            }
            
            User user = userDetailsService.registerUser(email, password, name, homeChurchId);
            return ResponseEntity.ok(Map.of("message", "User registered successfully", "email", user.getEmail()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
