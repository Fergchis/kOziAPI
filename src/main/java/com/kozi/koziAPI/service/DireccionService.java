package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Direccion;
import com.kozi.koziAPI.repository.DireccionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class DireccionService {

    @Autowired
    private DireccionRepository direccionRepository;

    public List<Direccion> findAll() {
        return direccionRepository.findAll();
    }
    
    public Direccion findById(Long id) {
        Direccion direccion = direccionRepository.findById(id).orElse(null);
        return direccion;
    }

    public Direccion save(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    public Direccion patchDireccion(Direccion direccion) {
        Direccion existingDireccion = direccionRepository.findById(direccion.getId()).orElse(null);
        if (existingDireccion != null) {
            if (direccion.getNombreCalle() != null) {
                existingDireccion.setNombreCalle(direccion.getNombreCalle());
            }
            if (direccion.getNumeroCalle() != null) {
                existingDireccion.setNumeroCalle(direccion.getNumeroCalle());
            }
            return direccionRepository.save(existingDireccion);
        }
        return null;
    }

    public void deleteById(Long id) {
        direccionRepository.deleteById(id);
    }

    public void deleteByComunaId(Long comunaId) {
        List<Direccion> direcciones = direccionRepository.findAll();
        for (Direccion direccion : direcciones) {
            if (direccion.getComuna() != null && direccion.getComuna().getId().equals(comunaId)) {
                direccionRepository.deleteById(direccion.getId());
            }
        }
    }
}
