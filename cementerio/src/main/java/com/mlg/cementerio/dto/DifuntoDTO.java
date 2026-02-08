package com.mlg.cementerio.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DifuntoDTO {
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotNull(message = "La fecha de fallecimiento es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser posteior a la actual")
    private LocalDate fechaFallecimiento;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDate fechaIngreso;

    private String ubicacion;
    private Integer idCementerio;
    private String nombreCementerio;
    private String localidad;
    private String provincia;
}