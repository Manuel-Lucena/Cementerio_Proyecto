package com.mlg.cementerio.mapper;

import com.mlg.cementerio.dto.DifuntoDTO;
import com.mlg.cementerio.entity.Concesion;
import com.mlg.cementerio.entity.Difunto;
import org.springframework.stereotype.Component;

@Component
public class DifuntoMapper {

    public Difunto toEntity(DifuntoDTO dto, Concesion concesion) {
        Difunto entity = null;

        if (dto != null) {
            entity = Difunto.builder()
                    .nombre(dto.getNombre())
                    .apellidos(dto.getApellidos())
                    .fechaFallecimiento(dto.getFechaFallecimiento())
                    .fechaIngreso(dto.getFechaIngreso())
                    .concesion(concesion)
                    .build();
        }

        return entity; 
    }

    public DifuntoDTO toDTO(Difunto entity) {
        DifuntoDTO dto = null;

        if (entity != null) {
            String pos = "N/A";
            Integer cemId = null;
            String nombreCem = "N/A";
            String loc = "N/A";
            String prov = "N/A";

         
            if (entity.getConcesion() != null && entity.getConcesion().getSepultura() != null) {
                var sep = entity.getConcesion().getSepultura();
                pos = String.format("Fila: %d Col: %d", sep.getFila(), sep.getColumna());

                if (sep.getAgrupacion() != null && sep.getAgrupacion().getCementerio() != null) {
                    var cem = sep.getAgrupacion().getCementerio();
                    cemId = cem.getId();
                    nombreCem = cem.getNombre();

                    if (cem.getLocalidad() != null) {
                        loc = cem.getLocalidad().getNombre();
                        if (cem.getLocalidad().getProvincia() != null) {
                            prov = cem.getLocalidad().getProvincia().getNombre();
                        }
                    }
                }
            }

            dto = DifuntoDTO.builder()
                    .id(entity.getId())
                    .nombre(entity.getNombre())
                    .apellidos(entity.getApellidos())
                    .fechaFallecimiento(entity.getFechaFallecimiento())
                    .fechaIngreso(entity.getFechaIngreso())
                    .ubicacion(pos)
                    .idCementerio(cemId)
                    .nombreCementerio(nombreCem)
                    .localidad(loc)
                    .provincia(prov)
                    .build();
        }

        return dto; 
    }
}