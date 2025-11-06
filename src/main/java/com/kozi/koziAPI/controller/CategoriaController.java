package com.kozi.koziAPI.controller;

import com.kozi.koziAPI.model.Categoria;
import com.kozi.koziAPI.repository.CategoriaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;          // 👈 importa @NonNull de Spring
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // LISTAR TODAS
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return ResponseEntity.ok(categorias);
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtener(@PathVariable int id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // CREAR
    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody @NonNull Categoria categoria) { // 👈 marca no nulo
        Categoria guardada = categoriaRepository.save(categoria);
        return ResponseEntity.ok(guardada);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable int id,
                                                @RequestBody @NonNull Categoria cambios) { // 👈 marca no nulo
        Optional<Categoria> existente = categoriaRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Categoria c = existente.get();
        if (cambios.getNombre() != null) c.setNombre(cambios.getNombre());
        if (cambios.getDescripcion() != null) c.setDescripcion(cambios.getDescripcion());
        Categoria guardada = categoriaRepository.save(c);
        return ResponseEntity.ok(guardada);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (!categoriaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
