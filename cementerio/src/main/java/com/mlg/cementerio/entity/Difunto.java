package com.mlg.cementerio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "DIFUNTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Difunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_concesion", nullable = false)
    private Concesion concesion;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(name = "fecha_fallecimiento", nullable = false)
    private LocalDate fechaFallecimiento;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Builder.Default 
    @Column(name = "estado")
    private String estado = "Pendiente";
}