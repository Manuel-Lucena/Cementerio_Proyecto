package com.mlg.cementerio.service;

import com.mlg.cementerio.dto.CementerioServicioDTO;
import com.mlg.cementerio.dto.ServicioDTO;
import com.mlg.cementerio.entity.*;
import com.mlg.cementerio.exceptions.ResourceNotFoundException;
import com.mlg.cementerio.mapper.ServicioMapper;
import com.mlg.cementerio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final CementerioServicioRepository cemeServicioRepository;
    private final CementerioRepository cementerioRepository;
    private final ServicioMapper mapper;

    /**
     * Devuelve el catálogo maestro de todos los servicios disponibles.
     */
    @Transactional(readOnly = true)
    public List<ServicioDTO> listarCatalogoCompleto() {
        return servicioRepository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    /**
     * Lista solo los servicios ACTIVOS para un cementerio.
     */
    @Transactional(readOnly = true)
    public List<CementerioServicioDTO> listarPorCementerio(Integer idCementerio) {
        return cemeServicioRepository.findByCementerioIdAndActivoTrue(idCementerio).stream()
                .map(mapper::toCementerioDTO)
                .toList();
    }

    /**
     * Sincroniza la configuración de servicios.
     */
    @Transactional
    public void guardarConfiguracion(Integer idCementerio, List<CementerioServicioDTO> dtos) {
        Cementerio cementerio = cementerioRepository.findById(idCementerio)
                .orElseThrow(() -> new ResourceNotFoundException("Cementerio no encontrado"));


        List<CementerioServicio> serviciosExistentes = cemeServicioRepository.findByCementerioId(idCementerio);

    
        serviciosExistentes.forEach(s -> s.setActivo(false));

        if (dtos != null && !dtos.isEmpty()) {
            for (CementerioServicioDTO dto : dtos) {
          
                CementerioServicio existente = serviciosExistentes.stream()
                        .filter(s -> s.getServicio().getId().equals(dto.getId_servicio()))
                        .findFirst()
                        .orElse(null);

                if (existente != null) {
                    existente.setCoste(dto.getCoste());
                    existente.setActivo(true);
                } else {
                   
                    Servicio servicioMaestro = servicioRepository.findById(dto.getId_servicio())
                            .orElseThrow(() -> new ResourceNotFoundException("Servicio maestro no encontrado"));

                    CementerioServicio nuevo = mapper.toEntity(dto, cementerio, servicioMaestro);
                    nuevo.setId(null);
                    nuevo.setActivo(true);
                    serviciosExistentes.add(nuevo);
                }
            }
        }

     
        cemeServicioRepository.saveAll(serviciosExistentes);
    }
}