package com.smallgroups.service;

import com.smallgroups.model.SmallGroup;
import com.smallgroups.repository.SmallGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SmallGroupService {
    
    @Autowired
    private SmallGroupRepository repository;
    
    public List<SmallGroup> getAllGroups() {
        return repository.findAll();
    }
    
    public Optional<SmallGroup> getGroupById(Long id) {
        return repository.findById(id);
    }
    
    public SmallGroup createGroup(SmallGroup group) {
        return repository.save(group);
    }
    
    public SmallGroup updateGroup(Long id, SmallGroup groupDetails) {
        SmallGroup group = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + id));
        
        group.setName(groupDetails.getName());
        group.setChurchName(groupDetails.getChurchName());
        group.setDenomination(groupDetails.getDenomination());
        group.setLocation(groupDetails.getLocation());
        group.setAddress(groupDetails.getAddress());
        group.setLatitude(groupDetails.getLatitude());
        group.setLongitude(groupDetails.getLongitude());
        group.setDescription(groupDetails.getDescription());
        group.setAgeGroup(groupDetails.getAgeGroup());
        group.setGender(groupDetails.getGender());
        group.setType(groupDetails.getType());
        group.setMeetingDay(groupDetails.getMeetingDay());
        group.setMeetingTime(groupDetails.getMeetingTime());
        group.setChildcareIncluded(groupDetails.getChildcareIncluded());
        group.setHandicapAccessible(groupDetails.getHandicapAccessible());
        group.setContactName(groupDetails.getContactName());
        group.setContactEmail(groupDetails.getContactEmail());
        group.setContactPhone(groupDetails.getContactPhone());
        group.setCurrentSize(groupDetails.getCurrentSize());
        group.setMaxSize(groupDetails.getMaxSize());
        
        return repository.save(group);
    }
    
    public void deleteGroup(Long id) {
        repository.deleteById(id);
    }
    
    public List<SmallGroup> searchGroups(String churchName, String denomination, 
                                          String ageGroup, String gender, String type,
                                          String location, Boolean childcareIncluded, 
                                          Boolean handicapAccessible, Double radius,
                                          Double userLat, Double userLon) {
        List<SmallGroup> groups = repository.searchGroups(
                churchName, denomination, ageGroup, gender, type, location, 
                childcareIncluded, handicapAccessible
        );
        
        // Filter by radius if coordinates are provided
        if (radius != null && userLat != null && userLon != null) {
            groups = groups.stream()
                    .filter(group -> {
                        if (group.getLatitude() == null || group.getLongitude() == null) {
                            return false;
                        }
                        double distance = calculateDistance(userLat, userLon, 
                                group.getLatitude(), group.getLongitude());
                        return distance <= radius;
                    })
                    .collect(Collectors.toList());
        }
        
        return groups;
    }
    
    // Haversine formula to calculate distance between two points
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        
        return distance; // returns distance in km
    }
}
