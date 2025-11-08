package com.kozi.koziAPI.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombreUsuario", length = 50, nullable = false)
    private String nombreUsuario;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "contraseña", length = 50, nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contraseña;

    @Column(name = "tipoMembresia", length = 20)
    private String tipoMembresia; // STANDARD o VIP

    @Column(name = "activo")
    private Boolean activo = true;
}
