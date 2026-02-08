package com.mlg.cementerio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "SERVICIO_CONTRATADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioContratado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_difunto", nullable = false)
    private Difunto difunto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cementerio_servicio", nullable = false)
    private CementerioServicio cementerioServicio;

    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;

    @Column(nullable = false)
    private String estado; 

    @Column(nullable = false)
    private Double coste;
}