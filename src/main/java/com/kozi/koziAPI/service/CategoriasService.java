package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Categorias;
import com.kozi.koziAPI.repository.CategoriasRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class CategoriasService {

    @Autowired
    private CategoriasRepository categoriasRepository;

    public List<Categorias> findAll() {
        return categoriasRepository.findAll();
    }

    public Categorias findById(Long id) {
        Categorias categorias = categoriasRepository.findById(id).orElse(null);
        return categorias;
    }

    public Categorias save(Categorias categorias) {
        return categoriasRepository.save(categorias);
    }

    public void deleteById(Long id) {
        categoriasRepository.deleteById(id);
    }

    public void deleteByProductoId(Long productoId) {
        List<Categorias> categoriass = categoriasRepository.findAll();
        for (Categorias categorias : categoriass) {
            if (categorias.getProducto() != null && categorias.getProducto().getId().equals(productoId)){
                categoriasRepository.deleteById(categorias.getId());
            }
        }
    }

    public void deleteByCategoriaId(Long categoriaId) {
        List<Categorias> categoriass = categoriasRepository.findAll();
        for (Categorias categorias : categoriass) {
            if (categorias.getCategoria() != null && categorias.getCategoria().getId().equals(categoriaId)){
                categoriasRepository.deleteById(categorias.getId());
            }
        }
    }
}