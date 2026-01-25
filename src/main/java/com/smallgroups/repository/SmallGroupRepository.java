package com.smallgroups.repository;

import com.smallgroups.model.SmallGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmallGroupRepository extends JpaRepository<SmallGroup, Long> {
    
    List<SmallGroup> findByChurchNameContainingIgnoreCase(String churchName);
    
    List<SmallGroup> findByDenominationIgnoreCase(String denomination);
    
    List<SmallGroup> findByAgeGroup(String ageGroup);
    
    List<SmallGroup> findByGender(String gender);
    
    List<SmallGroup> findByType(String type);
    
    List<SmallGroup> findByChildcareIncluded(Boolean childcareIncluded);
    
    List<SmallGroup> findByHandicapAccessible(Boolean handicapAccessible);
    
    @Query("SELECT g FROM SmallGroup g WHERE " +
           "(:churchName IS NULL OR LOWER(g.churchName) LIKE LOWER(CONCAT('%', :churchName, '%'))) AND " +
           "(:denomination IS NULL OR LOWER(g.denomination) = LOWER(:denomination)) AND " +
           "(:ageGroup IS NULL OR g.ageGroup = :ageGroup) AND " +
           "(:gender IS NULL OR g.gender = :gender) AND " +
           "(:type IS NULL OR g.type = :type) AND " +
           "(:location IS NULL OR LOWER(g.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:childcareIncluded IS NULL OR g.childcareIncluded = :childcareIncluded) AND " +
           "(:handicapAccessible IS NULL OR g.handicapAccessible = :handicapAccessible)")
    List<SmallGroup> searchGroups(
            @Param("churchName") String churchName,
            @Param("denomination") String denomination,
            @Param("ageGroup") String ageGroup,
            @Param("gender") String gender,
            @Param("type") String type,
            @Param("location") String location,
            @Param("childcareIncluded") Boolean childcareIncluded,
            @Param("handicapAccessible") Boolean handicapAccessible
    );
}
