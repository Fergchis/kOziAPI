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

import com.kozi.koziAPI.model.Color;
import com.kozi.koziAPI.model.Colores;
import com.kozi.koziAPI.model.Producto;
import com.kozi.koziAPI.repository.ColoresRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class ColoresServiceTest {

    @Autowired
    private ColoresService coloresService;

    @MockBean
    private ColoresRepository coloresRepository;

    // Helpers

    private Producto createProducto(Long id) {
        Producto p = new Producto();
        p.setId(id);
        p.setNombre("Producto " + id);
        p.setPrecio(1000.0);
        p.setImagenUrl("img.png");
        p.setDescripcion("Desc");
        p.setStock(10);
        return p;
    }

    private Color createColor(Long id, String nombre) {
        Color c = new Color();
        c.setId(id);
        c.setNombre(nombre);
        return c;
    }

    private Colores createColores(Long id, Long productoId, Long colorId) {
        Colores c = new Colores();
        c.setId(id);
        c.setProducto(createProducto(productoId));
        c.setColor(createColor(colorId, "Color " + colorId));
        return c;
    }

    // Tests

    @Test
    void findAll_returnsListOfColores() {
        when(coloresRepository.findAll())
                .thenReturn(List.of(
                        createColores(1L, 10L, 100L),
                        createColores(2L, 20L, 200L)
                ));

        List<Colores> result = coloresService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(coloresRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsColores() {
        Colores colores = createColores(1L, 10L, 100L);
        when(coloresRepository.findById(1L)).thenReturn(Optional.of(colores));

        Colores result = coloresService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(10L, result.getProducto().getId());
        assertEquals(100L, result.getColor().getId());
        verify(coloresRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(coloresRepository.findById(99L)).thenReturn(Optional.empty());

        Colores result = coloresService.findById(99L);

        assertNull(result);
        verify(coloresRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsColores() {
        Colores nuevo = createColores(null, 10L, 100L);

        when(coloresRepository.save(any(Colores.class)))
                .thenAnswer(invocation -> {
                    Colores c = invocation.getArgument(0);
                    c.setId(50L);
                    return c;
                });

        Colores result = coloresService.save(nuevo);

        assertNotNull(result.getId());
        assertEquals(10L, result.getProducto().getId());
        assertEquals(100L, result.getColor().getId());
        verify(coloresRepository, times(1)).save(any(Colores.class));
    }

    @Test
    void deleteById_callsRepositoryDelete() {
        doNothing().when(coloresRepository).deleteById(1L);

        coloresService.deleteById(1L);

        verify(coloresRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByProductoId_deletesOnlyMatchingProducto() {
        Colores c1 = createColores(1L, 10L, 100L); 
        Colores c2 = createColores(2L, 20L, 100L); 
        Colores c3 = createColores(3L, 10L, 200L); 

        when(coloresRepository.findAll())
                .thenReturn(List.of(c1, c2, c3));

        coloresService.deleteByProductoId(10L);

        verify(coloresRepository, times(1)).findAll();
        verify(coloresRepository, times(1)).deleteById(1L);
        verify(coloresRepository, times(1)).deleteById(3L);
        verify(coloresRepository, never()).deleteById(2L);
    }

    @Test
    void deleteByColorId_deletesOnlyMatchingColor() {
        Colores c1 = createColores(1L, 10L, 100L); 
        Colores c2 = createColores(2L, 20L, 200L); 
        Colores c3 = createColores(3L, 30L, 100L); 

        when(coloresRepository.findAll())
                .thenReturn(List.of(c1, c2, c3));

        coloresService.deleteByColorId(100L);

        verify(coloresRepository, times(1)).findAll();
        verify(coloresRepository, times(1)).deleteById(1L);
        verify(coloresRepository, times(1)).deleteById(3L);
        verify(coloresRepository, never()).deleteById(2L);
    }
}
