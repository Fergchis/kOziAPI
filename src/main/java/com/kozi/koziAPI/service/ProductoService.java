package com.kozi.koziAPI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kozi.koziAPI.model.Categorias;
import com.kozi.koziAPI.model.Producto;
import com.kozi.koziAPI.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
@SuppressWarnings("null")
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductosPedidoService productosPedidoService;

    @Autowired
    private MaterialesService materialesService;

    @Autowired
    private EstilosService estilosService;

    @Autowired
    private CategoriasService categoriasService;

    @Autowired
    private ColoresService coloresService;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto findById(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        return producto;
    }

    public List<Producto> findByCategoriaId(Long categoriaId) {
        List<Categorias> relaciones = categoriasService.findByCategoriaId(categoriaId);
        List<Producto> productos = new ArrayList<>();

        for (Categorias relacion : relaciones) {
            if (relacion.getProducto() != null && !productos.contains(relacion.getProducto())) {
                productos.add(relacion.getProducto());
            }
        }

        return productos;
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto patchProducto(Producto producto) {
        Producto existingProducto = productoRepository.findById(producto.getId()).orElse(null);
        if (existingProducto != null) {
            if (producto.getNombre() != null) {
                existingProducto.setNombre(producto.getNombre());
            }
            if (producto.getPrecio() != null) {
                existingProducto.setPrecio(producto.getPrecio());
            }
            if (producto.getImagenUrl() != null) {
                existingProducto.setImagenUrl(producto.getImagenUrl());
            }
            if (producto.getDescripcion() != null) {
                existingProducto.setDescripcion(producto.getDescripcion());
            }
            if (producto.getStock() != null) {
                existingProducto.setStock(producto.getStock());
            }
            return productoRepository.save(existingProducto);
        }
        return null;
    }

    public void deleteById(Long id) {
        materialesService.deleteByProductoId(id);
        estilosService.deleteByProductoId(id);
        categoriasService.deleteByProductoId(id);
        coloresService.deleteByProductoId(id);
        productosPedidoService.deleteByProductoId(id);
        productoRepository.deleteById(id);
    }
}
