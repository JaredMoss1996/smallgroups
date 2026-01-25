package com.smallgroups.repository;

import com.smallgroups.model.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    
    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert jdbcInsert;
    
    public UserRepository(JdbcClient jdbcClient, DataSource dataSource) {
        this.jdbcClient = jdbcClient;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }
    
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :email";
        return jdbcClient.sql(sql)
                .param("email", email)
                .query(this::mapRowToUser)
                .optional();
    }
    
    public User save(User user) {
        if (user.getId() == null) {
            return insert(user);
        } else {
            return update(user);
        }
    }
    
    private User insert(User user) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", user.getEmail());
        parameters.put("password", user.getPassword());
        parameters.put("name", user.getName());
        parameters.put("provider", user.getProvider());
        parameters.put("enabled", user.getEnabled());
        
        Number newId = jdbcInsert.executeAndReturnKey(parameters);
        user.setId(newId.longValue());
        return user;
    }
    
    private User update(User user) {
        String sql = """
            UPDATE users SET
                email = :email,
                password = :password,
                name = :name,
                provider = :provider,
                enabled = :enabled
            WHERE id = :id
            """;
        
        jdbcClient.sql(sql)
                .param("id", user.getId())
                .param("email", user.getEmail())
                .param("password", user.getPassword())
                .param("name", user.getName())
                .param("provider", user.getProvider())
                .param("enabled", user.getEnabled())
                .update();
        
        return user;
    }
    
    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setName(rs.getString("name"));
        user.setProvider(rs.getString("provider"));
        user.setEnabled(rs.getBoolean("enabled"));
        return user;
    }
}
