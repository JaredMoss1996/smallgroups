package com.smallgroups.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "small_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmallGroup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String churchName;
    
    private String denomination;
    
    @Column(nullable = false)
    private String location;
    
    private String address;
    
    private Double latitude;
    
    private Double longitude;
    
    @Column(length = 1000)
    private String description;
    
    private String ageGroup; // e.g., "18-25", "26-35", "36-45", "46-55", "56+", "All Ages"
    
    private String gender; // "Men Only", "Women Only", "Co-Ed"
    
    private String type; // e.g., "Bible Study", "Prayer Group", "Social", "Service", "Discipleship"
    
    private String meetingDay; // e.g., "Monday", "Tuesday", etc.
    
    private String meetingTime;
    
    private Boolean childcareIncluded = false;
    
    private Boolean handicapAccessible = false;
    
    private String contactName;
    
    private String contactEmail;
    
    private String contactPhone;
    
    private Integer currentSize;
    
    private Integer maxSize;
}
