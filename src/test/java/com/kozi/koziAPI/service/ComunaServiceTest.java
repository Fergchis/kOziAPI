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

import com.kozi.koziAPI.model.Ciudad;
import com.kozi.koziAPI.model.Comuna;
import com.kozi.koziAPI.repository.ComunaRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class ComunaServiceTest {

    @Autowired
    private ComunaService comunaService;

    @MockBean
    private ComunaRepository comunaRepository;

    @MockBean
    private DireccionService direccionService;

    // Helpers

    private Ciudad createCiudad(Long id) {
        Ciudad c = new Ciudad();
        c.setId(id);
        return c;
    }

    private Comuna createComuna(Long id, String nombre, Long ciudadId) {
        Comuna comuna = new Comuna();
        comuna.setId(id);
        comuna.setNombre(nombre);
        comuna.setCiudad(createCiudad(ciudadId));
        return comuna;
    }

    // Tests

    @Test
    void findAll_returnsListOfComunas() {
        when(comunaRepository.findAll())
                .thenReturn(List.of(
                        createComuna(1L, "Comuna 1", 10L),
                        createComuna(2L, "Comuna 2", 20L)
                ));

        List<Comuna> result = comunaService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(comunaRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsComuna() {
        Comuna comuna = createComuna(1L, "Comuna 1", 10L);
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comuna));

        Comuna result = comunaService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Comuna 1", result.getNombre());
        verify(comunaRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(comunaRepository.findById(99L)).thenReturn(Optional.empty());

        Comuna result = comunaService.findById(99L);

        assertNull(result);
        verify(comunaRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsComuna() {
        Comuna nueva = createComuna(null, "Nueva Comuna", 10L);

        when(comunaRepository.save(any(Comuna.class)))
                .thenAnswer(invocation -> {
                    Comuna c = invocation.getArgument(0);
                    c.setId(50L);
                    return c;
                });

        Comuna result = comunaService.save(nueva);

        assertNotNull(result.getId());
        assertEquals("Nueva Comuna", result.getNombre());
        verify(comunaRepository, times(1)).save(any(Comuna.class));
    }

    @Test
    void patchComuna_updatesNombre_whenExists() {
        Comuna existing = createComuna(1L, "Vieja", 10L);
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(comunaRepository.save(any(Comuna.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Comuna patch = new Comuna();
        patch.setId(1L);
        patch.setNombre("Nueva");

        Comuna result = comunaService.patchComuna(patch);

        assertNotNull(result);
        assertEquals("Nueva", result.getNombre());
        verify(comunaRepository, times(1)).findById(1L);
        verify(comunaRepository, times(1)).save(any(Comuna.class));
    }

    @Test
    void patchComuna_returnsNull_whenNotExists() {
        when(comunaRepository.findById(99L)).thenReturn(Optional.empty());

        Comuna patch = new Comuna();
        patch.setId(99L);
        patch.setNombre("Irrelevante");

        Comuna result = comunaService.patchComuna(patch);

        assertNull(result);
        verify(comunaRepository, times(1)).findById(99L);
        verify(comunaRepository, never()).save(any(Comuna.class));
    }

    @Test
    void deleteById_callsDireccionServiceAndRepository() {
        doNothing().when(direccionService).deleteByComunaId(1L);
        doNothing().when(comunaRepository).deleteById(1L);

        comunaService.deleteById(1L);

        verify(direccionService, times(1)).deleteByComunaId(1L);
        verify(comunaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByCiudadId_deletesMatchingComunas() {
        Comuna c1 = createComuna(1L, "C1", 10L); 
        Comuna c2 = createComuna(2L, "C2", 20L); 
        Comuna c3 = createComuna(3L, "C3", 10L); 

        when(comunaRepository.findAll())
                .thenReturn(List.of(c1, c2, c3));

        comunaService.deleteByCiudadId(10L);

        verify(comunaRepository, times(1)).findAll();
        verify(comunaRepository, times(1)).deleteById(1L);
        verify(comunaRepository, times(1)).deleteById(3L);
        verify(comunaRepository, never()).deleteById(2L);
    }
}
