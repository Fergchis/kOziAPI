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

import com.kozi.koziAPI.model.Estilo;
import com.kozi.koziAPI.service.EstiloService;

@RestController
@RequestMapping("/api/estilos")
public class EstiloController {
    
    @Autowired
    private EstiloService estiloService;

    @GetMapping
    public ResponseEntity<List<Estilo>> getAllEstilos() {
        List<Estilo> estilos = estiloService.findAll();
        if (estilos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estilos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estilo> getEstiloById(@PathVariable Long id) {
        Estilo estilo = estiloService.findById(id);
        if (estilo == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(estilo);
    }

    @PostMapping
    public ResponseEntity<Estilo> createEstilo(@RequestBody Estilo estilo) {
        Estilo createdEstilo = estiloService.save(estilo);
        return ResponseEntity.status(201).body(createdEstilo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estilo> updateEstilo(@PathVariable Long id, @RequestBody Estilo estilo) {
        estilo.setId(id);
        Estilo updatedEstilo = estiloService.save(estilo);
        if (updatedEstilo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEstilo);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Estilo> patchEstilo(@PathVariable Long id, @RequestBody Estilo estilo) {
        estilo.setId(id);
        Estilo patchedEstilo = estiloService.patchEstilo(estilo);
        if (patchedEstilo == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(patchedEstilo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstilo(@PathVariable Long id) {
        estiloService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
