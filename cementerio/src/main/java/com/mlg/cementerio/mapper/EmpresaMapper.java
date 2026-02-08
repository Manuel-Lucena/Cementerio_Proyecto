package com.mlg.cementerio.mapper;

import com.mlg.cementerio.dto.EmpresaDTO;
import com.mlg.cementerio.entity.Empresa;
import org.springframework.stereotype.Component;

@Component
public class EmpresaMapper {

    public Empresa toEntity(EmpresaDTO dto) {
        Empresa empresa = null;

        if (dto != null) {
            empresa = Empresa.builder()
                    .nombre(dto.getNombre())
                    .direccion(dto.getDireccion())
                    .telefono(dto.getTelefono())
                    .email(dto.getEmail())
                    .añosReutilizarNichos(dto.getAños_reutilizar_nichos())
                    .build();
        }

        return empresa;
    }

    public EmpresaDTO toDto(Empresa entity) {
        EmpresaDTO dto = null;

        if (entity != null) {
            dto = EmpresaDTO.builder()
                    .id(entity.getId())
                    .nombre(entity.getNombre())
                    .direccion(entity.getDireccion())
                    .telefono(entity.getTelefono())
                    .email(entity.getEmail())
                    .años_reutilizar_nichos(entity.getAñosReutilizarNichos())
                    .imagen(entity.getImagen())
                    .nombre_usuario(entity.getUsuario() != null ? entity.getUsuario().getNombreUsuario() : null)
                    .build();
        }

        return dto;
    }
}