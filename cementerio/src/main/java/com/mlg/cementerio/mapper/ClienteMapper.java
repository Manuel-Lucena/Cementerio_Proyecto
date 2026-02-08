package com.mlg.cementerio.mapper;

import com.mlg.cementerio.dto.ClienteDTO;
import com.mlg.cementerio.dto.RegistroClienteRequest;
import com.mlg.cementerio.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO dto = null;

        if (cliente != null) {
            dto = ClienteDTO.builder()
                    .id(cliente.getId())
                    .nombre(cliente.getNombre())
                    .apellidos(cliente.getApellidos())
                    .email(cliente.getEmail())
                    .telefono(cliente.getTelefono())
                    .direccion(cliente.getDireccion())
                    .fechaNacimiento(cliente.getFechaNacimiento())
                    .idUsuario(cliente.getUsuario() != null ? cliente.getUsuario().getId() : null)
                    .nombreUsuario(cliente.getUsuario() != null ? cliente.getUsuario().getNombreUsuario() : null)
                    .build();
        }

        return dto;
    }

    public Cliente toEntity(RegistroClienteRequest request) {
        Cliente cliente = null;

        if (request != null) {
            cliente = Cliente.builder()
                    .nombre(request.getNombre())
                    .apellidos(request.getApellidos())
                    .email(request.getEmail())
                    .telefono(request.getTelefono())
                    .direccion(request.getDireccion())
                    .fechaNacimiento(request.getFecha_nacimiento())
                    .build();
        }

        return cliente;
    }

    public void updateEntityFromDTO(ClienteDTO dto, Cliente cliente) {
        if (dto != null && cliente != null) {
            cliente.setNombre(dto.getNombre());
            cliente.setApellidos(dto.getApellidos());
            cliente.setEmail(dto.getEmail());
            cliente.setTelefono(dto.getTelefono());
            cliente.setDireccion(dto.getDireccion());
            cliente.setFechaNacimiento(dto.getFechaNacimiento());
        }
    }
}