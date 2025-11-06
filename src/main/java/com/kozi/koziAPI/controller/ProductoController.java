package com.kozi.koziAPI.controller;

import com.kozi.koziAPI.model.Categoria;
import com.kozi.koziAPI.model.Producto;
import com.kozi.koziAPI.repository.CategoriaRepository;
import com.kozi.koziAPI.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoController(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // LISTAR (opcional: ?categoriaId=123)
    @GetMapping
    public ResponseEntity<List<Producto>> listar(@RequestParam(required = false) Integer categoriaId) {
        if (categoriaId != null) {
            Optional<Categoria> cat = categoriaRepository.findById(categoriaId);
            if (cat.isEmpty()) {
                return ResponseEntity.ok(List.of());
            }
            Categoria categoria = cat.get();
            return ResponseEntity.ok(productoRepository.findByCategoria(categoria));
        }
        return ResponseEntity.ok(productoRepository.findAll());
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable int id) {
        Optional<Producto> prod = productoRepository.findById(id);
        return prod.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
