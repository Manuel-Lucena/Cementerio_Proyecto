package com.mlg.cementerio.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data                 
@NoArgsConstructor    
@AllArgsConstructor  
public class RegistroClienteRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, message = "El usuario debe tener al menos 4 caracteres")
    private String nombreUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "Debe confirmar la contraseña")
    private String password_confirm; 

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener exactamente 9 dígitos")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fecha_nacimiento; 
}