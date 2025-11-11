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

import com.kozi.koziAPI.model.Envio;
import com.kozi.koziAPI.service.EnvioService;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {
    
    @Autowired
    private EnvioService envioService;

    @GetMapping
    public ResponseEntity<List<Envio>> getAllEnvios() {
        List<Envio> envios = envioService.findAll();
        if (envios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(envios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> getEnvioById(@PathVariable Long id) {
        Envio envio = envioService.findById(id);
        if (envio == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(envio);
    }

    @PostMapping
    public ResponseEntity<Envio> createEnvio(@RequestBody Envio envio) {
        Envio createdEnvio = envioService.save(envio);
        return ResponseEntity.status(201).body(createdEnvio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Envio> updateEnvio(@PathVariable Long id, @RequestBody Envio envio) {
        envio.setId(id);
        Envio updatedEnvio = envioService.save(envio);
        if (updatedEnvio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEnvio);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Envio> patchEnvio(@PathVariable Long id, @RequestBody Envio envio) {
        envio.setId(id);
        Envio patchedEnvio = envioService.patchEnvio(envio);
        if (patchedEnvio == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(patchedEnvio);
    }   

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnvio(@PathVariable Long id) {
        envioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
