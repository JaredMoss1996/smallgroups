package com.smallgroups.repository;

import com.smallgroups.model.ApprovalRequest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ApprovalRequestRepository {
    
    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert jdbcInsert;
    
    public ApprovalRequestRepository(JdbcClient jdbcClient, DataSource dataSource) {
        this.jdbcClient = jdbcClient;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("approval_requests")
                .usingGeneratedKeyColumns("id");
    }
    
    public List<ApprovalRequest> findByChurchId(Long churchId) {
        String sql = "SELECT * FROM approval_requests WHERE church_id = :churchId ORDER BY created_at DESC";
        return jdbcClient.sql(sql)
                .param("churchId", churchId)
                .query(this::mapRowToApprovalRequest)
                .list();
    }
    
    public List<ApprovalRequest> findByUserId(Long userId) {
        String sql = "SELECT * FROM approval_requests WHERE user_id = :userId ORDER BY created_at DESC";
        return jdbcClient.sql(sql)
                .param("userId", userId)
                .query(this::mapRowToApprovalRequest)
                .list();
    }
    
    public Optional<ApprovalRequest> findById(Long id) {
        String sql = "SELECT * FROM approval_requests WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("id", id)
                .query(this::mapRowToApprovalRequest)
                .optional();
    }
    
    public ApprovalRequest save(ApprovalRequest request) {
        if (request.getId() == null) {
            return insert(request);
        } else {
            return update(request);
        }
    }
    
    private ApprovalRequest insert(ApprovalRequest request) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_id", request.getUserId());
        parameters.put("church_id", request.getChurchId());
        parameters.put("message", request.getMessage());
        parameters.put("status", request.getStatus());
        
        Number newId = jdbcInsert.executeAndReturnKey(parameters);
        request.setId(newId.longValue());
        request.setCreatedAt(LocalDateTime.now());
        return request;
    }
    
    private ApprovalRequest update(ApprovalRequest request) {
        String sql = """
            UPDATE approval_requests SET
                status = :status,
                reviewed_at = :reviewedAt,
                reviewed_by = :reviewedBy
            WHERE id = :id
            """;
        
        jdbcClient.sql(sql)
                .param("id", request.getId())
                .param("status", request.getStatus())
                .param("reviewedAt", request.getReviewedAt())
                .param("reviewedBy", request.getReviewedBy())
                .update();
        
        return request;
    }
    
    private ApprovalRequest mapRowToApprovalRequest(ResultSet rs, int rowNum) throws SQLException {
        ApprovalRequest request = new ApprovalRequest();
        request.setId(rs.getLong("id"));
        request.setUserId(rs.getLong("user_id"));
        request.setChurchId(rs.getLong("church_id"));
        request.setMessage(rs.getString("message"));
        request.setStatus(rs.getString("status"));
        request.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        if (rs.getTimestamp("reviewed_at") != null) {
            request.setReviewedAt(rs.getTimestamp("reviewed_at").toLocalDateTime());
        }
        request.setReviewedBy(rs.getObject("reviewed_by", Long.class));
        return request;
    }
}
