package com.smallgroups.controller;

import com.smallgroups.model.SmallGroup;
import com.smallgroups.service.SmallGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class SmallGroupApiController {
    
    private final SmallGroupService service;
    
    public SmallGroupApiController(SmallGroupService service) {
        this.service = service;
    }
    
    @GetMapping
    public List<SmallGroup> getAllGroups() {
        return service.getAllGroups();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SmallGroup> getGroupById(@PathVariable Long id) {
        return service.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public SmallGroup createGroup(@RequestBody SmallGroup group) {
        return service.createGroup(group);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SmallGroup> updateGroup(@PathVariable Long id, @RequestBody SmallGroup group) {
        try {
            SmallGroup updatedGroup = service.updateGroup(id, group);
            return ResponseEntity.ok(updatedGroup);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        service.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public List<SmallGroup> searchGroups(
            @RequestParam(required = false) String churchName,
            @RequestParam(required = false) String denomination,
            @RequestParam(required = false) String ageGroup,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean childcareIncluded,
            @RequestParam(required = false) Boolean handicapAccessible,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) Double userLat,
            @RequestParam(required = false) Double userLon
    ) {
        return service.searchGroups(churchName, denomination, ageGroup, gender, type, 
                location, childcareIncluded, handicapAccessible, radius, userLat, userLon);
    }
}
