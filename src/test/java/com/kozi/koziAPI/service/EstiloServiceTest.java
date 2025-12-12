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

import com.kozi.koziAPI.model.Estilo;
import com.kozi.koziAPI.repository.EstiloRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class EstiloServiceTest {

    @Autowired
    private EstiloService estiloService;

    @MockBean
    private EstiloRepository estiloRepository;

    @MockBean
    private EstilosService estilosService;

    // Helper

    private Estilo createEstilo(Long id, String nombre) {
        Estilo e = new Estilo();
        e.setId(id);
        e.setNombre(nombre);
        return e;
    }

    // Tests

    @Test
    void findAll_returnsListOfEstilos() {
        when(estiloRepository.findAll())
                .thenReturn(List.of(
                        createEstilo(1L, "Gótico"),
                        createEstilo(2L, "Vintage")
                ));

        List<Estilo> result = estiloService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Gótico", result.get(0).getNombre());
        verify(estiloRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsEstilo() {
        Estilo estilo = createEstilo(1L, "Gótico");
        when(estiloRepository.findById(1L)).thenReturn(Optional.of(estilo));

        Estilo result = estiloService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Gótico", result.getNombre());
        verify(estiloRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(estiloRepository.findById(99L)).thenReturn(Optional.empty());

        Estilo result = estiloService.findById(99L);

        assertNull(result);
        verify(estiloRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsEstilo() {
        Estilo nuevo = createEstilo(null, "Cyberpunk");

        when(estiloRepository.save(any(Estilo.class)))
                .thenAnswer(invocation -> {
                    Estilo e = invocation.getArgument(0);
                    e.setId(10L);
                    return e;
                });

        Estilo result = estiloService.save(nuevo);

        assertNotNull(result.getId());
        assertEquals("Cyberpunk", result.getNombre());
        verify(estiloRepository, times(1)).save(any(Estilo.class));
    }

    @Test
    void patchEstilo_updatesNombre_whenExists() {
        Estilo existing = createEstilo(1L, "Viejo");
        when(estiloRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(estiloRepository.save(any(Estilo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Estilo patch = new Estilo();
        patch.setId(1L);
        patch.setNombre("Nuevo");

        Estilo result = estiloService.patchEstilo(patch);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nuevo", result.getNombre());
        verify(estiloRepository, times(1)).findById(1L);
        verify(estiloRepository, times(1)).save(any(Estilo.class));
    }

    @Test
    void patchEstilo_returnsNull_whenNotExists() {
        when(estiloRepository.findById(99L)).thenReturn(Optional.empty());

        Estilo patch = new Estilo();
        patch.setId(99L);
        patch.setNombre("Irrelevante");

        Estilo result = estiloService.patchEstilo(patch);

        assertNull(result);
        verify(estiloRepository, times(1)).findById(99L);
        verify(estiloRepository, never()).save(any(Estilo.class));
    }

    @Test
    void deleteById_callsEstilosServiceAndRepository() {
        doNothing().when(estilosService).deleteByEstiloId(1L);
        doNothing().when(estiloRepository).deleteById(1L);

        estiloService.deleteById(1L);

        verify(estilosService, times(1)).deleteByEstiloId(1L);
        verify(estiloRepository, times(1)).deleteById(1L);
    }
}
