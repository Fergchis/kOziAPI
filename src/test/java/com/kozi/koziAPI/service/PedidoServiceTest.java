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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.kozi.koziAPI.model.Envio;
import com.kozi.koziAPI.model.Estado;
import com.kozi.koziAPI.model.Pago;
import com.kozi.koziAPI.model.Pedido;
import com.kozi.koziAPI.model.Usuario;
import com.kozi.koziAPI.repository.PedidoRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class PedidoServiceTest {

    @Autowired
    private PedidoService pedidoService;

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private ProductosPedidoService productosPedidoService;

    // Helpers 

    private Usuario createUsuario(Long id) {
        Usuario u = new Usuario();
        u.setId(id);
        return u;
    }

    private Pago createPago(Long id) {
        Pago p = new Pago();
        p.setId(id);
        return p;
    }

    private Envio createEnvio(Long id) {
        Envio e = new Envio();
        e.setId(id);
        return e;
    }

    private Estado createEstado(Long id) {
        Estado e = new Estado();
        e.setId(id);
        return e;
    }

    private Pedido createPedido(Long id, Long usuarioId, Long pagoId, Long envioId, Long estadoId) {
        Pedido p = new Pedido();
        p.setId(id);
        p.setFechaCreacion(LocalDateTime.now());
        p.setTotal(5000.0);
        p.setUsuario(createUsuario(usuarioId));
        p.setPago(createPago(pagoId));
        p.setEnvio(createEnvio(envioId));
        p.setEstado(createEstado(estadoId));
        return p;
    }

    // Tests

    @Test
    void findAll_returnsPedidoList() {
        when(pedidoRepository.findAll())
                .thenReturn(List.of(
                        createPedido(1L, 10L, 1L, 1L, 1L),
                        createPedido(2L, 20L, 2L, 2L, 2L)
                ));

        List<Pedido> result = pedidoService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsPedido() {
        Pedido p = createPedido(1L, 10L, 1L, 1L, 1L);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(p));

        Pedido result = pedidoService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        Pedido result = pedidoService.findById(99L);

        assertNull(result);
        verify(pedidoRepository, times(1)).findById(99L);
    }

    @Test
    void findByUsuarioId_returnsOnlyPedidosFromUser() {
        when(pedidoRepository.findByUsuarioId(10L))
                .thenReturn(List.of(
                        createPedido(1L, 10L, 1L, 1L, 1L),
                        createPedido(2L, 10L, 2L, 2L, 2L)
                ));

        List<Pedido> result = pedidoService.findByUsuarioId(10L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(pedidoRepository, times(1)).findByUsuarioId(10L);
    }

    @Test
    void save_persistsCorrectly() {
        Pedido nuevo = createPedido(null, 10L, 1L, 1L, 1L);

        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> {
                    Pedido p = invocation.getArgument(0);
                    p.setId(100L);
                    return p;
                });

        Pedido saved = pedidoService.save(nuevo);

        assertNotNull(saved.getId());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void patchPedido_updatesOnlyNonNullFields() {
        Pedido existing = createPedido(1L, 10L, 1L, 1L, 1L);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido patch = new Pedido();
        patch.setId(1L);
        patch.setFechaCreacion(LocalDateTime.of(2023, 1, 1, 12, 0));
        patch.setTotal(9999.0);
        patch.setPago(createPago(999L));

        Pedido result = pedidoService.patchPedido(patch);

        assertNotNull(result);
        assertEquals(9999.0, result.getTotal());
        assertEquals(999L, result.getPago().getId());
        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void patchPedido_whenNotFound_returnsNull() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        Pedido patch = new Pedido();
        patch.setId(99L);

        Pedido result = pedidoService.patchPedido(patch);

        assertNull(result);
        verify(pedidoRepository, times(1)).findById(99L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void deleteById_callsCascadeAndRepositoryDelete() {
        doNothing().when(productosPedidoService).deleteByPedidoId(1L);
        doNothing().when(pedidoRepository).deleteById(1L);

        pedidoService.deleteById(1L);

        verify(productosPedidoService, times(1)).deleteByPedidoId(1L);
        verify(pedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByUsuarioId_deletesMatchingPedidos() {
        Pedido p1 = createPedido(1L, 10L, 1L, 1L, 1L); 
        Pedido p2 = createPedido(2L, 20L, 1L, 1L, 1L); 
        Pedido p3 = createPedido(3L, 10L, 1L, 1L, 1L); 

        when(pedidoRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        pedidoService.deleteByUsuarioId(10L);

        verify(pedidoRepository, times(1)).findAll();
        verify(pedidoRepository, times(1)).deleteById(1L);
        verify(pedidoRepository, times(1)).deleteById(3L);
        verify(pedidoRepository, never()).deleteById(2L);
    }

    @Test
    void deleteByPagoId_deletesMatchingPedidos() {
        Pedido p1 = createPedido(1L, 10L, 100L, 1L, 1L); 
        Pedido p2 = createPedido(2L, 10L, 200L, 1L, 1L);
        Pedido p3 = createPedido(3L, 10L, 100L, 1L, 1L);

        when(pedidoRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        pedidoService.deleteByPagoId(100L);

        verify(pedidoRepository, times(1)).findAll();
        verify(pedidoRepository, times(1)).deleteById(1L);
        verify(pedidoRepository, times(1)).deleteById(3L);
        verify(pedidoRepository, never()).deleteById(2L);
    }

    @Test
    void deleteByEnvioId_deletesMatchingPedidos() {
        Pedido p1 = createPedido(1L, 10L, 1L, 100L, 1L);
        Pedido p2 = createPedido(2L, 10L, 1L, 200L, 1L);
        Pedido p3 = createPedido(3L, 10L, 1L, 100L, 1L);

        when(pedidoRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        pedidoService.deleteByEnvioId(100L);

        verify(pedidoRepository, times(1)).findAll();
        verify(pedidoRepository, times(1)).deleteById(1L);
        verify(pedidoRepository, times(1)).deleteById(3L);
        verify(pedidoRepository, never()).deleteById(2L);
    }

    @Test
    void deleteByEstadoId_deletesMatchingPedidos() {
        Pedido p1 = createPedido(1L, 10L, 1L, 1L, 5L);
        Pedido p2 = createPedido(2L, 10L, 1L, 1L, 6L);
        Pedido p3 = createPedido(3L, 10L, 1L, 1L, 5L);

        when(pedidoRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        pedidoService.deleteByEstadoId(5L);

        verify(pedidoRepository, times(1)).findAll();
        verify(pedidoRepository, times(1)).deleteById(1L);
        verify(pedidoRepository, times(1)).deleteById(3L);
        verify(pedidoRepository, never()).deleteById(2L);
    }
}
