package com.kozi.koziAPI.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.kozi.koziAPI.model.Categorias;
import com.kozi.koziAPI.model.Producto;
import com.kozi.koziAPI.repository.ProductoRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class ProductoServiceTest {

    @Autowired
    private ProductoService productoService;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private ProductosPedidoService productosPedidoService;

    @MockBean
    private MaterialesService materialesService;

    @MockBean
    private EstilosService estilosService;

    @MockBean
    private CategoriasService categoriasService;

    @MockBean
    private ColoresService coloresService;

    // Helpers

    private Producto createProducto(Long id, String nombre, Double precio, String img, String desc, Integer stock) {
        Producto p = new Producto();
        p.setId(id);
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setImagenUrl(img);
        p.setDescripcion(desc);
        p.setStock(stock);
        return p;
    }

    private Categorias createCategoriaRelacion(Long prodId) {
        Categorias c = new Categorias();
        Producto p = new Producto();
        p.setId(prodId);
        c.setProducto(p);
        return c;
    }

    // Tests

    @Test
    void findAll_returnsProductList() {
        when(productoRepository.findAll())
                .thenReturn(List.of(
                        createProducto(1L, "P1", 1000.0, "img1", "desc1", 10),
                        createProducto(2L, "P2", 2000.0, "img2", "desc2", 20)
                ));

        List<Producto> result = productoService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("P1", result.get(0).getNombre());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsProduct() {
        Producto p = createProducto(1L, "P1", 1000.0, "img1", "desc1", 10);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));

        Producto result = productoService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Producto result = productoService.findById(99L);

        assertNull(result);
        verify(productoRepository, times(1)).findById(99L);
    }

    @Test
    void findByCategoriaId_returnsProductsFromRelations() {
        Categorias rel1 = createCategoriaRelacion(10L);
        Categorias rel2 = createCategoriaRelacion(20L);
        Categorias rel3 = createCategoriaRelacion(10L); // repetido, debe evitar duplicados

        when(categoriasService.findByCategoriaId(5L))
                .thenReturn(List.of(rel1, rel2, rel3));

        List<Producto> result = productoService.findByCategoriaId(5L);

        assertNotNull(result);
        assertEquals(2, result.size()); // evita duplicados

        assertTrue(result.stream().anyMatch(p -> p.getId().equals(10L)));
        assertTrue(result.stream().anyMatch(p -> p.getId().equals(20L)));

        verify(categoriasService, times(1)).findByCategoriaId(5L);
    }

    @Test
    void findUltimos6Productos_callsRepositoryMethod() {
        when(productoRepository.findTop6ByOrderByIdDesc())
                .thenReturn(new ArrayList<>());

        productoService.findUltimos6Productos();

        verify(productoRepository, times(1)).findTop6ByOrderByIdDesc();
    }

    @Test
    void save_persistsThroughRepository() {
        Producto nuevo = createProducto(null, "Nuevo", 5000.0, "img", "desc", 10);

        when(productoRepository.save(any(Producto.class)))
                .thenAnswer(invocation -> {
                    Producto p = invocation.getArgument(0);
                    p.setId(100L);
                    return p;
                });

        Producto saved = productoService.save(nuevo);

        assertNotNull(saved.getId());
        assertEquals("Nuevo", saved.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void patchProducto_updatesOnlyNonNullFields() {
        Producto existing = createProducto(1L, "Old", 1000.0, "imgOld", "descOld", 10);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productoRepository.save(any(Producto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Producto patch = new Producto();
        patch.setId(1L);
        patch.setNombre("NewName");
        patch.setPrecio(3000.0);
        patch.setImagenUrl("imgNew");
        patch.setDescripcion("descNew");
        patch.setStock(50);

        Producto result = productoService.patchProducto(patch);

        assertNotNull(result);
        assertEquals("NewName", result.getNombre());
        assertEquals(3000.0, result.getPrecio());
        assertEquals("imgNew", result.getImagenUrl());
        assertEquals("descNew", result.getDescripcion());
        assertEquals(50, result.getStock());

        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void patchProducto_whenNotFound_returnsNull() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Producto patch = new Producto();
        patch.setId(99L);

        Producto result = productoService.patchProducto(patch);

        assertNull(result);
        verify(productoRepository, times(1)).findById(99L);
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void deleteById_callsAllCascadeServicesAndRepository() {
        doNothing().when(materialesService).deleteByProductoId(1L);
        doNothing().when(estilosService).deleteByProductoId(1L);
        doNothing().when(categoriasService).deleteByProductoId(1L);
        doNothing().when(coloresService).deleteByProductoId(1L);
        doNothing().when(productosPedidoService).deleteByProductoId(1L);
        doNothing().when(productoRepository).deleteById(1L);

        productoService.deleteById(1L);

        verify(materialesService, times(1)).deleteByProductoId(1L);
        verify(estilosService, times(1)).deleteByProductoId(1L);
        verify(categoriasService, times(1)).deleteByProductoId(1L);
        verify(coloresService, times(1)).deleteByProductoId(1L);
        verify(productosPedidoService, times(1)).deleteByProductoId(1L);
        verify(productoRepository, times(1)).deleteById(1L);
    }
}
