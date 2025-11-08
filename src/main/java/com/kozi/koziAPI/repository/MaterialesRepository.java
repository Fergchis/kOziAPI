package com.kozi.koziAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kozi.koziAPI.model.Materiales;

@Repository
public interface MaterialesRepository extends JpaRepository<Materiales, Integer>{
    
}
