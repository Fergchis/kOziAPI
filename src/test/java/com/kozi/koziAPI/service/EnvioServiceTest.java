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

import com.kozi.koziAPI.model.Envio;
import com.kozi.koziAPI.repository.EnvioRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class EnvioServiceTest {

    @Autowired
    private EnvioService envioService;

    @MockBean
    private EnvioRepository envioRepository;

    @MockBean
    private PedidoService pedidoService;

    // Helpers
    private Envio createEnvio(Long id, String metodo) {
        Envio e = new Envio();
        e.setId(id);
        e.setMetodoEnvio(metodo);
        return e;
    }

    // Tests

    @Test
    void findAll_returnsListOfEnvios() {
        when(envioRepository.findAll())
                .thenReturn(List.of(
                        createEnvio(1L, "Chilexpress"),
                        createEnvio(2L, "FedEx")
                ));

        List<Envio> result = envioService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(envioRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsEnvio() {
        Envio envio = createEnvio(1L, "CorreosChile");
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));

        Envio result = envioService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("CorreosChile", result.getMetodoEnvio());
        verify(envioRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(envioRepository.findById(99L)).thenReturn(Optional.empty());

        Envio result = envioService.findById(99L);

        assertNull(result);
        verify(envioRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsEnvio() {
        Envio nuevo = createEnvio(null, "FedEx");

        when(envioRepository.save(any(Envio.class)))
                .thenAnswer(invocation -> {
                    Envio e = invocation.getArgument(0);
                    e.setId(10L);
                    return e;
                });

        Envio result = envioService.save(nuevo);

        assertNotNull(result.getId());
        assertEquals("FedEx", result.getMetodoEnvio());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    void patchEnvio_updatesMetodo_whenExists() {
        Envio existing = createEnvio(1L, "Viejo");
        when(envioRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(envioRepository.save(any(Envio.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Envio patch = new Envio();
        patch.setId(1L);
        patch.setMetodoEnvio("Nuevo");

        Envio result = envioService.patchEnvio(patch);

        assertNotNull(result);
        assertEquals("Nuevo", result.getMetodoEnvio());
        verify(envioRepository, times(1)).findById(1L);
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    void patchEnvio_returnsNull_whenNotExists() {
        when(envioRepository.findById(99L)).thenReturn(Optional.empty());

        Envio patch = new Envio();
        patch.setId(99L);
        patch.setMetodoEnvio("Irrelevante");

        Envio result = envioService.patchEnvio(patch);

        assertNull(result);
        verify(envioRepository, times(1)).findById(99L);
        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    void deleteById_callsPedidoServiceAndRepository() {
        doNothing().when(pedidoService).deleteByEnvioId(1L);
        doNothing().when(envioRepository).deleteById(1L);

        envioService.deleteById(1L);

        verify(pedidoService, times(1)).deleteByEnvioId(1L);
        verify(envioRepository, times(1)).deleteById(1L);
    }
}
