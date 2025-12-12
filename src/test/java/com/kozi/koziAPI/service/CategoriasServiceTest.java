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

import com.kozi.koziAPI.model.Categorias;
import com.kozi.koziAPI.model.Categoria;
import com.kozi.koziAPI.model.Producto;
import com.kozi.koziAPI.repository.CategoriasRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class CategoriasServiceTest {

    @Autowired
    private CategoriasService categoriasService;

    @MockBean
    private CategoriasRepository categoriasRepository;

    // Helpers

    private Producto createProducto(Long id) {
        Producto p = new Producto();
        p.setId(id);
        p.setNombre("Producto " + id);
        p.setPrecio(1000.0);
        p.setDescripcion("Desc");
        p.setImagenUrl("img");
        return p;
    }

    private Categoria createCategoria(Long id) {
        Categoria c = new Categoria();
        c.setId(id);
        c.setNombre("Categoria " + id);
        return c;
    }

    private Categorias createRel(Long id, Long productoId, Long categoriaId) {
        Categorias rel = new Categorias();
        rel.setId(id);
        rel.setProducto(createProducto(productoId));
        rel.setCategoria(createCategoria(categoriaId));
        return rel;
    }

    // Tests

    @Test
    void findAll_returnsListOfRelations() {
        when(categoriasRepository.findAll()).thenReturn(List.of(
                createRel(1L, 10L, 50L),
                createRel(2L, 20L, 60L)
        ));

        List<Categorias> result = categoriasService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoriasRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsRelation() {
        Categorias rel = createRel(1L, 10L, 99L);

        when(categoriasRepository.findById(1L)).thenReturn(Optional.of(rel));

        Categorias result = categoriasService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(10L, result.getProducto().getId());
        assertEquals(99L, result.getCategoria().getId());
        verify(categoriasRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(categoriasRepository.findById(999L)).thenReturn(Optional.empty());

        Categorias result = categoriasService.findById(999L);

        assertNull(result);
        verify(categoriasRepository, times(1)).findById(999L);
    }

    @Test
    void findByCategoriaId_returnsRelationsOfCategory() {
        when(categoriasRepository.findByCategoriaId(30L))
                .thenReturn(List.of(
                        createRel(1L, 10L, 30L),
                        createRel(2L, 11L, 30L)
                ));

        List<Categorias> result = categoriasService.findByCategoriaId(30L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoriasRepository, times(1)).findByCategoriaId(30L);
    }

    @Test
    void save_persistsRelation() {
        Categorias rel = createRel(null, 10L, 20L);

        when(categoriasRepository.save(any(Categorias.class)))
                .thenAnswer(invocation -> {
                    Categorias r = invocation.getArgument(0);
                    r.setId(99L);
                    return r;
                });

        Categorias result = categoriasService.save(rel);

        assertNotNull(result.getId());
        assertEquals(10L, result.getProducto().getId());
        assertEquals(20L, result.getCategoria().getId());
        verify(categoriasRepository, times(1)).save(any(Categorias.class));
    }

    @Test
    void deleteById_callsRepositoryDelete() {
        doNothing().when(categoriasRepository).deleteById(1L);

        categoriasService.deleteById(1L);

        verify(categoriasRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByProductoId_deletesOnlyRelationsMatchingProduct() {
        Categorias r1 = createRel(1L, 10L, 100L); 
        Categorias r2 = createRel(2L, 20L, 200L); 
        Categorias r3 = createRel(3L, 10L, 300L); 

        when(categoriasRepository.findAll()).thenReturn(List.of(r1, r2, r3));

        categoriasService.deleteByProductoId(10L);

        verify(categoriasRepository, times(1)).deleteById(1L);
        verify(categoriasRepository, times(1)).deleteById(3L);
        verify(categoriasRepository, never()).deleteById(2L);
    }

    @Test
    void deleteByCategoriaId_deletesOnlyRelationsMatchingCategory() {
        Categorias r1 = createRel(1L, 10L, 100L); 
        Categorias r2 = createRel(2L, 10L, 200L); 
        Categorias r3 = createRel(3L, 11L, 100L); 

        when(categoriasRepository.findAll()).thenReturn(List.of(r1, r2, r3));

        categoriasService.deleteByCategoriaId(100L);

        verify(categoriasRepository, times(1)).deleteById(1L);
        verify(categoriasRepository, times(1)).deleteById(3L);
        verify(categoriasRepository, never()).deleteById(2L);
    }
}
