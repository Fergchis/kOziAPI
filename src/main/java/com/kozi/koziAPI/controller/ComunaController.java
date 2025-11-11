package com.kozi.koziAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kozi.koziAPI.model.Comuna;
import com.kozi.koziAPI.service.ComunaService;

@RestController
@RequestMapping("/api/comunas")
public class ComunaController {
    
    @Autowired
    private ComunaService comunaService;

    @GetMapping
    public ResponseEntity<List<Comuna>> getAllComunas() {
        List<Comuna> comunas = comunaService.findAll();
        if (comunas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comunas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comuna> getComunaById(@PathVariable Long id) {
        Comuna comuna = comunaService.findById(id);
        if (comuna == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(comuna);
    }

    @PostMapping
    public ResponseEntity<Comuna> createComuna(@RequestBody Comuna comuna) {
        Comuna createdComuna = comunaService.save(comuna);
        return ResponseEntity.status(201).body(createdComuna);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comuna> updateComuna(@PathVariable Long id, @RequestBody Comuna comuna) {
        comuna.setId(id);
        Comuna updatedComuna = comunaService.save(comuna);
        if (updatedComuna == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedComuna);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Comuna> patchComuna(@PathVariable Long id, @RequestBody Comuna comuna) {
        comuna.setId(id);
        Comuna patchedComuna = comunaService.patchComuna(comuna);
        if (patchedComuna == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(patchedComuna);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComuna(@PathVariable Long id) {
        comunaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
