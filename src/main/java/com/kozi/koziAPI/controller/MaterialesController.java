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

import com.kozi.koziAPI.model.Materiales;
import com.kozi.koziAPI.service.MaterialesService;

@RestController
@RequestMapping("/api/materialess")
public class MaterialesController {
    
    @Autowired
    private MaterialesService materialesService;

    @GetMapping
    public ResponseEntity<List<Materiales>> getAllMaterialess() {
        List<Materiales> materialess = materialesService.findAll();
        if (materialess.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(materialess);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Materiales> getMaterialesById(@PathVariable Long id) {
        Materiales materiales = materialesService.findById(id);
        if (materiales == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(materiales);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Materiales> createMateriales(@RequestBody Materiales materiales) {
        Materiales createdMateriales = materialesService.save(materiales);
        return ResponseEntity.status(201).body(createdMateriales);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMateriales(@PathVariable Long id) {
        materialesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
