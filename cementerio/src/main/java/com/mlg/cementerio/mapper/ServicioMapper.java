package com.mlg.cementerio.mapper;

import com.mlg.cementerio.dto.CementerioServicioDTO;
import com.mlg.cementerio.dto.ServicioDTO;
import com.mlg.cementerio.entity.Cementerio;
import com.mlg.cementerio.entity.CementerioServicio;
import com.mlg.cementerio.entity.Servicio;
import org.springframework.stereotype.Component;

@Component
public class ServicioMapper {

    public ServicioDTO toDTO(Servicio entity) {
        ServicioDTO dto = null;

        if (entity != null) {
            dto = ServicioDTO.builder()
                    .id(entity.getId())
                    .nombre(entity.getNombre())
                    .descripcion(entity.getDescripcion())
                    .build();
        }

        return dto;
    }

    public CementerioServicioDTO toCementerioDTO(CementerioServicio entity) {
        CementerioServicioDTO dto = null;

        if (entity != null) {
            dto = CementerioServicioDTO.builder()
                    .id(entity.getId())
                    .id_cementerio(entity.getCementerio() != null ? entity.getCementerio().getId() : null)
                    .id_servicio(entity.getServicio() != null ? entity.getServicio().getId() : null)
                    .nombreServicio(entity.getServicio() != null ? entity.getServicio().getNombre() : "N/A")
                    .coste(entity.getCoste())
                    .build();
        }

        return dto;
    }

    public CementerioServicio toEntity(CementerioServicioDTO dto, Cementerio cementerio, Servicio servicio) {
        CementerioServicio entity = null;

        if (dto != null) {
            entity = CementerioServicio.builder()
                    .id(dto.getId())
                    .cementerio(cementerio)
                    .servicio(servicio)
                    .coste(dto.getCoste())
                    .build();
        }

        return entity;
    }
}