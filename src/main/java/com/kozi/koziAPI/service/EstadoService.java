package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Estado;
import com.kozi.koziAPI.repository.EstadoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class EstadoService {
    
    @Autowired
    private EstadoRepository estadoRepository;

    public List<Estado> findAll() {
        return estadoRepository.findAll();
    }

    public Estado findById(Long id) {
        Estado estado = estadoRepository.findById(id).orElse(null);
        return estado;
    }

    public Estado save(Estado estado) {
        return estadoRepository.save(estado);
    }

    public Estado patchEstado(Estado estado) {
        Estado existingEstado = estadoRepository.findById(estado.getId()).orElse(null);
        if (existingEstado != null) {
            if (estado.getTipoEstado() != null) {
                existingEstado.setTipoEstado(estado.getTipoEstado());
            }
            return estadoRepository.save(existingEstado);
        }
        return null;
    }

    public void deleteById(Long id) {
        estadoRepository.deleteById(id);
    }
}
