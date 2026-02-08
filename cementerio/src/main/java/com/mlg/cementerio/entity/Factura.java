package com.mlg.cementerio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "FACTURA")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_sepultura", nullable = false)
    private Sepultura sepultura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_concesion", nullable = true)
    private Concesion concesion;

    @Column(name = "fecha_factura", nullable = false)
    private LocalDate fechaFactura;

    private Double total;
    private String estado;

    private String concepto;
}