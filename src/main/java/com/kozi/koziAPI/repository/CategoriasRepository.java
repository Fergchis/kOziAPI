package com.kozi.koziAPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kozi.koziAPI.model.Categorias;

@Repository
public interface CategoriasRepository extends JpaRepository<Categorias, Long>{
    
    List<Categorias> findByCategoriaId(Long categoriaId);
}