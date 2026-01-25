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
            
            Long churchId = Long.parseLong(request.get("churchId"));
            String message = request.get("message");
            
            ApprovalRequest approvalRequest = approvalRequestService.createRequest(
                    user.getId(), churchId, message);
            return ResponseEntity.ok(approvalRequest);
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
    public ResponseEntity<List<ApprovalRequest>> getRequestsByChurch(@PathVariable Long churchId) {
        return ResponseEntity.ok(approvalRequestService.getRequestsByChurch(churchId));
    }
    
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        User user = userDetailsService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
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
        approvalRequestService.rejectRequest(id, user.getId());
        return ResponseEntity.ok(Map.of("message", "Request rejected"));
    }
}
