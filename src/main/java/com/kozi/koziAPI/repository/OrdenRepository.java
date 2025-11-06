package com.kozi.koziAPI.repository;

import com.kozi.koziAPI.model.Orden;
import com.kozi.koziAPI.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Integer> {
    List<Orden> findByUsuario(Usuario usuario);
}
