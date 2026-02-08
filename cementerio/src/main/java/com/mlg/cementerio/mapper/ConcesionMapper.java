package com.mlg.cementerio.mapper;

import com.mlg.cementerio.dto.ConcesionDTO;
import com.mlg.cementerio.dto.TramiteConcesionDTO;
import com.mlg.cementerio.entity.Cliente;
import com.mlg.cementerio.entity.Concesion;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class ConcesionMapper {

    public Concesion toEntity(TramiteConcesionDTO dto, Cliente cliente) {
        Concesion concesion = null;

        if (dto != null) {
            concesion = Concesion.builder()
                    .cliente(cliente)
                    .fechaInicio(dto.getFechaInicio())
                    .fechaFin(dto.getFechaFin())
                    .build();
        }

        return concesion;
    }

    public ConcesionDTO toDto(Concesion entity) {
        ConcesionDTO dto = null;

        if (entity != null) {
            String codigoSep = "N/A";
            String nombreCem = "N/A";

            if (entity.getSepultura() != null) {
                codigoSep = entity.getSepultura().getCodigo_visual();
                
                if (entity.getSepultura().getAgrupacion() != null && 
                    entity.getSepultura().getAgrupacion().getCementerio() != null) {
                    nombreCem = entity.getSepultura().getAgrupacion().getCementerio().getNombre();
                }
            }

            dto = ConcesionDTO.builder()
                    .id(entity.getId().longValue())
                    .fechaInicio(entity.getFechaInicio())
                    .fechaFin(entity.getFechaFin())
                    .activa(entity.getFechaFin() != null && entity.getFechaFin().isAfter(LocalDate.now()))
                    .codigoSepultura(codigoSep)
                    .nombreCementerio(nombreCem)
                    .build();
        }

        return dto;
    }
}