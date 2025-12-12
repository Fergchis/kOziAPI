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

import com.kozi.koziAPI.model.Comuna;
import com.kozi.koziAPI.model.Direccion;
import com.kozi.koziAPI.model.Usuario;
import com.kozi.koziAPI.repository.DireccionRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class DireccionServiceTest {

    @Autowired
    private DireccionService direccionService;

    @MockBean
    private DireccionRepository direccionRepository;

    // Helpers

    private Comuna createComuna(Long id) {
        Comuna c = new Comuna();
        c.setId(id);
        c.setNombre("Comuna " + id);
        return c;
    }

    private Usuario createUsuario(Long id) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNombreUsuario("user" + id);
        return u;
    }

    private Direccion createDireccion(Long id, Long comunaId, Long usuarioId) {
        Direccion d = new Direccion();
        d.setId(id);
        d.setNombreCalle("Calle " + id);
        d.setNumeroCalle(100);
        d.setComuna(createComuna(comunaId));
        d.setUsuario(createUsuario(usuarioId));
        return d;
    }

    // Tests

    @Test
    void findAll_returnsListOfDirecciones() {
        when(direccionRepository.findAll())
                .thenReturn(List.of(
                        createDireccion(1L, 10L, 100L),
                        createDireccion(2L, 20L, 200L)
                ));

        List<Direccion> result = direccionService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(direccionRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsDireccion() {
        Direccion dir = createDireccion(1L, 10L, 100L);
        when(direccionRepository.findById(1L)).thenReturn(Optional.of(dir));

        Direccion result = direccionService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Calle 1", result.getNombreCalle());
        verify(direccionRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(direccionRepository.findById(99L)).thenReturn(Optional.empty());

        Direccion result = direccionService.findById(99L);

        assertNull(result);
        verify(direccionRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsDireccion() {
        Direccion nueva = createDireccion(null, 10L, 100L);

        when(direccionRepository.save(any(Direccion.class)))
                .thenAnswer(invocation -> {
                    Direccion d = invocation.getArgument(0);
                    d.setId(50L);
                    return d;
                });

        Direccion result = direccionService.save(nueva);

        assertNotNull(result.getId());
        verify(direccionRepository, times(1)).save(any(Direccion.class));
    }

    @Test
    void patchDireccion_updatesFields_whenExists() {
        Direccion existing = createDireccion(1L, 10L, 100L);
        when(direccionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(direccionRepository.save(any(Direccion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Direccion patch = new Direccion();
        patch.setId(1L);
        patch.setNombreCalle("Nueva Calle");
        patch.setNumeroCalle(999);

        Direccion result = direccionService.patchDireccion(patch);

        assertNotNull(result);
        assertEquals("Nueva Calle", result.getNombreCalle());
        assertEquals(999, result.getNumeroCalle());
        verify(direccionRepository, times(1)).findById(1L);
        verify(direccionRepository, times(1)).save(any(Direccion.class));
    }

    @Test
    void patchDireccion_returnsNull_whenNotExists() {
        when(direccionRepository.findById(99L)).thenReturn(Optional.empty());

        Direccion patch = new Direccion();
        patch.setId(99L);

        Direccion result = direccionService.patchDireccion(patch);

        assertNull(result);
        verify(direccionRepository, times(1)).findById(99L);
        verify(direccionRepository, never()).save(any(Direccion.class));
    }

    @Test
    void deleteById_callsRepositoryDelete() {
        doNothing().when(direccionRepository).deleteById(1L);

        direccionService.deleteById(1L);

        verify(direccionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByComunaId_deletesMatchingDirecciones() {
        Direccion d1 = createDireccion(1L, 10L, 100L); 
        Direccion d2 = createDireccion(2L, 20L, 100L); 
        Direccion d3 = createDireccion(3L, 10L, 200L); 

        when(direccionRepository.findAll())
                .thenReturn(List.of(d1, d2, d3));

        direccionService.deleteByComunaId(10L);

        verify(direccionRepository, times(1)).findAll();
        verify(direccionRepository, times(1)).deleteById(1L);
        verify(direccionRepository, times(1)).deleteById(3L);
        verify(direccionRepository, never()).deleteById(2L);
    }

    @Test
    void deleteByUsuarioId_deletesMatchingDirecciones() {
        Direccion d1 = createDireccion(1L, 10L, 100L); 
        Direccion d2 = createDireccion(2L, 10L, 200L); 
        Direccion d3 = createDireccion(3L, 10L, 100L); 

        when(direccionRepository.findAll())
                .thenReturn(List.of(d1, d2, d3));

        direccionService.deleteByUsuarioId(100L);

        verify(direccionRepository, times(1)).findAll();
        verify(direccionRepository, times(1)).deleteById(1L);
        verify(direccionRepository, times(1)).deleteById(3L);
        verify(direccionRepository, never()).deleteById(2L);
    }
}
