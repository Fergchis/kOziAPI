package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Rol;
import com.kozi.koziAPI.repository.RolRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class RolService {
    
    @Autowired
    private RolRepository rolRepository;

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    public Rol findById(Long id) {
        Rol rol = rolRepository.findById(id).orElse(null);
        return rol;
    }

    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    public Rol patchRol(Rol rol) {
        Rol existingRol = rolRepository.findById(rol.getId()).orElse(null);
        if (existingRol != null) {
            if (rol.getNombreRol() != null) {
                existingRol.setNombreRol(rol.getNombreRol());
            }
            return rolRepository.save(existingRol);
        }
        return null;
    }

    public void deleteById(Long id) {
        rolRepository.deleteById(id);
    }
}
