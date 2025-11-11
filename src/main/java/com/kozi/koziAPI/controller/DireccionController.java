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

import com.kozi.koziAPI.model.Direccion;
import com.kozi.koziAPI.service.DireccionService;

@RestController
@RequestMapping("/api/direcciones")
public class DireccionController {
    
    @Autowired
    private DireccionService direccionService;

    @GetMapping
    public ResponseEntity<List<Direccion>> getAllDirecciones() {
        List<Direccion> direcciones = direccionService.findAll();
        if (direcciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(direcciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Direccion> getDireccionById(@PathVariable Long id) {
        Direccion direccion = direccionService.findById(id);
        if (direccion == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(direccion);
    }

    @PostMapping
    public ResponseEntity<Direccion> createDireccion(@RequestBody Direccion direccion) {
        Direccion createdDireccion = direccionService.save(direccion);
        return ResponseEntity.status(201).body(createdDireccion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Direccion> updateDireccion(@PathVariable Long id, @RequestBody Direccion direccion) {
        direccion.setId(id);
        Direccion updatedDireccion = direccionService.save(direccion);
        if (updatedDireccion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedDireccion);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Direccion> patchDireccion(@PathVariable Long id, @RequestBody Direccion direccion) {
        direccion.setId(id);
        Direccion patchedDireccion = direccionService.patchDireccion(direccion);
        if (patchedDireccion == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(patchedDireccion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDireccion(@PathVariable Long id) {
        direccionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
