package com.smallgroups.service;

import com.smallgroups.model.ApprovalRequest;
import com.smallgroups.repository.ApprovalRequestRepository;
import com.smallgroups.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ApprovalRequestService {
    
    private final ApprovalRequestRepository approvalRequestRepository;
    private final UserRepository userRepository;
    
    public ApprovalRequestService(ApprovalRequestRepository approvalRequestRepository, UserRepository userRepository) {
        this.approvalRequestRepository = approvalRequestRepository;
        this.userRepository = userRepository;
    }
    
    public ApprovalRequest createRequest(Long userId, Long churchId, String message) {
        ApprovalRequest request = new ApprovalRequest();
        request.setUserId(userId);
        request.setChurchId(churchId);
        request.setMessage(message);
        request.setStatus("PENDING");
        return approvalRequestRepository.save(request);
    }
    
    public List<ApprovalRequest> getRequestsByChurch(Long churchId) {
        return approvalRequestRepository.findByChurchId(churchId);
    }
    
    public List<ApprovalRequest> getRequestsByUser(Long userId) {
        return approvalRequestRepository.findByUserId(userId);
    }
    
    public void approveRequest(Long requestId, Long reviewerId) {
        Optional<ApprovalRequest> optionalRequest = approvalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            ApprovalRequest request = optionalRequest.get();
            request.setStatus("APPROVED");
            request.setReviewedAt(LocalDateTime.now());
            request.setReviewedBy(reviewerId);
            approvalRequestRepository.save(request);
            
            // Update user to be approved for group creation
            userRepository.findById(request.getUserId()).ifPresent(user -> {
                user.setApprovedForGroupCreation(true);
                userRepository.save(user);
            });
        }
    }
    
    public void rejectRequest(Long requestId, Long reviewerId) {
        Optional<ApprovalRequest> optionalRequest = approvalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            ApprovalRequest request = optionalRequest.get();
            request.setStatus("REJECTED");
            request.setReviewedAt(LocalDateTime.now());
            request.setReviewedBy(reviewerId);
            approvalRequestRepository.save(request);
        }
    }
}
