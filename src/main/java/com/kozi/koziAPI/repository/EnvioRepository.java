package com.kozi.koziAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kozi.koziAPI.model.Envio;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long>{

} 