package com.kozi.koziAPI.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "total", nullable = false)
    private Double total = 0.0;

    @Column(name = "estado", length = 20)
    private String estado; // CREADA, PAGADA, CANCELADA

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Sin cascade para evitar que JPA intente persistir items antes de tiempo
    @OneToMany(mappedBy = "orden")
    private List<OrdenItem> items = new ArrayList<>();
}
