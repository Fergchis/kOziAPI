package com.kozi.koziAPI.controller;

import com.kozi.koziAPI.model.Orden;
import com.kozi.koziAPI.model.OrdenItem;
import com.kozi.koziAPI.model.Producto;
import com.kozi.koziAPI.model.Usuario;
import com.kozi.koziAPI.repository.OrdenItemRepository;
import com.kozi.koziAPI.repository.OrdenRepository;
import com.kozi.koziAPI.repository.ProductoRepository;
import com.kozi.koziAPI.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ordenes")
public class OrdenController {

    private final OrdenRepository ordenRepository;
    private final OrdenItemRepository ordenItemRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public OrdenController(OrdenRepository ordenRepository,
                           OrdenItemRepository ordenItemRepository,
                           UsuarioRepository usuarioRepository,
                           ProductoRepository productoRepository) {
        this.ordenRepository = ordenRepository;
        this.ordenItemRepository = ordenItemRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    // CREAR ORDEN (simulada)
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Orden orden) {

        if (orden.getUsuario() == null || orden.getUsuario().getId() == null) {
            return ResponseEntity.badRequest().body("Debe enviar el usuario con su id");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(orden.getUsuario().getId());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no existe");
        }

        orden.setUsuario(usuarioOpt.get());
        orden.setFechaCreacion(LocalDateTime.now());
        orden.setEstado("CREADA");

        double total = 0.0;

        // Guardamos orden primero
        Orden guardada = ordenRepository.save(orden);

        if (orden.getItems() != null) {
            for (OrdenItem item : orden.getItems()) {
                Optional<Producto> prodOpt = productoRepository.findById(item.getProducto().getId());
                if (prodOpt.isEmpty()) continue;

                Producto prod = prodOpt.get();
                item.setProducto(prod);
                item.setOrden(guardada);
                item.setPrecioUnitario(prod.getPrecio());
                total += prod.getPrecio() * item.getCantidad();
                ordenItemRepository.save(item);

                // Descontar stock (simple)
                prod.setStock(prod.getStock() - item.getCantidad());
                productoRepository.save(prod);
            }
        }

        guardada.setTotal(total);
        ordenRepository.save(guardada);

        return ResponseEntity.ok(guardada);
    }

    // LISTAR TODAS (para admin)
    @GetMapping
    public ResponseEntity<List<Orden>> listar() {
        return ResponseEntity.ok(ordenRepository.findAll());
    }

    // LISTAR POR USUARIO
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Orden>> listarPorUsuario(@PathVariable int id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Orden> ordenes = ordenRepository.findByUsuario(usuario.get());
        return ResponseEntity.ok(ordenes);
    }
}
