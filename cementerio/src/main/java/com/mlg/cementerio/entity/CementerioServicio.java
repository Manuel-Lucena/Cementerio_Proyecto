package com.mlg.cementerio.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "CEMENTERIO_SERVICIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CementerioServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cementerio", nullable = false)
    private Cementerio cementerio;

    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    private Servicio servicio;

    @Column(nullable = false)
    private Double coste;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}