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

import com.kozi.koziAPI.model.Estado;
import com.kozi.koziAPI.repository.EstadoRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class EstadoServiceTest {

    @Autowired
    private EstadoService estadoService;

    @MockBean
    private EstadoRepository estadoRepository;

    @MockBean
    private PedidoService pedidoService;

    // Helper 
    private Estado createEstado(Long id, String tipo) {
        Estado e = new Estado();
        e.setId(id);
        e.setTipoEstado(tipo);
        return e;
    }

    // Tests 

    @Test
    void findAll_returnsList() {
        when(estadoRepository.findAll())
                .thenReturn(List.of(
                        createEstado(1L, "Creado"),
                        createEstado(2L, "Enviado")
                ));

        List<Estado> result = estadoService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(estadoRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsEstado() {
        Estado est = createEstado(1L, "Pagado");
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(est));

        Estado result = estadoService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Pagado", result.getTipoEstado());
        verify(estadoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());

        Estado result = estadoService.findById(99L);

        assertNull(result);
        verify(estadoRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsEstado() {
        Estado nuevo = createEstado(null, "Cancelado");

        when(estadoRepository.save(any(Estado.class)))
                .thenAnswer(invocation -> {
                    Estado e = invocation.getArgument(0);
                    e.setId(10L);
                    return e;
                });

        Estado result = estadoService.save(nuevo);

        assertNotNull(result.getId());
        assertEquals("Cancelado", result.getTipoEstado());
        verify(estadoRepository, times(1)).save(any(Estado.class));
    }

    @Test
    void patchEstado_updatesTipoEstado_whenExists() {
        Estado existing = createEstado(1L, "Viejo");
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(estadoRepository.save(any(Estado.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Estado patch = new Estado();
        patch.setId(1L);
        patch.setTipoEstado("Nuevo");

        Estado result = estadoService.patchEstado(patch);

        assertNotNull(result);
        assertEquals("Nuevo", result.getTipoEstado());
        verify(estadoRepository, times(1)).findById(1L);
        verify(estadoRepository, times(1)).save(any(Estado.class));
    }

    @Test
    void patchEstado_returnsNull_whenNotExists() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());

        Estado patch = new Estado();
        patch.setId(99L);
        patch.setTipoEstado("Nada");

        Estado result = estadoService.patchEstado(patch);

        assertNull(result);
        verify(estadoRepository, times(1)).findById(99L);
        verify(estadoRepository, never()).save(any(Estado.class));
    }

    @Test
    void deleteById_callsPedidoServiceAndRepository() {
        doNothing().when(pedidoService).deleteByEstadoId(1L);
        doNothing().when(estadoRepository).deleteById(1L);

        estadoService.deleteById(1L);

        verify(pedidoService, times(1)).deleteByEstadoId(1L);
        verify(estadoRepository, times(1)).deleteById(1L);
    }
}
