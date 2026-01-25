package com.smallgroups.controller;

import com.smallgroups.model.SmallGroup;
import com.smallgroups.model.User;
import com.smallgroups.service.CustomUserDetailsService;
import com.smallgroups.service.SmallGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class SmallGroupApiController {
    
    private final SmallGroupService service;
    private final CustomUserDetailsService userDetailsService;
    
    public SmallGroupApiController(SmallGroupService service, CustomUserDetailsService userDetailsService) {
        this.service = service;
        this.userDetailsService = userDetailsService;
    }
    
    @GetMapping
    public List<SmallGroup> getAllGroups() {
        return service.getAllGroups();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SmallGroup> getGroupById(@PathVariable Long id) {
        return service.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody SmallGroup group, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userDetailsService.getUserByEmail(email);
            
            if (user == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }
            
            // Check if user is approved to create groups or is an admin
            if (!user.getApprovedForGroupCreation() && 
                !"ADMIN".equals(user.getRole()) && 
                !"GROUP_CREATOR".equals(user.getRole())) {
                return ResponseEntity.status(403).body(Map.of("error", 
                    "You must be approved by your home church to create groups"));
            }
            
            group.setCreatorId(user.getId());
            SmallGroup createdGroup = service.createGroup(group);
            return ResponseEntity.ok(createdGroup);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SmallGroup> updateGroup(@PathVariable Long id, @RequestBody SmallGroup group) {
        try {
            SmallGroup updatedGroup = service.updateGroup(id, group);
            return ResponseEntity.ok(updatedGroup);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        service.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public List<SmallGroup> searchGroups(
            @RequestParam(required = false) String churchName,
            @RequestParam(required = false) String denomination,
            @RequestParam(required = false) String ageGroup,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean childcareIncluded,
            @RequestParam(required = false) Boolean handicapAccessible,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) Double userLat,
            @RequestParam(required = false) Double userLon
    ) {
        return service.searchGroups(churchName, denomination, ageGroup, gender, type, 
                location, childcareIncluded, handicapAccessible, radius, userLat, userLon);
    }
    
    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinGroup(@PathVariable Long id, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userDetailsService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }
            service.joinGroup(user.getId(), id);
            return ResponseEntity.ok(Map.of("message", "Successfully joined the group"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/leave")
    public ResponseEntity<?> leaveGroup(@PathVariable Long id, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userDetailsService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }
            service.leaveGroup(user.getId(), id);
            return ResponseEntity.ok(Map.of("message", "Successfully left the group"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/joined")
    public ResponseEntity<?> getJoinedGroups(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userDetailsService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }
            List<SmallGroup> groups = service.getJoinedGroups(user.getId());
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/{id}/is-member")
    public ResponseEntity<?> isUserMember(@PathVariable Long id, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userDetailsService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.ok(Map.of("isMember", false));
            }
            boolean isMember = service.isUserMember(user.getId(), id);
            return ResponseEntity.ok(Map.of("isMember", isMember));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
