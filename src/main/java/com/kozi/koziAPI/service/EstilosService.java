package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Estilos;
import com.kozi.koziAPI.repository.EstilosRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class EstilosService {

    @Autowired
    private EstilosRepository estilosRepository;

    public List<Estilos> findAll() {
        return estilosRepository.findAll();
    }

    public Estilos findById(Long id) {
        Estilos estilos = estilosRepository.findById(id).orElse(null);
        return estilos;
    }

    public Estilos save(Estilos estilos) {
        return estilosRepository.save(estilos);
    }

    public void deleteById(Long id) {
        estilosRepository.deleteById(id);
    }

    public void deleteByProductoId(Long productoId) {
        List<Estilos> estiloss = estilosRepository.findAll();
        for (Estilos estilos : estiloss) {
            if (estilos.getProducto() != null && estilos.getProducto().getId().equals(productoId)){
                estilosRepository.deleteById(estilos.getId());
            }
        }
    }

    public void deleteByEstiloId(Long estiloId) {
        List<Estilos> estiloss = estilosRepository.findAll();
        for (Estilos estilos : estiloss) {
            if (estilos.getEstilo() != null && estilos.getEstilo().getId().equals(estiloId)){
                estilosRepository.deleteById(estilos.getId());
            }
        }
    }    
}