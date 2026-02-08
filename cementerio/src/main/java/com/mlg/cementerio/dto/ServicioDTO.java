package com.mlg.cementerio.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
}