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

    @Autowired
    private ProductosPedidoService productosPedidoService;

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedido findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        return pedido;
    }

    public List<Pedido> findByUsuarioId(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
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
        productosPedidoService.deleteByPedidoId(id);
        pedidoRepository.deleteById(id);
    }

    public void deleteByUsuarioId(Long usuarioId) {
        List<Pedido> pedidos = pedidoRepository.findAll();
        for (Pedido pedido : pedidos) {
            if (pedido.getUsuario() != null && pedido.getUsuario().getId().equals(usuarioId)) {
                pedidoRepository.deleteById(pedido.getId());
            }
        }
    }

    public void deleteByPagoId(Long pagoId) {
        List<Pedido> pedidos = pedidoRepository.findAll();
        for (Pedido pedido : pedidos) {
            if (pedido.getPago() != null && pedido.getPago().getId().equals(pagoId)) {
                pedidoRepository.deleteById(pedido.getId());
            }
        }
    }

    public void deleteByEnvioId(Long envioId) {
        List<Pedido> pedidos = pedidoRepository.findAll();
        for (Pedido pedido : pedidos) {
            if (pedido.getEnvio() != null && pedido.getEnvio().getId().equals(envioId)) {
                pedidoRepository.deleteById(pedido.getId());
            }
        }
    }

    public void deleteByEstadoId(Long estadoId) {
        List<Pedido> pedidos = pedidoRepository.findAll();
        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() != null && pedido.getEstado().getId().equals(estadoId)) {
                pedidoRepository.deleteById(pedido.getId());
            }
        }
    }
}
