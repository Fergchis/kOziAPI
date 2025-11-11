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

import com.kozi.koziAPI.model.Color;
import com.kozi.koziAPI.service.ColorService;

@RestController
@RequestMapping("/api/colores")
public class ColorController {

    @Autowired
    private ColorService colorService;

    @GetMapping
    public ResponseEntity<List<Color>> getAllColores() {
        List<Color> colores = colorService.findAll();
        if (colores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(colores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Color> getColorById(@PathVariable Long id) {
        Color color = colorService.findById(id);
        if (color == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(color);
    }

    @PostMapping
    public ResponseEntity<Color> createColor(@RequestBody Color color) {
        Color createdColor = colorService.save(color);
        return ResponseEntity.status(201).body(createdColor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Color> updateColor(@PathVariable Long id, @RequestBody Color color) {
        color.setId(id);
        Color updatedColor = colorService.save(color);
        if (updatedColor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedColor);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Color> patchColor(@PathVariable Long id, @RequestBody Color color) {
        color.setId(id);
        Color patchedColor = colorService.patchColor(color);
        if (patchedColor == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(patchedColor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColor(@PathVariable Long id) {
        colorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
