package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Pedido;
import com.kozi.koziAPI.repository.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedido findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        return pedido;
    }

    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Pedido patchPedido(Pedido pedido) {
        Pedido existingPedido = pedidoRepository.findById(pedido.getId()).orElse(null);
        if (existingPedido != null) {
            if (pedido.getFechaCreacion() != null) {
                existingPedido.setFechaCreacion(pedido.getFechaCreacion());
            }
            if (pedido.getTotal() != null) {
                existingPedido.setTotal(pedido.getTotal());
            }
            return pedidoRepository.save(existingPedido);
        }
        return null;
    }

    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }

}
