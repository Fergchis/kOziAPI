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

import com.kozi.koziAPI.model.Categorias;
import com.kozi.koziAPI.service.CategoriasService;

@RestController
@RequestMapping("/api/categoriass")
public class CategoriasController {
    
    @Autowired
    private CategoriasService categoriasService;

    @GetMapping
    public ResponseEntity<List<Categorias>> getAllCategoriass() {
        List<Categorias> categoriass = categoriasService.findAll();
        if (categoriass.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categoriass);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categorias> getCategoriasById(@PathVariable Long id) {
        Categorias categorias = categoriasService.findById(id);
        if (categorias == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(categorias);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Categorias> createCategorias(@RequestBody Categorias categorias) {
        Categorias createdCategorias = categoriasService.save(categorias);
        return ResponseEntity.status(201).body(createdCategorias);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategorias(@PathVariable Long id) {
        categoriasService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
