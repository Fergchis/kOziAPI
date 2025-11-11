package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Material;
import com.kozi.koziAPI.repository.MaterialRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class MaterialService {
    
    @Autowired
    private MaterialRepository materialRepository;
    
    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    public Material findById(Long id) {
        Material material = materialRepository.findById(id).orElse(null);
        return material;
    }

    public Material save(Material material) {
        return materialRepository.save(material);
    }

    public Material patchMaterial(Material material) {
        Material existingMaterial = materialRepository.findById(material.getId()).orElse(null);
        if (existingMaterial != null) {
            if (material.getNombre() != null) {
                existingMaterial.setNombre(material.getNombre());
            }
            return materialRepository.save(existingMaterial);
        }
        return null;
    }

    public void deleteById(Long id) {
        materialRepository.deleteById(id);
    }
}
