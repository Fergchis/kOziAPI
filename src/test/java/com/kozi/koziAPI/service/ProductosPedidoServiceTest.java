package com.kozi.koziAPI.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
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

import com.kozi.koziAPI.model.Pedido;
import com.kozi.koziAPI.model.Producto;
import com.kozi.koziAPI.model.ProductosPedido;
import com.kozi.koziAPI.repository.ProductosPedidoRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class ProductosPedidoServiceTest {

    @Autowired
    private ProductosPedidoService productosPedidoService;

    @MockBean
    private ProductosPedidoRepository productosPedidoRepository;

    // Helpers

    private Pedido createPedido(Long id) {
        Pedido p = new Pedido();
        p.setId(id);
        return p;
    }

    private Producto createProducto(Long id) {
        Producto pr = new Producto();
        pr.setId(id);
        return pr;
    }

    private ProductosPedido createProductosPedido(Long id, Long pedidoId, Long productoId, int cantidad) {
        ProductosPedido pp = new ProductosPedido();
        pp.setId(id);
        pp.setCantidad(cantidad);
        pp.setPedido(pedidoId != null ? createPedido(pedidoId) : null);
        pp.setProducto(productoId != null ? createProducto(productoId) : null);
        return pp;
    }

    // Tests

    @Test
    void findAll_returnsList() {
        when(productosPedidoRepository.findAll())
                .thenReturn(List.of(
                        createProductosPedido(1L, 10L, 100L, 2),
                        createProductosPedido(2L, 10L, 101L, 3)
                ));

        List<ProductosPedido> result = productosPedidoService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getCantidad());
        verify(productosPedidoRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsEntity() {
        ProductosPedido pp = createProductosPedido(1L, 10L, 100L, 2);
        when(productosPedidoRepository.findById(1L)).thenReturn(Optional.of(pp));

        ProductosPedido result = productosPedidoService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(2, result.getCantidad());
        verify(productosPedidoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(productosPedidoRepository.findById(99L)).thenReturn(Optional.empty());

        ProductosPedido result = productosPedidoService.findById(99L);

        assertNull(result);
        verify(productosPedidoRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsThroughRepository() {
        ProductosPedido nuevo = createProductosPedido(null, 10L, 100L, 5);

        when(productosPedidoRepository.save(any(ProductosPedido.class)))
                .thenAnswer(invocation -> {
                    ProductosPedido pp = invocation.getArgument(0);
                    pp.setId(10L);
                    return pp;
                });

        ProductosPedido saved = productosPedidoService.save(nuevo);

        assertNotNull(saved.getId());
        assertEquals(5, saved.getCantidad());
        assertEquals(10L, saved.getPedido().getId());
        assertEquals(100L, saved.getProducto().getId());
        verify(productosPedidoRepository, times(1)).save(any(ProductosPedido.class));
    }

    @Test
    void deleteById_callsRepository() {
        doNothing().when(productosPedidoRepository).deleteById(1L);

        productosPedidoService.deleteById(1L);

        verify(productosPedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByPedidoId_deletesOnlyMatchingPedido() {
        ProductosPedido pp1 = createProductosPedido(1L, 10L, 100L, 1); 
        ProductosPedido pp2 = createProductosPedido(2L, 20L, 100L, 2); 
        ProductosPedido pp3 = createProductosPedido(3L, 10L, 101L, 3); 

        when(productosPedidoRepository.findAll())
                .thenReturn(List.of(pp1, pp2, pp3));

        doNothing().when(productosPedidoRepository).deleteById(anyLong());

        productosPedidoService.deleteByPedidoId(10L);

        verify(productosPedidoRepository, times(1)).findAll();
        verify(productosPedidoRepository, times(1)).deleteById(1L);
        verify(productosPedidoRepository, times(1)).deleteById(3L);
        verify(productosPedidoRepository, never()).deleteById(2L);
    }

    @Test
    void deleteByProductoId_deletesOnlyMatchingProducto() {
        ProductosPedido pp1 = createProductosPedido(1L, 10L, 100L, 1); 
        ProductosPedido pp2 = createProductosPedido(2L, 20L, 200L, 2); 
        ProductosPedido pp3 = createProductosPedido(3L, 30L, 100L, 3); 

        when(productosPedidoRepository.findAll())
                .thenReturn(List.of(pp1, pp2, pp3));

        doNothing().when(productosPedidoRepository).deleteById(anyLong());

        productosPedidoService.deleteByProductoId(100L);

        verify(productosPedidoRepository, times(1)).findAll();
        verify(productosPedidoRepository, times(1)).deleteById(1L);
        verify(productosPedidoRepository, times(1)).deleteById(3L);
        verify(productosPedidoRepository, never()).deleteById(2L);
    }
}
