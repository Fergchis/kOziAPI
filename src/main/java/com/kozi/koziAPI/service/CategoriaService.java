package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Categoria;
import com.kozi.koziAPI.repository.CategoriaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria findById(Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        return categoria;
    }

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria patchCategoria(Categoria categoria) {
        Categoria existingCategoria = categoriaRepository.findById(categoria.getId()).orElse(null);
        if (existingCategoria != null) {
            if (categoria.getNombre() != null) {
                existingCategoria.setNombre(categoria.getNombre());
            }
            return categoriaRepository.save(existingCategoria);
        }
        return null;
    }

    public void deleteById(Long id) {
        categoriaRepository.deleteById(id);
    }
}
