package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Comuna;
import com.kozi.koziAPI.repository.ComunaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private DireccionService direccionService;

    public List<Comuna> findAll() {
        return comunaRepository.findAll();
    }

    public Comuna findById(Long id) {
        Comuna comuna = comunaRepository.findById(id).orElse(null);
        return comuna;
    }  

    public Comuna save(Comuna comuna) {
        return comunaRepository.save(comuna);
    }

    public Comuna patchComuna(Comuna comuna) {
        Comuna existingComuna = comunaRepository.findById(comuna.getId()).orElse(null);
        if (existingComuna != null) {
            if (comuna.getNombre() != null) {
                existingComuna.setNombre(comuna.getNombre());
            }
            return comunaRepository.save(existingComuna);
        }
        return null;
    }

    public void deleteById(Long id) {
        direccionService.deleteByComunaId(id);
        comunaRepository.deleteById(id);
    }

    public void deleteByCiudadId(Long ciudadId) {
        List<Comuna> comunas = comunaRepository.findAll();
        for (Comuna comuna : comunas) {
            if (comuna.getCiudad() != null && comuna.getCiudad().getId().equals(ciudadId)) {
                comunaRepository.deleteById(comuna.getId());
            }
        }
    }  
}
