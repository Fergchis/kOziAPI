package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Color;
import com.kozi.koziAPI.repository.ColorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class ColorService {
    
    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ColoresService coloresService;

    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    public Color findById(Long id) {
        Color color = colorRepository.findById(id).orElse(null);
        return color;
    }

    public Color save(Color color) {
        return colorRepository.save(color);
    }
    
    public Color patchColor(Color color) {
        Color existingColor = colorRepository.findById(color.getId()).orElse(null);
        if (existingColor != null) {
            if (color.getNombre() != null) {
                existingColor.setNombre(color.getNombre());
            }
            return colorRepository.save(existingColor);
        }
        return null;
    }

    public void deleteById(Long id) {
        coloresService.deleteByColorId(id);
        colorRepository.deleteById(id);
    }
}
