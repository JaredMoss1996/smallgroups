package com.smallgroups.service;

import com.smallgroups.model.Church;
import com.smallgroups.repository.ChurchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChurchService {
    
    private final ChurchRepository churchRepository;
    
    public ChurchService(ChurchRepository churchRepository) {
        this.churchRepository = churchRepository;
    }
    
    public List<Church> getAllChurches() {
        return churchRepository.findAll();
    }
    
    public Optional<Church> getChurchById(Long id) {
        return churchRepository.findById(id);
    }
    
    public Church saveChurch(Church church) {
        return churchRepository.save(church);
    }
}
