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

import com.kozi.koziAPI.model.Ciudad;
import com.kozi.koziAPI.service.CiudadService;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadController {
    
    @Autowired
    private CiudadService ciudadService;

    @GetMapping
    public ResponseEntity<List<Ciudad>> getAllCiudades() {
        List<Ciudad> ciudades = ciudadService.findAll();
        if (ciudades.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ciudades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ciudad> getCiudadById(@PathVariable Long id) {
        Ciudad ciudad = ciudadService.findById(id);
        if (ciudad == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(ciudad);
    }

    @PostMapping
    public ResponseEntity<Ciudad> createCiudad(@RequestBody Ciudad ciudad) {
        Ciudad createdCiudad = ciudadService.save(ciudad);
        return ResponseEntity.status(201).body(createdCiudad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ciudad> updateCiudad(@PathVariable Long id, @RequestBody Ciudad ciudad) {
        ciudad.setId(id);
        Ciudad updatedCiudad = ciudadService.save(ciudad);
        if (updatedCiudad == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCiudad);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ciudad> patchCiudad(@PathVariable Long id, @RequestBody Ciudad ciudad) {
        ciudad.setId(id);
        Ciudad patchedCiudad = ciudadService.patchCiudad(ciudad);
        if (patchedCiudad == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(patchedCiudad);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCiudad(@PathVariable Long id) {
        ciudadService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
