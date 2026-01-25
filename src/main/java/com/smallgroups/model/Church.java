package com.smallgroups.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Church {
    
    private Long id;
    private String name;
    private String denomination;
    private String address;
    private String city;
    private String state;
    private String zipCode;
}
