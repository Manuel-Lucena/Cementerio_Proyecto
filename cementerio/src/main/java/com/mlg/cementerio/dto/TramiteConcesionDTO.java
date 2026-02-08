package com.mlg.cementerio.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TramiteConcesionDTO {
    private Integer idUsuario;
    private Long idCliente;

    @NotNull(message = "La sepultura es obligatoria")
    private Integer idSepultura; 

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fechaFin;

    @NotNull(message = "El total a pagar es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double totalPagar;   

    @Valid 
    @NotNull(message = "Los datos del difunto son obligatorios")
    private DifuntoDTO difunto;
}