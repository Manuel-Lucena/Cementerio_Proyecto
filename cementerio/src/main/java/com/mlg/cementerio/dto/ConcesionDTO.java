package com.mlg.cementerio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConcesionDTO {
    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Double precio;
    private boolean activa;
    private String codigoSepultura;
    private String nombreCementerio;
    private String tipoServicio;
}