package com.kozi.koziAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kozi.koziAPI.model.Colores;

@Repository
public interface ColoresRepository extends JpaRepository<Colores, Long>{
    
}
