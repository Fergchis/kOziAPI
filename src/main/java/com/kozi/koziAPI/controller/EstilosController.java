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

import com.kozi.koziAPI.model.Estilos;
import com.kozi.koziAPI.service.EstilosService;

@RestController
@RequestMapping("/api/estiloss")
public class EstilosController {
    
    @Autowired
    private EstilosService estilosService;

    @GetMapping
    public ResponseEntity<List<Estilos>> getAllEstiloss() {
        List<Estilos> estiloss = estilosService.findAll();
        if (estiloss.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estiloss);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estilos> getEstilosById(@PathVariable Long id) {
        Estilos estilos = estilosService.findById(id);
        if (estilos == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(estilos);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Estilos> createEstilos(@RequestBody Estilos estilos) {
        Estilos createdEstilos = estilosService.save(estilos);
        return ResponseEntity.status(201).body(createdEstilos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstilos(@PathVariable Long id) {
        estilosService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
