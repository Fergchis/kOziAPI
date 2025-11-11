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

import com.kozi.koziAPI.model.Pago;
import com.kozi.koziAPI.service.PagoService;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    
    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> getAllPagos() {
        List<Pago> pagos = pagoService.findAll();
        if (pagos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> getPagoById(@PathVariable Long id) {
        Pago pago = pagoService.findById(id);
        if (pago == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(pago);
    }

    @PostMapping
    public ResponseEntity<Pago> createPago(@RequestBody Pago pago) {
        Pago createdPago = pagoService.save(pago);
        return ResponseEntity.status(201).body(createdPago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> updatePago(@PathVariable Long id, @RequestBody Pago pago) {
        pago.setId(id);
        Pago updatedPago = pagoService.save(pago);
        if (updatedPago == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPago);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pago> patchPago(@PathVariable Long id, @RequestBody Pago pago) {
        pago.setId(id);
        Pago patchedPago = pagoService.patchPago(pago);
        if (patchedPago == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(patchedPago);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable Long id) {
        pagoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
