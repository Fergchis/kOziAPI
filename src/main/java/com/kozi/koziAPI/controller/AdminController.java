package com.kozi.koziAPI.controller;

import com.kozi.koziAPI.model.Categoria;
import com.kozi.koziAPI.model.Producto;
import com.kozi.koziAPI.repository.CategoriaRepository;
import com.kozi.koziAPI.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public AdminController(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // CREAR PRODUCTO
    @PostMapping("/productos")
    public ResponseEntity<?> crearProducto(@RequestBody Producto body) {
        // Se espera que venga body.categoria.id
        if (body.getCategoria() == null || body.getCategoria().getId() == null) {
            return ResponseEntity.badRequest().body("Debe enviar categoria.id");
        }
        Optional<Categoria> catOpt = categoriaRepository.findById(body.getCategoria().getId());
        if (catOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Categoria no existe");
        }

        Producto p = new Producto();
        p.setNombre(body.getNombre());
        p.setPrecio(body.getPrecio());
        p.setImagenUrl(body.getImagenUrl());
        p.setStock(body.getStock() != null ? body.getStock() : 0);
        p.setCategoria(catOpt.get());

        Producto guardado = productoRepository.save(p);
        return ResponseEntity.ok(guardado);
    }

    // ACTUALIZAR PRODUCTO
    @PutMapping("/productos/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable int id, @RequestBody Producto body) {
        Optional<Producto> existente = productoRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Producto p = existente.get();

        if (body.getNombre() != null) p.setNombre(body.getNombre());
        if (body.getPrecio() != null) p.setPrecio(body.getPrecio());
        if (body.getImagenUrl() != null) p.setImagenUrl(body.getImagenUrl());
        if (body.getStock() != null) p.setStock(body.getStock());

        // Cambiar categoría si viene
        if (body.getCategoria() != null && body.getCategoria().getId() != null) {
            Optional<Categoria> catOpt = categoriaRepository.findById(body.getCategoria().getId());
            if (catOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Categoria no existe");
            }
            p.setCategoria(catOpt.get());
        }

        Producto guardado = productoRepository.save(p);
        return ResponseEntity.ok(guardado);
    }

    // ELIMINAR PRODUCTO
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable int id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // (Opcional) LISTAR ÓRDENES ADMIN – lo implementamos cuando creemos la lógica de órdenes
    // @GetMapping("/ordenes")
    // public ResponseEntity<List<Orden>> listarOrdenesAdmin() { ... }
}
