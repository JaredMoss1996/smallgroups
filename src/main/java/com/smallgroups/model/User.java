package com.smallgroups.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private Long id;
    private String email;
    private String password;
    private String name;
    private String provider; // "local" or "google"
    private Boolean enabled = true;
}
