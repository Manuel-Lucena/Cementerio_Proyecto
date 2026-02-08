package com.mlg.cementerio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CementerioDTO {
    private Integer id;

    @NotBlank(message = "El nombre del cementerio es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private String imagen_plano; 

    @NotNull(message = "La empresa vinculada es obligatoria")
    private Integer id_empresa;

    @NotNull(message = "La localidad es obligatoria")
    private Integer id_localidad;


    @NotNull(message = "La provincia es obligatoria")
    private Integer id_provincia;
}