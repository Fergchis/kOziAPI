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

import com.kozi.koziAPI.model.Region;
import com.kozi.koziAPI.service.RegionService;

@RestController
@RequestMapping("/api/regiones")
public class RegionController {
    
    @Autowired
    private RegionService regionService;

    @GetMapping
    public ResponseEntity<List<Region>> getAllRegiones() {
        List<Region> regiones = regionService.findAll();
        if (regiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(regiones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(@PathVariable Long id) {
        Region region = regionService.findById(id);
        if (region == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(region);
    }

    @PostMapping
    public ResponseEntity<Region> createRegion(@RequestBody Region region) {
        Region createdRegion = regionService.save(region);
        return ResponseEntity.status(201).body(createdRegion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Region> updateRegion(@PathVariable Long id, @RequestBody Region region) {
        region.setId(id);
        Region updatedRegion = regionService.save(region);
        if (updatedRegion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRegion);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Region> patchRegion(@PathVariable Long id, @RequestBody Region region) {
        region.setId(id);
        Region patchedRegion = regionService.patchRegion(region);
        if (patchedRegion == null) {
            return ResponseEntity.notFound().build();
        } 
        return ResponseEntity.ok(patchedRegion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
