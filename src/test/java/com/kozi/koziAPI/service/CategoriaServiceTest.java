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

import com.kozi.koziAPI.model.Categoria;
import com.kozi.koziAPI.repository.CategoriaRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class CategoriaServiceTest {

    @Autowired
    private CategoriaService categoriaService;

    @MockBean
    private CategoriaRepository categoriaRepository;

    @MockBean
    private CategoriasService categoriasService;

    // Helpers

    private Categoria createCategoria(Long id, String nombre) {
        Categoria c = new Categoria();
        c.setId(id);
        c.setNombre(nombre);
        return c;
    }

    // Tests

    @Test
    void findAll_returnsListOfCategories() {
        when(categoriaRepository.findAll()).thenReturn(List.of(
                createCategoria(1L, "Accesorios"),
                createCategoria(2L, "Ropa")
        ));

        List<Categoria> result = categoriaService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsCategoria() {
        Categoria categoria = createCategoria(1L, "Anillos");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria result = categoriaService.findById(1L);

        assertNotNull(result);
        assertEquals("Anillos", result.getNombre());
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        Categoria result = categoriaService.findById(999L);

        assertNull(result);
        verify(categoriaRepository, times(1)).findById(999L);
    }

    @Test
    void save_persistsCategoria() {
        Categoria nueva = createCategoria(null, "Pulseras");

        when(categoriaRepository.save(any(Categoria.class)))
                .thenAnswer(invocation -> {
                    Categoria c = invocation.getArgument(0);
                    c.setId(100L);
                    return c;
                });

        Categoria result = categoriaService.save(nueva);

        assertNotNull(result.getId());
        assertEquals("Pulseras", result.getNombre());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void patchCategoria_updatesNombreSuccessfully() {
        Categoria original = createCategoria(1L, "Old");
        Categoria update = createCategoria(1L, "New");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(original));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(original);

        Categoria result = categoriaService.patchCategoria(update);

        assertNotNull(result);
        assertEquals("New", result.getNombre());
        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).save(original);
    }

    @Test
    void patchCategoria_whenNotExists_returnsNull() {
        Categoria update = createCategoria(999L, "New");

        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        Categoria result = categoriaService.patchCategoria(update);

        assertNull(result);
        verify(categoriaRepository, times(1)).findById(999L);
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void deleteById_triggersCascadeDeleteAndRepositoryDelete() {
        doNothing().when(categoriasService).deleteByCategoriaId(5L);
        doNothing().when(categoriaRepository).deleteById(5L);

        categoriaService.deleteById(5L);

        verify(categoriasService, times(1)).deleteByCategoriaId(5L);
        verify(categoriaRepository, times(1)).deleteById(5L);
    }
}
