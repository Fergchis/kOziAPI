package com.kozi.koziAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kozi.koziAPI.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
