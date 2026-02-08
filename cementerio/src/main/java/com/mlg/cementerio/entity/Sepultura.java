package com.mlg.cementerio.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "SEPULTURA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sepultura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer fila;

    @Column(nullable = false)
    private Integer columna;

    @Column(name = "codigo_visual", length = 20)
    private String codigo_visual;

    @Column(nullable = false)
    private Boolean ocupado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agrupacion", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Agrupacion agrupacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_concesion")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Concesion concesion;
}