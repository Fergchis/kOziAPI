package com.kozi.koziAPI.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.kozi.koziAPI.model.Material;
import com.kozi.koziAPI.repository.MaterialRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class MaterialServiceTest {

    @Autowired
    private MaterialService materialService;

    @MockBean
    private MaterialRepository materialRepository;

    @MockBean
    private MaterialesService materialesService;

    // Helper 
    private Material createMaterial(Long id, String nombre) {
        Material m = new Material();
        m.setId(id);
        m.setNombre(nombre);
        return m;
    }

    // Tests

    @Test
    void findAll_returnsMaterialList() {
        when(materialRepository.findAll())
                .thenReturn(List.of(
                        createMaterial(1L, "Acero"),
                        createMaterial(2L, "Plata")
                ));

        List<Material> result = materialService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(materialRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsMaterial() {
        Material mat = createMaterial(1L, "Acero");
        when(materialRepository.findById(1L)).thenReturn(Optional.of(mat));

        Material result = materialService.findById(1L);

        assertNotNull(result);
        assertEquals("Acero", result.getNombre());
        verify(materialRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(materialRepository.findById(99L)).thenReturn(Optional.empty());

        Material result = materialService.findById(99L);

        assertNull(result);
        verify(materialRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsMaterial() {
        Material newMat = createMaterial(null, "Bronce");

        when(materialRepository.save(any(Material.class)))
                .thenAnswer(invocation -> {
                    Material m = invocation.getArgument(0);
                    m.setId(10L);
                    return m;
                });

        Material result = materialService.save(newMat);

        assertNotNull(result.getId());
        assertEquals("Bronce", result.getNombre());
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void patchMaterial_updatesNombre_whenMaterialExists() {
        Material existing = createMaterial(1L, "Viejo");
        when(materialRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(materialRepository.save(any(Material.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Material patch = new Material();
        patch.setId(1L);
        patch.setNombre("Nuevo");

        Material result = materialService.patchMaterial(patch);

        assertNotNull(result);
        assertEquals("Nuevo", result.getNombre());
        verify(materialRepository, times(1)).findById(1L);
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void patchMaterial_returnsNull_whenNotExists() {
        when(materialRepository.findById(99L)).thenReturn(Optional.empty());

        Material patch = new Material();
        patch.setId(99L);
        patch.setNombre("Irrelevante");

        Material result = materialService.patchMaterial(patch);

        assertNull(result);
        verify(materialRepository, times(1)).findById(99L);
        verify(materialRepository, never()).save(any(Material.class));
    }

    @Test
    void deleteById_callsMaterialesService_andRepository() {
        doNothing().when(materialesService).deleteByMaterialId(1L);
        doNothing().when(materialRepository).deleteById(1L);

        materialService.deleteById(1L);

        verify(materialesService, times(1)).deleteByMaterialId(1L);
        verify(materialRepository, times(1)).deleteById(1L);
    }
}
