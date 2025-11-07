package com.kozi.koziAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kozi.koziAPI.model.Pedido;

@Repository
public interface PedidoItemRepository extends JpaRepository<Pedido, Integer>{
    
}
