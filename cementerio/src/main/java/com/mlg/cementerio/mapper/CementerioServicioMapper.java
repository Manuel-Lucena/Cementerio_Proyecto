package com.mlg.cementerio.mapper;

import com.mlg.cementerio.dto.CementerioServicioDTO;
import com.mlg.cementerio.entity.CementerioServicio;
import org.springframework.stereotype.Component;

@Component
public class CementerioServicioMapper {

    public CementerioServicioDTO toDTO(CementerioServicio entity) {
        CementerioServicioDTO dto = null;

        if (entity != null) {
            dto = CementerioServicioDTO.builder()
                    .id(entity.getId())
                    .id_cementerio(entity.getCementerio().getId())
                    .id_servicio(entity.getServicio().getId())
                    .nombreServicio(entity.getServicio().getNombre())
                    .descripcion(entity.getServicio().getDescripcion())
                    .coste(entity.getCoste())
                    .build();
        }

        return dto;
    }
}