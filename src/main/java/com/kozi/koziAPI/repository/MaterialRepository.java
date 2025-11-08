package com.kozi.koziAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kozi.koziAPI.model.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer>{
  
} 