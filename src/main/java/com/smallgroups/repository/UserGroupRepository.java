package com.smallgroups.repository;

import com.smallgroups.model.UserGroup;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserGroupRepository {
    
    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert jdbcInsert;
    
    public UserGroupRepository(JdbcClient jdbcClient, DataSource dataSource) {
        this.jdbcClient = jdbcClient;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("user_groups")
                .usingGeneratedKeyColumns("id");
    }
    
    public List<UserGroup> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_groups WHERE user_id = :userId ORDER BY joined_at DESC";
        return jdbcClient.sql(sql)
                .param("userId", userId)
                .query(this::mapRowToUserGroup)
                .list();
    }
    
    public boolean existsByUserIdAndGroupId(Long userId, Long groupId) {
        String sql = "SELECT COUNT(*) FROM user_groups WHERE user_id = :userId AND group_id = :groupId";
        Integer count = jdbcClient.sql(sql)
                .param("userId", userId)
                .param("groupId", groupId)
                .query(Integer.class)
                .single();
        return count > 0;
    }
    
    public UserGroup save(Long userId, Long groupId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_id", userId);
        parameters.put("group_id", groupId);
        
        Number newId = jdbcInsert.executeAndReturnKey(parameters);
        UserGroup userGroup = new UserGroup();
        userGroup.setId(newId.longValue());
        userGroup.setUserId(userId);
        userGroup.setGroupId(groupId);
        return userGroup;
    }
    
    public void delete(Long userId, Long groupId) {
        String sql = "DELETE FROM user_groups WHERE user_id = :userId AND group_id = :groupId";
        jdbcClient.sql(sql)
                .param("userId", userId)
                .param("groupId", groupId)
                .update();
    }
    
    private UserGroup mapRowToUserGroup(ResultSet rs, int rowNum) throws SQLException {
        UserGroup userGroup = new UserGroup();
        userGroup.setId(rs.getLong("id"));
        userGroup.setUserId(rs.getLong("user_id"));
        userGroup.setGroupId(rs.getLong("group_id"));
        userGroup.setJoinedAt(rs.getTimestamp("joined_at").toLocalDateTime());
        return userGroup;
    }
}
