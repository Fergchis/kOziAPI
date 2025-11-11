package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Estilo;
import com.kozi.koziAPI.repository.EstiloRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class EstiloService {
    
    @Autowired
    private EstiloRepository estiloRepository;

    public List<Estilo> findAll() {
        return estiloRepository.findAll();
    }

    public Estilo findById(Long id) {
        Estilo estilo = estiloRepository.findById(id).orElse(null);
        return estilo;
    }

    public Estilo save(Estilo estilo) {
        return estiloRepository.save(estilo);
    }

    public Estilo patchEstilo(Estilo estilo) {
        Estilo existingEstilo = estiloRepository.findById(estilo.getId()).orElse(null);
        if (existingEstilo != null) {
            if (estilo.getNombre() != null) {
                existingEstilo.setNombre(estilo.getNombre());
            }
            return estiloRepository.save(existingEstilo);
        }
        return null;
    }

    public void deleteById(Long id) {
        estiloRepository.deleteById(id);
    }
}
