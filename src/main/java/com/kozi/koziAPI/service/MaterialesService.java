package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Materiales;
import com.kozi.koziAPI.repository.MaterialesRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class MaterialesService {

    @Autowired
    private MaterialesRepository materialesRepository;

    public List<Materiales> findAll() {
        return materialesRepository.findAll();
    }

    public Materiales findById(Long id) {
        Materiales materiales = materialesRepository.findById(id).orElse(null);
        return materiales;
    }

    public Materiales save(Materiales materiales) {
        return materialesRepository.save(materiales);
    }

    public void deleteById(Long id) {
        materialesRepository.deleteById(id);
    }

    public void deleteByProductoId(Long productoId) {
        List<Materiales> materialess = materialesRepository.findAll();
        for (Materiales materiales : materialess) {
            if (materiales.getProducto() != null && materiales.getProducto().getId().equals(productoId)){
                materialesRepository.deleteById(materiales.getId());
            }
        }
    }

    public void deleteByMaterialId(Long materialId) {
        List<Materiales> materialess = materialesRepository.findAll();
        for (Materiales materiales : materialess) {
            if (materiales.getMaterial() != null && materiales.getMaterial().getId().equals(materialId)){
                materialesRepository.deleteById(materiales.getId());
            }
        }
    }
}