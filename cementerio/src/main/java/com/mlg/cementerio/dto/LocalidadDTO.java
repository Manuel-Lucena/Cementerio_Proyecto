package com.mlg.cementerio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class LocalidadDTO {
    private Integer id;
    private String nombre;
    private Integer idProvincia;
}