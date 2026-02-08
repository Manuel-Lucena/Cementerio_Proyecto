package com.mlg.cementerio.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SepulturaDTO {
    private Integer id;
    private Integer fila;
    private Integer columna;
    private String codigoVisual;
    private Boolean ocupado;
    private Integer idAgrupacion;
}