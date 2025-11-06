package com.kozi.koziAPI.repository;

import com.kozi.koziAPI.model.Categoria;
import com.kozi.koziAPI.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByCategoria(Categoria categoria);
}
