package com.kozi.koziAPI.model;

import java.time.LocalDateTime;

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
@Table(name = "pedido")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private LocalDateTime fechaCreacion;
    
    @Column(nullable = false, length = 7)
    private Double total;

    @ManyToOne
    @JoinColumn(name = "usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "estado", nullable = false)
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "envio", nullable = false)
    private Envio envio;

    @ManyToOne
    @JoinColumn(name = "pago", nullable = false)
    private Pago pago;
}
