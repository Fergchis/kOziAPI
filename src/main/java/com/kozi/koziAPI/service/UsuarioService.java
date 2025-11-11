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
        usuarioRepository.deleteById(id);
    }
}
