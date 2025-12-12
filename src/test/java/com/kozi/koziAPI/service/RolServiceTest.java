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

import com.kozi.koziAPI.model.Rol;
import com.kozi.koziAPI.repository.RolRepository;

@SuppressWarnings({ "null", "removal" })
@SpringBootTest
class RolServiceTest {

    @Autowired
    private RolService rolService;

    @MockBean
    private RolRepository rolRepository;

    @MockBean
    private UsuarioService usuarioService;

    // Helpers

    private Rol createRol(Long id, String nombre) {
        Rol rol = new Rol();
        rol.setId(id);
        rol.setNombreRol(nombre);
        return rol;
    }

    // Tests 

    @Test
    void findAll_returnsRolesList() {
        when(rolRepository.findAll())
                .thenReturn(List.of(
                        createRol(1L, "ADMIN"),
                        createRol(2L, "USER")
                ));

        List<Rol> result = rolService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getNombreRol());
        verify(rolRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsRol() {
        Rol rol = createRol(1L, "ADMIN");
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        Rol result = rolService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ADMIN", result.getNombreRol());
        verify(rolRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        Rol result = rolService.findById(99L);

        assertNull(result);
        verify(rolRepository, times(1)).findById(99L);
    }

    @Test
    void save_persistsRolThroughRepository() {
        Rol nuevo = createRol(null, "NEW_ROLE");

        when(rolRepository.save(any(Rol.class)))
                .thenAnswer(invocation -> {
                    Rol r = invocation.getArgument(0);
                    r.setId(10L);
                    return r;
                });

        Rol saved = rolService.save(nuevo);

        assertNotNull(saved.getId());
        assertEquals("NEW_ROLE", saved.getNombreRol());
        verify(rolRepository, times(1)).save(any(Rol.class));
    }

    @Test
    void patchRol_updatesNombreWhenExists() {
        Rol existing = createRol(1L, "OLD_NAME");

        when(rolRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(rolRepository.save(any(Rol.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Rol patch = new Rol();
        patch.setId(1L);
        patch.setNombreRol("NEW_NAME");

        Rol result = rolService.patchRol(patch);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("NEW_NAME", result.getNombreRol());
        verify(rolRepository, times(1)).findById(1L);
        verify(rolRepository, times(1)).save(any(Rol.class));
    }

    @Test
    void patchRol_whenNotFound_returnsNull() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        Rol patch = new Rol();
        patch.setId(99L);
        patch.setNombreRol("IRRELEVANT");

        Rol result = rolService.patchRol(patch);

        assertNull(result);
        verify(rolRepository, times(1)).findById(99L);
        verify(rolRepository, never()).save(any(Rol.class));
    }

    @Test
    void deleteById_callsUsuarioServiceAndRepository() {
        doNothing().when(usuarioService).deleteByRolId(1L);
        doNothing().when(rolRepository).deleteById(1L);

        rolService.deleteById(1L);

        verify(usuarioService, times(1)).deleteByRolId(1L);
        verify(rolRepository, times(1)).deleteById(1L);
    }
}
