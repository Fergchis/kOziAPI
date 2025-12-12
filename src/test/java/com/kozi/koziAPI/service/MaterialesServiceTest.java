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
import com.kozi.koziAPI.model.Materiales;
import com.kozi.koziAPI.model.Producto;
import com.kozi.koziAPI.repository.MaterialesRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class MaterialesServiceTest {

    @Autowired
    private MaterialesService materialesService;

    @MockBean
    private MaterialesRepository materialesRepository;

    // Helpers 

    private Producto createProducto(Long id) {
        Producto p = new Producto();
        p.setId(id);
        return p;
    }

    private Material createMaterial(Long id) {
        Material m = new Material();
        m.setId(id);
        return m;
    }

    private Materiales createMateriales(Long id, Long productoId, Long materialId) {
        Materiales m = new Materiales();
        m.setId(id);
        m.setProducto(createProducto(productoId));
        m.setMaterial(createMaterial(materialId));
        return m;
    }

    // Tests 

    @Test
    void findAll_returnsList() {
        when(materialesRepository.findAll())
                .thenReturn(List.of(
                        createMateriales(1L, 10L, 100L),
                        createMateriales(2L, 20L, 200L)
                ));

        List<Materiales> result = materialesService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(materialesRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsEntity() {
        Materiales m = createMateriales(1L, 10L, 100L);
        when(materialesRepository.findById(1L)).thenReturn(Optional.of(m));

        Materiales result = materialesService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(materialesRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(materialesRepository.findById(99L)).thenReturn(Optional.empty());

        Materiales result = materialesService.findById(99L);

        assertNull(result);
        verify(materialesRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsThroughRepository() {
        Materiales nuevo = createMateriales(null, 10L, 100L);

        when(materialesRepository.save(any(Materiales.class)))
                .thenAnswer(invocation -> {
                    Materiales m = invocation.getArgument(0);
                    m.setId(50L);
                    return m;
                });

        Materiales result = materialesService.save(nuevo);

        assertNotNull(result.getId());
        verify(materialesRepository, times(1)).save(any(Materiales.class));
    }

    @Test
    void deleteById_callsRepositoryDelete() {
        doNothing().when(materialesRepository).deleteById(1L);

        materialesService.deleteById(1L);

        verify(materialesRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByProductoId_deletesOnlyMatchingProducto() {
        Materiales m1 = createMateriales(1L, 10L, 100L);
        Materiales m2 = createMateriales(2L, 20L, 100L);
        Materiales m3 = createMateriales(3L, 10L, 200L); 

        when(materialesRepository.findAll())
                .thenReturn(List.of(m1, m2, m3));

        materialesService.deleteByProductoId(10L);

        verify(materialesRepository, times(1)).findAll();
        verify(materialesRepository, times(1)).deleteById(1L);
        verify(materialesRepository, times(1)).deleteById(3L);
        verify(materialesRepository, never()).deleteById(2L);
    }

    @Test
    void deleteByMaterialId_deletesOnlyMatchingMaterial() {
        Materiales m1 = createMateriales(1L, 10L, 100L); 
        Materiales m2 = createMateriales(2L, 10L, 200L); 
        Materiales m3 = createMateriales(3L, 20L, 100L); 

        when(materialesRepository.findAll())
                .thenReturn(List.of(m1, m2, m3));

        materialesService.deleteByMaterialId(100L);

        verify(materialesRepository, times(1)).findAll();
        verify(materialesRepository, times(1)).deleteById(1L);
        verify(materialesRepository, times(1)).deleteById(3L);
        verify(materialesRepository, never()).deleteById(2L);
    }
}
