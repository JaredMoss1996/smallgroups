package com.smallgroups.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequest {
    
    private Long id;
    private Long userId;
    private Long churchId;
    private String message;
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
    private Long reviewedBy;
}
