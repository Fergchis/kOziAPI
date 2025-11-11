package com.kozi.koziAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kozi.koziAPI.model.Membresia;

@Repository
public interface MembresiaRepository extends JpaRepository<Membresia, Long>{

}
