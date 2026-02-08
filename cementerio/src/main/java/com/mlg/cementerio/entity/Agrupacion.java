package com.mlg.cementerio.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "AGRUPACION")
@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Agrupacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false)
    private Integer filas;

    @Column(nullable = false)
    private Integer columnas;

    private String descripcion;

    @Column(name = "coordenadas_mapa", columnDefinition = "TEXT")
    private String coordenadas_mapa;

    @Column(name = "tipo_forma", length = 20)
    private String tipo_forma;

   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cementerio", nullable = false)
    private Cementerio cementerio;

    @OneToMany(mappedBy = "agrupacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sepultura> sepulturas;
}