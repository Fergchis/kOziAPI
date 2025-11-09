package com.kozi.koziAPI.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, nullable = false)
    private String nombre;

    @Column(length = 7, nullable = false)
    private Double precio;

    @Column(length = 100, nullable = false)
    private String imagenUrl;

    @Column(length = 100, nullable = false)
    private String descripcion;

    @Column(name = "stock", nullable = false)
    private Integer stock;
}
