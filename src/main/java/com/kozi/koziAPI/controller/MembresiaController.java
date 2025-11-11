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

import com.kozi.koziAPI.model.Membresia;
import com.kozi.koziAPI.service.MembresiaService;

@RestController
@RequestMapping("/api/membresias")
public class MembresiaController {
    
    @Autowired
    private MembresiaService membresiaService;

    @GetMapping
    public ResponseEntity<List<Membresia>> getAllMembresias() {
        List<Membresia> membresias = membresiaService.findAll();
        if (membresias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(membresias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Membresia> getMembresiaById(@PathVariable Long id) {
        Membresia membresia = membresiaService.findById(id);
        if (membresia == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(membresia);
    }

    @PostMapping
    public ResponseEntity<Membresia> createMembresia(@RequestBody Membresia membresia) {
        Membresia createdMembresia = membresiaService.save(membresia);
        return ResponseEntity.status(201).body(createdMembresia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Membresia> updateMembresia(@PathVariable Long id, @RequestBody Membresia membresia) {
        membresia.setId(id);
        Membresia updatedMembresia = membresiaService.save(membresia);
        if (updatedMembresia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedMembresia);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Membresia> patchMembresia(@PathVariable Long id, @RequestBody Membresia membresia) {
        membresia.setId(id);
        Membresia patchedMembresia = membresiaService.patchMembresia(membresia);
        if (patchedMembresia == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(patchedMembresia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembresia(@PathVariable Long id) {
        membresiaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
