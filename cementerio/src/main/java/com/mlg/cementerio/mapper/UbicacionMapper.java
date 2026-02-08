package com.mlg.cementerio.mapper;

import org.springframework.stereotype.Component;

import com.mlg.cementerio.dto.LocalidadDTO;
import com.mlg.cementerio.dto.ProvinciaDTO;
import com.mlg.cementerio.entity.Localidad;
import com.mlg.cementerio.entity.Provincia;

@Component
public class UbicacionMapper {

    public ProvinciaDTO toProvinciaDTO(Provincia entity) {
        ProvinciaDTO dto = null;
        if (entity != null) {
            dto = ProvinciaDTO.builder()
                    .id(entity.getId())
                    .nombre(entity.getNombre())
                    .build();
        }
        return dto;
    }

    public LocalidadDTO toLocalidadDTO(Localidad entity) {
        LocalidadDTO dto = null;
        if (entity != null) {
            dto = LocalidadDTO.builder()
                    .id(entity.getId())
                    .nombre(entity.getNombre())
                    .idProvincia(entity.getProvincia() != null ? entity.getProvincia().getId() : null)
                    .build();
        }
        return dto;
    }
}