package com.mlg.cementerio.mapper;

import org.springframework.stereotype.Component;
import com.mlg.cementerio.dto.AgrupacionDTO;
import com.mlg.cementerio.entity.Agrupacion;

@Component
public class AgrupacionMapper {

    public Agrupacion toEntity(AgrupacionDTO dto) {
        Agrupacion entity = null;

        if (dto != null) {
            entity = Agrupacion.builder()
                    .id(dto.getId())
                    .nombre(dto.getNombre())
                    .filas(dto.getFilas())
                    .columnas(dto.getColumnas())
                    .descripcion(dto.getDescripcion())
                    .coordenadas_mapa(dto.getCoordenadas_mapa())
                    .tipo_forma(dto.getTipo_forma())
                    .build();
        }

        return entity;
    }

    public AgrupacionDTO toDTO(Agrupacion entity) {
        AgrupacionDTO dto = null;

        if (entity != null) {
            dto = AgrupacionDTO.builder()
                    .id(entity.getId())
                    .nombre(entity.getNombre())
                    .filas(entity.getFilas())
                    .columnas(entity.getColumnas())
                    .descripcion(entity.getDescripcion())
                    .coordenadas_mapa(entity.getCoordenadas_mapa())
                    .tipo_forma(entity.getTipo_forma())
                    .id_cementerio(entity.getCementerio() != null ? entity.getCementerio().getId() : null)
                    .build();
        }

        return dto;
    }
}