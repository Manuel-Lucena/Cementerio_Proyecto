package com.mlg.cementerio.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PROVINCIA")
@Data
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "provincia")
    @JsonIgnore 
    private List<Localidad> localidades;
}