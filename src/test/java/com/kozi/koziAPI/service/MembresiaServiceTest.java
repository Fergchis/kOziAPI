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

import com.kozi.koziAPI.model.Membresia;
import com.kozi.koziAPI.repository.MembresiaRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class MembresiaServiceTest {

    @Autowired
    private MembresiaService membresiaService;

    @MockBean
    private MembresiaRepository membresiaRepository;

    @MockBean
    private UsuarioService usuarioService;

    // Helper 

    private Membresia createMembresia(Long id, String tipo) {
        Membresia m = new Membresia();
        m.setId(id);
        m.setTipoMembresia(tipo);
        return m;
    }

    // Tests

    @Test
    void findAll_returnsListOfMembresias() {
        when(membresiaRepository.findAll())
                .thenReturn(List.of(
                        createMembresia(1L, "VIP"),
                        createMembresia(2L, "Premium")
                ));

        List<Membresia> result = membresiaService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("VIP", result.get(0).getTipoMembresia());
        verify(membresiaRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsMembresia() {
        Membresia membresia = createMembresia(1L, "VIP");

        when(membresiaRepository.findById(1L))
                .thenReturn(Optional.of(membresia));

        Membresia result = membresiaService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("VIP", result.getTipoMembresia());
        verify(membresiaRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(membresiaRepository.findById(999L))
                .thenReturn(Optional.empty());

        Membresia result = membresiaService.findById(999L);

        assertNull(result);
        verify(membresiaRepository, times(1)).findById(999L);
    }

    @Test
    void save_persistsThroughRepository() {
        Membresia nueva = createMembresia(null, "Gold");

        when(membresiaRepository.save(any(Membresia.class)))
                .thenAnswer(invocation -> {
                    Membresia m = invocation.getArgument(0);
                    m.setId(10L);
                    return m;
                });

        Membresia result = membresiaService.save(nueva);

        assertNotNull(result.getId());
        assertEquals("Gold", result.getTipoMembresia());
        verify(membresiaRepository, times(1)).save(any(Membresia.class));
    }

    @Test
    void patchMembresia_updatesFieldsWhenExists() {
        Membresia existing = createMembresia(1L, "VIP");

        when(membresiaRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(membresiaRepository.save(any(Membresia.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Membresia patch = new Membresia();
        patch.setId(1L);
        patch.setTipoMembresia("Premium");

        Membresia result = membresiaService.patchMembresia(patch);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Premium", result.getTipoMembresia());
        verify(membresiaRepository, times(1)).findById(1L);
        verify(membresiaRepository, times(1)).save(any(Membresia.class));
    }

    @Test
    void patchMembresia_whenNotFound_returnsNull() {
        when(membresiaRepository.findById(500L))
                .thenReturn(Optional.empty());

        Membresia patch = new Membresia();
        patch.setId(500L);
        patch.setTipoMembresia("Irrelevante");

        Membresia result = membresiaService.patchMembresia(patch);

        assertNull(result);
        verify(membresiaRepository, times(1)).findById(500L);
        verify(membresiaRepository, never()).save(any(Membresia.class));
    }

    @Test
    void deleteById_callsUsuarioServiceAndRepository() {
        doNothing().when(usuarioService).deleteByMembresiaId(1L);
        doNothing().when(membresiaRepository).deleteById(1L);

        membresiaService.deleteById(1L);

        verify(usuarioService, times(1)).deleteByMembresiaId(1L);
        verify(membresiaRepository, times(1)).deleteById(1L);
    }
}
