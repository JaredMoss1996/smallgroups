package com.smallgroups.repository;

import com.smallgroups.model.Church;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ChurchRepository {
    
    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert jdbcInsert;
    
    public ChurchRepository(JdbcClient jdbcClient, DataSource dataSource) {
        this.jdbcClient = jdbcClient;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("churches")
                .usingGeneratedKeyColumns("id");
    }
    
    public List<Church> findAll() {
        String sql = "SELECT * FROM churches ORDER BY name";
        return jdbcClient.sql(sql)
                .query(this::mapRowToChurch)
                .list();
    }
    
    public Optional<Church> findById(Long id) {
        String sql = "SELECT * FROM churches WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("id", id)
                .query(this::mapRowToChurch)
                .optional();
    }
    
    public Church save(Church church) {
        if (church.getId() == null) {
            return insert(church);
        } else {
            return update(church);
        }
    }
    
    private Church insert(Church church) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", church.getName());
        parameters.put("denomination", church.getDenomination());
        parameters.put("address", church.getAddress());
        parameters.put("city", church.getCity());
        parameters.put("state", church.getState());
        parameters.put("zip_code", church.getZipCode());
        
        Number newId = jdbcInsert.executeAndReturnKey(parameters);
        church.setId(newId.longValue());
        return church;
    }
    
    private Church update(Church church) {
        String sql = """
            UPDATE churches SET
                name = :name,
                denomination = :denomination,
                address = :address,
                city = :city,
                state = :state,
                zip_code = :zipCode
            WHERE id = :id
            """;
        
        jdbcClient.sql(sql)
                .param("id", church.getId())
                .param("name", church.getName())
                .param("denomination", church.getDenomination())
                .param("address", church.getAddress())
                .param("city", church.getCity())
                .param("state", church.getState())
                .param("zipCode", church.getZipCode())
                .update();
        
        return church;
    }
    
    private Church mapRowToChurch(ResultSet rs, int rowNum) throws SQLException {
        Church church = new Church();
        church.setId(rs.getLong("id"));
        church.setName(rs.getString("name"));
        church.setDenomination(rs.getString("denomination"));
        church.setAddress(rs.getString("address"));
        church.setCity(rs.getString("city"));
        church.setState(rs.getString("state"));
        church.setZipCode(rs.getString("zip_code"));
        return church;
    }
}
