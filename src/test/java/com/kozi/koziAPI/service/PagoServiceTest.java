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

import com.kozi.koziAPI.model.Pago;
import com.kozi.koziAPI.repository.PagoRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class PagoServiceTest {

    @Autowired
    private PagoService pagoService;

    @MockBean
    private PagoRepository pagoRepository;

    @MockBean
    private PedidoService pedidoService;

    // Helper

    private Pago createPago(Long id, String tipo) {
        Pago p = new Pago();
        p.setId(id);
        p.setTipoPago(tipo);
        return p;
    }

    // Tests

    @Test
    void findAll_returnsPagoList() {
        when(pagoRepository.findAll())
                .thenReturn(List.of(
                        createPago(1L, "Crédito"),
                        createPago(2L, "Débito")
                ));

        List<Pago> result = pagoService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Crédito", result.get(0).getTipoPago());
        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsPago() {
        Pago pago = createPago(1L, "Crédito");
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        Pago result = pagoService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Crédito", result.getTipoPago());
        verify(pagoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        Pago result = pagoService.findById(99L);

        assertNull(result);
        verify(pagoRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsThroughRepository() {
        Pago nuevo = createPago(null, "Transferencia");

        when(pagoRepository.save(any(Pago.class)))
                .thenAnswer(invocation -> {
                    Pago p = invocation.getArgument(0);
                    p.setId(10L);
                    return p;
                });

        Pago saved = pagoService.save(nuevo);

        assertNotNull(saved.getId());
        assertEquals("Transferencia", saved.getTipoPago());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void patchPago_updatesTipoPagoWhenExists() {
        Pago existing = createPago(1L, "Crédito");

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(pagoRepository.save(any(Pago.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pago patch = new Pago();
        patch.setId(1L);
        patch.setTipoPago("Débito");

        Pago result = pagoService.patchPago(patch);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Débito", result.getTipoPago());
        verify(pagoRepository, times(1)).findById(1L);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void patchPago_whenNotFound_returnsNull() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        Pago patch = new Pago();
        patch.setId(99L);
        patch.setTipoPago("Irrelevante");

        Pago result = pagoService.patchPago(patch);

        assertNull(result);
        verify(pagoRepository, times(1)).findById(99L);
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void deleteById_callsPedidoServiceAndRepository() {
        doNothing().when(pedidoService).deleteByPagoId(1L);
        doNothing().when(pagoRepository).deleteById(1L);

        pagoService.deleteById(1L);

        verify(pedidoService, times(1)).deleteByPagoId(1L);
        verify(pagoRepository, times(1)).deleteById(1L);
    }
}
