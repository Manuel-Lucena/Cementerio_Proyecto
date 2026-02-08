package com.mlg.cementerio.entity;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EMPRESA")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;

    @Column(nullable = false)
    private String nombre;

    private String direccion;
    private String telefono;
    private String email;

    @Column(name = "años_reutilizar_nichos")
    private Integer añosReutilizarNichos;

    @Column(columnDefinition = "LONGTEXT")
    private String imagen;

    @OneToMany(mappedBy = "empresa", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties("empresa")
    private List<Cementerio> cementerios;
}