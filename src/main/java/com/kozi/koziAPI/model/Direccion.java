package com.kozi.koziAPI.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "direccion")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Direccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombreCalle;

    @Column(nullable = true, length = 10)
    private Integer numeroCalle;

    @ManyToOne
    @JoinColumn(name = "comuna", nullable = false)
    private Comuna comuna;

    @ManyToOne
    @JoinColumn(name = "usuario", nullable = false)
    private Usuario usuario;
}
