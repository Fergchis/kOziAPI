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
import com.kozi.koziAPI.model.Estilos;
import com.kozi.koziAPI.model.Producto;
import com.kozi.koziAPI.repository.EstilosRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class EstilosServiceTest {

    @Autowired
    private EstilosService estilosService;

    @MockBean
    private EstilosRepository estilosRepository;

    // Helpers

    private Producto createProducto(Long id) {
        Producto p = new Producto();
        p.setId(id);
        return p;
    }

    private Estilo createEstilo(Long id) {
        Estilo e = new Estilo();
        e.setId(id);
        return e;
    }

    private Estilos createEstilos(Long id, Long productoId, Long estiloId) {
        Estilos es = new Estilos();
        es.setId(id);
        es.setProducto(createProducto(productoId));
        es.setEstilo(createEstilo(estiloId));
        return es;
    }

    // Tests 

    @Test
    void findAll_returnsList() {
        when(estilosRepository.findAll())
                .thenReturn(List.of(
                        createEstilos(1L, 10L, 100L),
                        createEstilos(2L, 20L, 200L)
                ));

        List<Estilos> result = estilosService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(estilosRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsEntity() {
        Estilos estilos = createEstilos(1L, 10L, 100L);
        when(estilosRepository.findById(1L)).thenReturn(Optional.of(estilos));

        Estilos result = estilosService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(estilosRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(estilosRepository.findById(99L)).thenReturn(Optional.empty());

        Estilos result = estilosService.findById(99L);

        assertNull(result);
        verify(estilosRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsThroughRepository() {
        Estilos nuevo = createEstilos(null, 10L, 100L);

        when(estilosRepository.save(any(Estilos.class)))
                .thenAnswer(invocation -> {
                    Estilos es = invocation.getArgument(0);
                    es.setId(50L);
                    return es;
                });

        Estilos result = estilosService.save(nuevo);

        assertNotNull(result.getId());
        verify(estilosRepository, times(1)).save(any(Estilos.class));
    }

    @Test
    void deleteById_callsRepositoryDelete() {
        doNothing().when(estilosRepository).deleteById(1L);

        estilosService.deleteById(1L);

        verify(estilosRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByProductoId_deletesOnlyMatchingProducto() {
        Estilos e1 = createEstilos(1L, 10L, 100L);
        Estilos e2 = createEstilos(2L, 20L, 100L); 
        Estilos e3 = createEstilos(3L, 10L, 200L); 

        when(estilosRepository.findAll())
                .thenReturn(List.of(e1, e2, e3));

        estilosService.deleteByProductoId(10L);

        verify(estilosRepository, times(1)).findAll();
        verify(estilosRepository, times(1)).deleteById(1L);
        verify(estilosRepository, times(1)).deleteById(3L);
        verify(estilosRepository, never()).deleteById(2L);
    }

    @Test
    void deleteByEstiloId_deletesOnlyMatchingEstilo() {
        Estilos e1 = createEstilos(1L, 10L, 100L); 
        Estilos e2 = createEstilos(2L, 10L, 200L);
        Estilos e3 = createEstilos(3L, 20L, 100L);

        when(estilosRepository.findAll())
                .thenReturn(List.of(e1, e2, e3));

        estilosService.deleteByEstiloId(100L);

        verify(estilosRepository, times(1)).findAll();
        verify(estilosRepository, times(1)).deleteById(1L);
        verify(estilosRepository, times(1)).deleteById(3L);
        verify(estilosRepository, never()).deleteById(2L);
    }
}
