package com.mlg.cementerio.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CementerioServicioDTO {

    private Integer id;

    @NotNull(message = "El ID del cementerio es necesario")
    private Integer id_cementerio;

    @NotNull(message = "El ID del servicio es necesario")
    private Integer id_servicio;

    private String descripcion;
    private String nombreServicio;


    @NotNull(message = "El coste es obligatorio")
    @PositiveOrZero(message = "El coste no puede ser un valor negativo")
    private Double coste;
}