package com.mlg.cementerio.mapper;

import com.mlg.cementerio.dto.CementerioDTO;
import com.mlg.cementerio.entity.Cementerio;
import com.mlg.cementerio.entity.Empresa;
import com.mlg.cementerio.entity.Localidad;
import org.springframework.stereotype.Component;

@Component
public class CementerioMapper {

    public Cementerio toEntity(CementerioDTO dto, Empresa empresa, Localidad localidad) {
        Cementerio entity = null; 

        if (dto != null) {
            entity = Cementerio.builder()
                    .id(dto.getId())
                    .nombre(dto.getNombre())
                    .direccion(dto.getDireccion())
                    .imagenPlano(dto.getImagen_plano())
                    .empresa(empresa)
                    .localidad(localidad)
                    .build();
        }

        return entity; 
    }

    public CementerioDTO toDTO(Cementerio entity) {
        CementerioDTO dto = null; 

        if (entity != null) {
           
            Integer idProvincia = (entity.getLocalidad() != null && entity.getLocalidad().getProvincia() != null)
                    ? entity.getLocalidad().getProvincia().getId()
                    : null;

        
            dto = CementerioDTO.builder()
                    .id(entity.getId())
                    .nombre(entity.getNombre())
                    .direccion(entity.getDireccion())
                    .imagen_plano(entity.getImagenPlano())
                    .id_empresa(entity.getEmpresa() != null ? entity.getEmpresa().getId() : null)
                    .id_localidad(entity.getLocalidad() != null ? entity.getLocalidad().getId() : null)
                    .id_provincia(idProvincia)
                    .build();
        }

        return dto; 
    }
}