package com.kozi.koziAPI.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kozi.koziAPI.model.Membresia;
import com.kozi.koziAPI.model.Rol;
import com.kozi.koziAPI.model.Usuario;
import com.kozi.koziAPI.repository.UsuarioRepository;

@SpringBootTest
@SuppressWarnings({ "null", "removal" })
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private DireccionService direccionService;

    @MockBean
    private PedidoService pedidoService;

    // Helpers

    private Rol createRol(Long id) {
        Rol rol = new Rol();
        rol.setId(id);
        return rol;
    }

    private Membresia createMembresia(Long id) {
        Membresia m = new Membresia();
        m.setId(id);
        return m;
    }

    private Usuario createUsuario(Long id, Long rolId, Long memId) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNombreUsuario("user" + id);
        u.setEmail("user" + id + "@test.com");
        u.setPassword("plainpass");
        u.setFotoPerfil("foto" + id + ".png");
        u.setActivo(true);
        u.setRol(createRol(rolId));
        u.setMembresia(createMembresia(memId));
        return u;
    }

    // Tests

    @Test
    void findAll_returnsUsuariosList() {
        when(usuarioRepository.findAll())
                .thenReturn(List.of(
                        createUsuario(1L, 1L, 1L),
                        createUsuario(2L, 1L, 2L)
                ));

        List<Usuario> result = usuarioService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getNombreUsuario());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsUsuario() {
        Usuario u = createUsuario(1L, 1L, 1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        Usuario result = usuarioService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user1@test.com", result.getEmail());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Usuario result = usuarioService.findById(99L);

        assertNull(result);
        verify(usuarioRepository, times(1)).findById(99L);
    }

    @Test
    void login_whenCredentialsOk_returnsUsuario() {
        Usuario stored = createUsuario(1L, 1L, 1L);
        stored.setPassword(passwordEncoder.encode("plainpass"));

        when(usuarioRepository.findByEmail("user1@test.com")).thenReturn(stored);

        Usuario intento = new Usuario();
        intento.setEmail("user1@test.com");
        intento.setPassword("plainpass");

        Usuario result = usuarioService.login(intento);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(usuarioRepository, times(1)).findByEmail("user1@test.com");
    }

    @Test
    void login_whenWrongPassword_returnsNull() {
        Usuario stored = createUsuario(1L, 1L, 1L);
        stored.setPassword(passwordEncoder.encode("plainpass"));

        when(usuarioRepository.findByEmail("user1@test.com")).thenReturn(stored);

        Usuario intento = new Usuario();
        intento.setEmail("user1@test.com");
        intento.setPassword("wrong");

        Usuario result = usuarioService.login(intento);

        assertNull(result);
        verify(usuarioRepository, times(1)).findByEmail("user1@test.com");
    }

    @Test
    void login_whenUserNotFound_returnsNull() {
        when(usuarioRepository.findByEmail("nouser@test.com")).thenReturn(null);

        Usuario intento = new Usuario();
        intento.setEmail("nouser@test.com");
        intento.setPassword("plainpass");

        Usuario result = usuarioService.login(intento);

        assertNull(result);
        verify(usuarioRepository, times(1)).findByEmail("nouser@test.com");
    }

    @Test
    void save_encryptsPasswordBeforePersist() {
        Usuario nuevo = createUsuario(null, 1L, 1L);
        nuevo.setPassword("plainpass");

        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> {
                    Usuario u = invocation.getArgument(0);
                    u.setId(10L);
                    return u;
                });

        Usuario saved = usuarioService.save(nuevo);

        assertNotNull(saved.getId());
        assertNotEquals("plainpass", saved.getPassword());
        assertTrue(passwordEncoder.matches("plainpass", saved.getPassword()));
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void patchUsuario_updatesOnlyNonNullFields() {
        Usuario existing = createUsuario(1L, 1L, 1L);
        existing.setPassword(passwordEncoder.encode("oldpass"));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario patch = new Usuario();
        patch.setId(1L);
        patch.setNombreUsuario("nuevoNombre");
        patch.setPassword("newpass");
        patch.setActivo(false);
        patch.setRol(createRol(2L));             
        patch.setMembresia(createMembresia(3L)); 

        Usuario result = usuarioService.patchUsuario(patch);

        assertNotNull(result);
        assertEquals("nuevoNombre", result.getNombreUsuario());
        assertEquals("user1@test.com", result.getEmail());      
        assertEquals("foto1.png", result.getFotoPerfil());      
        assertFalse(result.getActivo());
        assertEquals(2L, result.getRol().getId());
        assertEquals(3L, result.getMembresia().getId());
        assertTrue(passwordEncoder.matches("newpass", result.getPassword()));

        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void patchUsuario_whenNotFound_returnsNull() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Usuario patch = new Usuario();
        patch.setId(99L);
        patch.setNombreUsuario("irrelevante");

        Usuario result = usuarioService.patchUsuario(patch);

        assertNull(result);
        verify(usuarioRepository, times(1)).findById(99L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deleteById_cascadesToDireccionAndPedidoAndRepository() {
        doNothing().when(direccionService).deleteByUsuarioId(1L);
        doNothing().when(pedidoService).deleteByUsuarioId(1L);
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.deleteById(1L);

        verify(direccionService, times(1)).deleteByUsuarioId(1L);
        verify(pedidoService, times(1)).deleteByUsuarioId(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByMembresiaId_deletesOnlyMatchingUsers() {
        Usuario u1 = createUsuario(1L, 1L, 10L);
        Usuario u2 = createUsuario(2L, 1L, 20L);
        Usuario u3 = createUsuario(3L, 1L, 10L);

        when(usuarioRepository.findAll()).thenReturn(List.of(u1, u2, u3));
        doNothing().when(usuarioRepository).deleteById(anyLong());

        usuarioService.deleteByMembresiaId(10L);

        verify(usuarioRepository, times(1)).findAll();
        verify(usuarioRepository, times(1)).deleteById(1L);
        verify(usuarioRepository, times(1)).deleteById(3L);
        verify(usuarioRepository, never()).deleteById(2L);
    }

    @Test
    void deleteByRolId_deletesOnlyMatchingUsers() {
        Usuario u1 = createUsuario(1L, 10L, 1L);
        Usuario u2 = createUsuario(2L, 20L, 1L);
        Usuario u3 = createUsuario(3L, 10L, 1L);

        when(usuarioRepository.findAll()).thenReturn(List.of(u1, u2, u3));
        doNothing().when(usuarioRepository).deleteById(anyLong());

        usuarioService.deleteByRolId(10L);

        verify(usuarioRepository, times(1)).findAll();
        verify(usuarioRepository, times(1)).deleteById(1L);
        verify(usuarioRepository, times(1)).deleteById(3L);
        verify(usuarioRepository, never()).deleteById(2L);
    }
}
