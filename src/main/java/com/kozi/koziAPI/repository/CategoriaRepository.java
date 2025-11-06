package com.kozi.koziAPI.repository;

import com.kozi.koziAPI.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
