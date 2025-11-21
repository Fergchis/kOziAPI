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

import com.kozi.koziAPI.model.ProductosPedido;
import com.kozi.koziAPI.service.ProductosPedidoService;

@RestController
@RequestMapping("/api/productosPedidos")
public class ProductosPedidoController {
    
    @Autowired
    private ProductosPedidoService productosPedidoService;

    @GetMapping
    public ResponseEntity<List<ProductosPedido>> getAllProductosPedidos() {
        List<ProductosPedido> productosPedidos = productosPedidoService.findAll();
        if (productosPedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productosPedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductosPedido> getProductosPedidoById(@PathVariable Long id) {
        ProductosPedido productosPedido = productosPedidoService.findById(id);
        if (productosPedido == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(productosPedido);
    }

    @PostMapping
    public ResponseEntity<ProductosPedido> createProductosPedido(@RequestBody ProductosPedido productosPedido) {
        ProductosPedido createdProductosPedido = productosPedidoService.save(productosPedido);
        return ResponseEntity.status(201).body(createdProductosPedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductosPedido(@PathVariable Long id) {
        productosPedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
