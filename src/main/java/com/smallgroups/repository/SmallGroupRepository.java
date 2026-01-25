package com.smallgroups.repository;

import com.smallgroups.model.SmallGroup;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SmallGroupRepository {
    
    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert jdbcInsert;
    
    public SmallGroupRepository(JdbcClient jdbcClient, DataSource dataSource) {
        this.jdbcClient = jdbcClient;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("small_groups")
                .usingGeneratedKeyColumns("id");
    }
    
    public List<SmallGroup> findAll() {
        String sql = "SELECT * FROM small_groups";
        return jdbcClient.sql(sql)
                .query(this::mapRowToSmallGroup)
                .list();
    }
    
    public Optional<SmallGroup> findById(Long id) {
        String sql = "SELECT * FROM small_groups WHERE id = :id";
        return jdbcClient.sql(sql)
                .param("id", id)
                .query(this::mapRowToSmallGroup)
                .optional();
    }
    
    public SmallGroup save(SmallGroup group) {
        if (group.getId() == null) {
            return insert(group);
        } else {
            return update(group);
        }
    }
    
    private SmallGroup insert(SmallGroup group) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", group.getName());
        parameters.put("church_name", group.getChurchName());
        parameters.put("denomination", group.getDenomination());
        parameters.put("location", group.getLocation());
        parameters.put("address", group.getAddress());
        parameters.put("latitude", group.getLatitude());
        parameters.put("longitude", group.getLongitude());
        parameters.put("description", group.getDescription());
        parameters.put("age_group", group.getAgeGroup());
        parameters.put("gender", group.getGender());
        parameters.put("type", group.getType());
        parameters.put("meeting_day", group.getMeetingDay());
        parameters.put("meeting_time", group.getMeetingTime());
        parameters.put("childcare_included", group.getChildcareIncluded());
        parameters.put("handicap_accessible", group.getHandicapAccessible());
        parameters.put("contact_name", group.getContactName());
        parameters.put("contact_email", group.getContactEmail());
        parameters.put("contact_phone", group.getContactPhone());
        parameters.put("current_size", group.getCurrentSize());
        parameters.put("max_size", group.getMaxSize());
        parameters.put("image_url", group.getImageUrl());
        
        Number newId = jdbcInsert.executeAndReturnKey(parameters);
        group.setId(newId.longValue());
        return group;
    }
    
    private SmallGroup update(SmallGroup group) {
        String sql = """
            UPDATE small_groups SET
                name = :name,
                church_name = :churchName,
                denomination = :denomination,
                location = :location,
                address = :address,
                latitude = :latitude,
                longitude = :longitude,
                description = :description,
                age_group = :ageGroup,
                gender = :gender,
                type = :type,
                meeting_day = :meetingDay,
                meeting_time = :meetingTime,
                childcare_included = :childcareIncluded,
                handicap_accessible = :handicapAccessible,
                contact_name = :contactName,
                contact_email = :contactEmail,
                contact_phone = :contactPhone,
                current_size = :currentSize,
                max_size = :maxSize,
                image_url = :imageUrl
            WHERE id = :id
            """;
        
        jdbcClient.sql(sql)
                .param("id", group.getId())
                .param("name", group.getName())
                .param("churchName", group.getChurchName())
                .param("denomination", group.getDenomination())
                .param("location", group.getLocation())
                .param("address", group.getAddress())
                .param("latitude", group.getLatitude())
                .param("longitude", group.getLongitude())
                .param("description", group.getDescription())
                .param("ageGroup", group.getAgeGroup())
                .param("gender", group.getGender())
                .param("type", group.getType())
                .param("meetingDay", group.getMeetingDay())
                .param("meetingTime", group.getMeetingTime())
                .param("childcareIncluded", group.getChildcareIncluded())
                .param("handicapAccessible", group.getHandicapAccessible())
                .param("contactName", group.getContactName())
                .param("contactEmail", group.getContactEmail())
                .param("contactPhone", group.getContactPhone())
                .param("currentSize", group.getCurrentSize())
                .param("maxSize", group.getMaxSize())
                .param("imageUrl", group.getImageUrl())
                .update();
        
        return group;
    }
    
    public void deleteById(Long id) {
        String sql = "DELETE FROM small_groups WHERE id = :id";
        jdbcClient.sql(sql)
                .param("id", id)
                .update();
    }
    
    public List<SmallGroup> findByChurchNameContainingIgnoreCase(String churchName) {
        String sql = "SELECT * FROM small_groups WHERE LOWER(church_name) LIKE LOWER(:churchName)";
        return jdbcClient.sql(sql)
                .param("churchName", "%" + churchName + "%")
                .query(this::mapRowToSmallGroup)
                .list();
    }
    
    public List<SmallGroup> findByDenominationIgnoreCase(String denomination) {
        String sql = "SELECT * FROM small_groups WHERE LOWER(denomination) = LOWER(:denomination)";
        return jdbcClient.sql(sql)
                .param("denomination", denomination)
                .query(this::mapRowToSmallGroup)
                .list();
    }
    
    public List<SmallGroup> findByAgeGroup(String ageGroup) {
        String sql = "SELECT * FROM small_groups WHERE age_group = :ageGroup";
        return jdbcClient.sql(sql)
                .param("ageGroup", ageGroup)
                .query(this::mapRowToSmallGroup)
                .list();
    }
    
    public List<SmallGroup> findByGender(String gender) {
        String sql = "SELECT * FROM small_groups WHERE gender = :gender";
        return jdbcClient.sql(sql)
                .param("gender", gender)
                .query(this::mapRowToSmallGroup)
                .list();
    }
    
    public List<SmallGroup> findByType(String type) {
        String sql = "SELECT * FROM small_groups WHERE type = :type";
        return jdbcClient.sql(sql)
                .param("type", type)
                .query(this::mapRowToSmallGroup)
                .list();
    }
    
    public List<SmallGroup> findByChildcareIncluded(Boolean childcareIncluded) {
        String sql = "SELECT * FROM small_groups WHERE childcare_included = :childcareIncluded";
        return jdbcClient.sql(sql)
                .param("childcareIncluded", childcareIncluded)
                .query(this::mapRowToSmallGroup)
                .list();
    }
    
    public List<SmallGroup> findByHandicapAccessible(Boolean handicapAccessible) {
        String sql = "SELECT * FROM small_groups WHERE handicap_accessible = :handicapAccessible";
        return jdbcClient.sql(sql)
                .param("handicapAccessible", handicapAccessible)
                .query(this::mapRowToSmallGroup)
                .list();
    }
    
    public List<SmallGroup> searchGroups(String churchName, String denomination,
                                          String ageGroup, String gender, String type,
                                          String location, Boolean childcareIncluded,
                                          Boolean handicapAccessible) {
        StringBuilder sql = new StringBuilder("SELECT * FROM small_groups WHERE 1=1");
        Map<String, Object> params = new HashMap<>();
        
        if (churchName != null && !churchName.isEmpty()) {
            sql.append(" AND LOWER(church_name) LIKE LOWER(:churchName)");
            params.put("churchName", "%" + churchName + "%");
        }
        if (denomination != null && !denomination.isEmpty()) {
            sql.append(" AND LOWER(denomination) = LOWER(:denomination)");
            params.put("denomination", denomination);
        }
        if (ageGroup != null && !ageGroup.isEmpty()) {
            sql.append(" AND age_group = :ageGroup");
            params.put("ageGroup", ageGroup);
        }
        if (gender != null && !gender.isEmpty()) {
            sql.append(" AND gender = :gender");
            params.put("gender", gender);
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND type = :type");
            params.put("type", type);
        }
        if (location != null && !location.isEmpty()) {
            sql.append(" AND LOWER(location) LIKE LOWER(:location)");
            params.put("location", "%" + location + "%");
        }
        if (childcareIncluded != null) {
            sql.append(" AND childcare_included = :childcareIncluded");
            params.put("childcareIncluded", childcareIncluded);
        }
        if (handicapAccessible != null) {
            sql.append(" AND handicap_accessible = :handicapAccessible");
            params.put("handicapAccessible", handicapAccessible);
        }
        
        return jdbcClient.sql(sql.toString())
                .params(params)
                .query(this::mapRowToSmallGroup)
                .list();
    }
    
    private SmallGroup mapRowToSmallGroup(ResultSet rs, int rowNum) throws SQLException {
        SmallGroup group = new SmallGroup();
        group.setId(rs.getLong("id"));
        group.setName(rs.getString("name"));
        group.setChurchName(rs.getString("church_name"));
        group.setDenomination(rs.getString("denomination"));
        group.setLocation(rs.getString("location"));
        group.setAddress(rs.getString("address"));
        
        Double latitude = rs.getObject("latitude", Double.class);
        group.setLatitude(latitude);
        
        Double longitude = rs.getObject("longitude", Double.class);
        group.setLongitude(longitude);
        
        group.setDescription(rs.getString("description"));
        group.setAgeGroup(rs.getString("age_group"));
        group.setGender(rs.getString("gender"));
        group.setType(rs.getString("type"));
        group.setMeetingDay(rs.getString("meeting_day"));
        group.setMeetingTime(rs.getString("meeting_time"));
        group.setChildcareIncluded(rs.getBoolean("childcare_included"));
        group.setHandicapAccessible(rs.getBoolean("handicap_accessible"));
        group.setContactName(rs.getString("contact_name"));
        group.setContactEmail(rs.getString("contact_email"));
        group.setContactPhone(rs.getString("contact_phone"));
        
        Integer currentSize = rs.getObject("current_size", Integer.class);
        group.setCurrentSize(currentSize);
        
        Integer maxSize = rs.getObject("max_size", Integer.class);
        group.setMaxSize(maxSize);
        
        group.setImageUrl(rs.getString("image_url"));
        
        return group;
    }
}
