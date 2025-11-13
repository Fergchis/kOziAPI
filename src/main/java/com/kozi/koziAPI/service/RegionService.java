package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Region;
import com.kozi.koziAPI.repository.RegionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class RegionService {
    
    @Autowired
    private RegionRepository regionRepository;
    
    @Autowired
    private CiudadService ciudadService;

    public List<Region> findAll() {
        return regionRepository.findAll();
    }

    public Region findById(Long id) {
        Region region = regionRepository.findById(id).orElse(null);
        return region;
    }
    
    public Region save(Region region) {
        return regionRepository.save(region);
    }

    public Region patchRegion(Region region) {
        Region existingRegion = regionRepository.findById(region.getId()).orElse(null);
        if (existingRegion != null) {
            if (region.getNombre() != null) {
                existingRegion.setNombre(region.getNombre());
            }
            return regionRepository.save(existingRegion);
        }
        return null;
    }

    public void deleteById(Long id) {
        ciudadService.deleteByRegionId(id);
        regionRepository.deleteById(id);
    }
}
