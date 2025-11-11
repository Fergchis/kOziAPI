package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Ciudad;
import com.kozi.koziAPI.repository.CiudadRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class CiudadService {
    
    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private ComunaService comunaService;

    public List<Ciudad> findAll() {
        return ciudadRepository.findAll();
    }

    public Ciudad findById(Long id) {
        Ciudad ciudad = ciudadRepository.findById(id).orElse(null);
        return ciudad;
    }

    public Ciudad save(Ciudad ciudad) {
        return ciudadRepository.save(ciudad);
    }

    public Ciudad patchCiudad(Ciudad ciudad) {
        Ciudad existingCiudad = ciudadRepository.findById(ciudad.getId()).orElse(null);
        if (existingCiudad != null) {
            if (ciudad.getNombre() != null) {
                existingCiudad.setNombre(ciudad.getNombre());
            }
            return ciudadRepository.save(existingCiudad);
        }
        return null;
    }

    public void deleteById(Long id) {
        comunaService.deleteByCiudadId(id);
        ciudadRepository.deleteById(id);
    }

    public void deleteByRegionId(Long regionId) {
        List<Ciudad> ciudades = ciudadRepository.findAll();
        for (Ciudad ciudad : ciudades) {
            if (ciudad.getRegion() != null && ciudad.getRegion().getId().equals(regionId)) {
                ciudadRepository.deleteById(ciudad.getId());
            }
        }
    }
}
