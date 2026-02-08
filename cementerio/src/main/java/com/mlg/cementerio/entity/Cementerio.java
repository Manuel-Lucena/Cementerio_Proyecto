package com.mlg.cementerio.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CEMENTERIO")
public class Cementerio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    private String direccion;

    @Column(name = "imagen_plano")
    private String imagenPlano;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    @JsonIgnoreProperties("cementerios")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "id_localidad")
    private Localidad localidad;

    @Builder.Default  
    @OneToMany(mappedBy = "cementerio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("cementerio") 
    private List<CementerioServicio> servicios = new ArrayList<>();

    @Builder.Default  
    @OneToMany(mappedBy = "cementerio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("cementerio")
    private List<Agrupacion> agrupaciones = new ArrayList<>();
}