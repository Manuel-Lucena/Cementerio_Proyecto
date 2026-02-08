package com.mlg.cementerio.mapper;

import com.mlg.cementerio.dto.SepulturaDTO;
import com.mlg.cementerio.entity.Sepultura;
import org.springframework.stereotype.Component;

@Component
public class SepulturaMapper {

    public SepulturaDTO toDTO(Sepultura entity) {
        SepulturaDTO dto = null;

        if (entity != null) {
            dto = SepulturaDTO.builder()
                    .id(entity.getId())
                    .fila(entity.getFila())
                    .columna(entity.getColumna())
                    .codigoVisual(entity.getCodigo_visual())
                    .ocupado(entity.getOcupado())
                    .idAgrupacion(entity.getAgrupacion() != null ? entity.getAgrupacion().getId() : null)
                    .build();
        }

        return dto;
    }

    public Sepultura toEntity(SepulturaDTO dto) {
        Sepultura entity = null;

        if (dto != null) {
            entity = Sepultura.builder()
                    .id(dto.getId())
                    .fila(dto.getFila())
                    .columna(dto.getColumna())
                    .codigo_visual(dto.getCodigoVisual())
                    .ocupado(dto.getOcupado())
                    .build();
        }

        return entity;
    }
}