package com.mlg.cementerio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "CLIENTE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "idUsuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false)
    private String nombre;

    private String apellidos;
    private String email;
    private String direccion;
    private String telefono;
    
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento; 
}