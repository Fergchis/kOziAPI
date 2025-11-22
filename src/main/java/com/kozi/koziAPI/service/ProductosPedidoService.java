package com.kozi.koziAPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.ProductosPedido;
import com.kozi.koziAPI.repository.ProductosPedidoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class ProductosPedidoService {
    
    @Autowired
    private ProductosPedidoRepository productosPedidoRepository;

    public List<ProductosPedido> findAll() {
        return productosPedidoRepository.findAll();
    }

    public ProductosPedido findById(Long id) {
        ProductosPedido productosPedido = productosPedidoRepository.findById(id).orElse(null);
        return productosPedido;
    }

    public ProductosPedido save(ProductosPedido productosPedido) {
        return productosPedidoRepository.save(productosPedido);
    }

    public void deleteById(Long id) {
        productosPedidoRepository.deleteById(id);
    }

    public void deleteByPedidoId(Long pedidoId) {
        List<ProductosPedido> productosPedidos = productosPedidoRepository.findAll();
        for (ProductosPedido productosPedido : productosPedidos) {
            if (productosPedido.getPedido() != null && productosPedido.getPedido().getId().equals(pedidoId)){
                productosPedidoRepository.deleteById(productosPedido.getId());
            }
        }
    }

    public void deleteByProductoId(Long productoId) {
        List<ProductosPedido> productosPedidos = productosPedidoRepository.findAll();
        for (ProductosPedido productosPedido : productosPedidos) {
            if (productosPedido.getProducto() != null && productosPedido.getProducto().getId().equals(productoId)){
                productosPedidoRepository.deleteById(productosPedido.getId());
            }
        }
    }
}
