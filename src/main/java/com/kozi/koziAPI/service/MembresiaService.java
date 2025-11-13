package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Membresia;
import com.kozi.koziAPI.repository.MembresiaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class MembresiaService {
    
    @Autowired
    private MembresiaRepository membresiaRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<Membresia> findAll() {
        return membresiaRepository.findAll();
    }

    public Membresia findById(Long id) {
        Membresia membresia = membresiaRepository.findById(id).orElse(null);
        return membresia;
    }

    public Membresia save(Membresia membresia) {
        return membresiaRepository.save(membresia);
    }

    public Membresia patchMembresia(Membresia membresia) {
        Membresia existingMembresia = membresiaRepository.findById(membresia.getId()).orElse(null);
        if (existingMembresia != null) {
            if (membresia.getTipoMembresia() != null) {
                existingMembresia.setTipoMembresia(membresia.getTipoMembresia());
            }
            return membresiaRepository.save(existingMembresia);
        }
        return null;
    }

    public void deleteById(Long id) {
        usuarioService.deleteByMembresiaId(id);
        membresiaRepository.deleteById(id);
    }
}
