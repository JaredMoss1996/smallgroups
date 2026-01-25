package com.smallgroups.controller;

import com.smallgroups.model.Church;
import com.smallgroups.service.ChurchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/churches")
public class ChurchApiController {
    
    private final ChurchService churchService;
    
    public ChurchApiController(ChurchService churchService) {
        this.churchService = churchService;
    }
    
    @GetMapping
    public ResponseEntity<List<Church>> getAllChurches() {
        return ResponseEntity.ok(churchService.getAllChurches());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Church> getChurchById(@PathVariable Long id) {
        return churchService.getChurchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
