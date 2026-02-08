package com.mlg.cementerio.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgrupacionDTO {
    private Integer id;

    @NotBlank(message = "El nombre del sector es obligatorio")
    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    private String nombre;

    @NotNull(message = "El número de filas es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 fila")
    private Integer filas;

    @NotNull(message = "El número de columnas es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 columna")
    private Integer columnas;

    private String descripcion;

    @NotBlank(message = "Las coordenadas del mapa son obligatorias")
    private String coordenadas_mapa;

    @NotBlank(message = "El tipo de forma es obligatorio")
    private String tipo_forma;

    @NotNull(message = "El sector debe estar vinculado a un cementerio")
    private Integer id_cementerio;
}