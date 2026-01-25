package com.smallgroups.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup {
    
    private Long id;
    private Long userId;
    private Long groupId;
    private LocalDateTime joinedAt;
}
