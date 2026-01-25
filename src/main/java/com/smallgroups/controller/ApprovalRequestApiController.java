package com.smallgroups.controller;

import com.smallgroups.model.ApprovalRequest;
import com.smallgroups.model.User;
import com.smallgroups.service.ApprovalRequestService;
import com.smallgroups.service.CustomUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/approval-requests")
public class ApprovalRequestApiController {
    
    private final ApprovalRequestService approvalRequestService;
    private final CustomUserDetailsService userDetailsService;
    
    public ApprovalRequestApiController(ApprovalRequestService approvalRequestService, 
                                       CustomUserDetailsService userDetailsService) {
        this.approvalRequestService = approvalRequestService;
        this.userDetailsService = userDetailsService;
    }
    
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody Map<String, String> request, 
                                          Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userDetailsService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }
            
            String churchIdStr = request.get("churchId");
            if (churchIdStr == null || churchIdStr.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Church ID is required"));
            }
            
            Long churchId = Long.parseLong(churchIdStr);
            String message = request.get("message");
            
            ApprovalRequest approvalRequest = approvalRequestService.createRequest(
                    user.getId(), churchId, message);
            return ResponseEntity.ok(approvalRequest);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid church ID"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/my-requests")
    public ResponseEntity<List<ApprovalRequest>> getMyRequests(Authentication authentication) {
        String email = authentication.getName();
        User user = userDetailsService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(approvalRequestService.getRequestsByUser(user.getId()));
    }
    
    @GetMapping("/church/{churchId}")
    public ResponseEntity<?> getRequestsByChurch(@PathVariable Long churchId, Authentication authentication) {
        String email = authentication.getName();
        User user = userDetailsService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(403).body(Map.of("error", "User not found"));
        }
        
        // Only ADMIN users or users with the church as their home church can view requests
        if (!"ADMIN".equals(user.getRole()) && !churchId.equals(user.getHomeChurchId())) {
            return ResponseEntity.status(403).body(Map.of("error", "Not authorized to view these requests"));
        }
        
        return ResponseEntity.ok(approvalRequestService.getRequestsByChurch(churchId));
    }
    
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        User user = userDetailsService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }
        
        // Only ADMIN users can approve requests
        if (!"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).body(Map.of("error", "Only administrators can approve requests"));
        }
        
        approvalRequestService.approveRequest(id, user.getId());
        return ResponseEntity.ok(Map.of("message", "Request approved"));
    }
    
    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        User user = userDetailsService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }
        
        // Only ADMIN users can reject requests
        if (!"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).body(Map.of("error", "Only administrators can reject requests"));
        }
        
        approvalRequestService.rejectRequest(id, user.getId());
        return ResponseEntity.ok(Map.of("message", "Request rejected"));
    }
}
