package com.mlg.cementerio.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {

    private Integer id; 
    
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, message = "El usuario debe tener al menos 4 caracteres")
    private String nombre_usuario;

    private String password;

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección postal es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe contener exactamente 9 números")
    private String telefono;

    @NotBlank(message = "El email corporativo es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotNull(message = "Los años de reutilización son obligatorios")
    @Min(value = 5, message = "Por normativa, el mínimo son 5 años")
    private Integer años_reutilizar_nichos;

    private String imagen;
}