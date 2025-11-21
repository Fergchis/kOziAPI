package com.kozi.koziAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kozi.koziAPI.model.Colores;
import com.kozi.koziAPI.service.ColoresService;

@RestController
@RequestMapping("/api/coloress")
public class ColoresController {
    
    @Autowired
    private ColoresService coloresService;

    @GetMapping
    public ResponseEntity<List<Colores>> getAllColoress() {
        List<Colores> coloress = coloresService.findAll();
        if (coloress.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(coloress);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Colores> getColoresById(@PathVariable Long id) {
        Colores colores = coloresService.findById(id);
        if (colores == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(colores);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Colores> createColores(@RequestBody Colores colores) {
        Colores createdColores = coloresService.save(colores);
        return ResponseEntity.status(201).body(createdColores);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColores(@PathVariable Long id) {
        coloresService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
