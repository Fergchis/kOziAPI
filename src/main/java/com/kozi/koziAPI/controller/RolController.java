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

import com.kozi.koziAPI.model.Rol;
import com.kozi.koziAPI.service.RolService;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    
    @Autowired
    private RolService rolService;

    @GetMapping
    public ResponseEntity<List<Rol>> getAllRoles() {
        List<Rol> roles = rolService.findAll();
        if (roles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> getRolById(@PathVariable Long id) {
        Rol rol = rolService.findById(id);
        if (rol == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(rol);
    }

    @PostMapping
    public ResponseEntity<Rol> createRol(@RequestBody Rol rol) {
        Rol createdRol = rolService.save(rol);
        return ResponseEntity.status(201).body(createdRol);
    }   

    @PutMapping("/{id}")
    public ResponseEntity<Rol> updateRol(@PathVariable Long id, @RequestBody Rol rol) {
        rol.setId(id);
        Rol updatedRol = rolService.save(rol);
        if (updatedRol == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRol);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Rol> patchRol(@PathVariable Long id, @RequestBody Rol rol) {
        rol.setId(id);
        Rol patchedRol = rolService.patchRol(rol);
        if (patchedRol == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(patchedRol);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable Long id) {
        rolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
