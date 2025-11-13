package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Usuario;
import com.kozi.koziAPI.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DireccionService direccionService;

    @Autowired
    private PedidoService pedidoService;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        return usuario;
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario patchUsuario(Usuario usuario) {
        Usuario existingUsuario = usuarioRepository.findById(usuario.getId()).orElse(null);
        if (existingUsuario != null) {
            if (usuario.getNombreUsuario() != null) {
                existingUsuario.setNombreUsuario(usuario.getNombreUsuario());
            }
            if (usuario.getEmail() != null) {
                existingUsuario.setEmail(usuario.getEmail());
            }
            if (usuario.getContraseña() != null) {
                existingUsuario.setContraseña(usuario.getContraseña());
            }
            if (usuario.getTipoMembresia() != null) {
                existingUsuario.setTipoMembresia(usuario.getTipoMembresia());
            }
            if (usuario.getActivo() != null) { //esto es necesario???
                existingUsuario.setActivo(usuario.getActivo());
            }
            return usuarioRepository.save(existingUsuario);
        }
        return null;
    }

    public void deleteById(Long id) {
        direccionService.deleteByUsuarioId(id);
        pedidoService.deleteByUsuarioId(id);
        usuarioRepository.deleteById(id);
    }

    public void deleteByMembresiaId(Long membresiaId) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario usuario : usuarios) {
            if (usuario.getMembresia() != null && usuario.getMembresia().getId().equals(membresiaId)) {
                usuarioRepository.deleteById(usuario.getId());
            }
        }
    }

    public void deleteByRolId(Long rolId) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario usuario : usuarios) {
            if (usuario.getRol() != null && usuario.getRol().getId().equals(rolId)) {
                usuarioRepository.deleteById(usuario.getId());
            }
        }
    }
}
