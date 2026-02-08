package com.mlg.cementerio.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "LOCALIDAD")
@Data
public class Localidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_provincia")
    private Provincia provincia;
}