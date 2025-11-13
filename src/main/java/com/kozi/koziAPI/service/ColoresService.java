package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Colores;
import com.kozi.koziAPI.repository.ColoresRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class ColoresService {
    
    @Autowired
    private ColoresRepository coloresRepository;

    public List<Colores> findAll() {
        return coloresRepository.findAll();
    }

    public Colores findById(Long id) {
        Colores colores = coloresRepository.findById(id).orElse(null);
        return colores;
    }

    public Colores save(Colores colores) {
        return coloresRepository.save(colores);
    }

    public void deleteById(Long id) {
        coloresRepository.deleteById(id);
    }

    public void deleteByProductoId(Long productoId) {
        List<Colores> coloress = coloresRepository.findAll();
        for (Colores colores : coloress) {
            if (colores.getProducto() != null && colores.getProducto().getId().equals(productoId)){
                coloresRepository.deleteById(colores.getId());
            }
        }
    }

    public void deleteByColorId(Long colorId) {
        List<Colores> coloress = coloresRepository.findAll();
        for (Colores colores : coloress) {
            if (colores.getColor() != null && colores.getColor().getId().equals(colorId)){
                coloresRepository.deleteById(colores.getId());
            }
        }
    }
}
