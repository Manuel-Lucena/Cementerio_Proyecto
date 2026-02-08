package com.mlg.cementerio.service;

import com.mlg.cementerio.dto.AgrupacionDTO;
import com.mlg.cementerio.mapper.AgrupacionMapper;
import com.mlg.cementerio.entity.Agrupacion;
import com.mlg.cementerio.entity.Cementerio;
import com.mlg.cementerio.entity.Sepultura;
import com.mlg.cementerio.exceptions.BadRequestException;
import com.mlg.cementerio.exceptions.ResourceNotFoundException;
import com.mlg.cementerio.repository.AgrupacionRepository;
import com.mlg.cementerio.repository.CementerioRepository;
import com.mlg.cementerio.repository.SepulturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar agrupaciones de sepulturas y su generación automática.
 */
@Service
@RequiredArgsConstructor
public class AgrupacionService {

    private final AgrupacionRepository agrupacionRepository;
    private final SepulturaRepository sepulturaRepository;
    private final CementerioRepository cementerioRepository;
    private final AgrupacionMapper agrupacionMapper;

    /**
     * Crea una agrupación buscando el cementerio.
     */
    @Transactional
    public AgrupacionDTO crear(AgrupacionDTO dto) {
        Cementerio cementerio = cementerioRepository.findById(dto.getId_cementerio())
                .orElseThrow(() -> new ResourceNotFoundException("Cementerio no encontrado"));

        if (dto.getFilas() <= 0 || dto.getColumnas() <= 0) {
            throw new BadRequestException("Dimensiones inválidas: deben ser mayores a 0");
        }

        Agrupacion agrupacion = agrupacionMapper.toEntity(dto);
        agrupacion.setCementerio(cementerio);
        Agrupacion guardada = agrupacionRepository.save(agrupacion);

        generarSepulturas(guardada);
        return agrupacionMapper.toDTO(guardada);
    }

    /**
     * Actualiza la agrupación validando su existencia con Optional.
     */
    @Transactional
    public AgrupacionDTO actualizar(Integer id, AgrupacionDTO dto) {
        Agrupacion existente = agrupacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agrupación no encontrada"));

        boolean dimensionesCambiadas = !existente.getFilas().equals(dto.getFilas()) || 
                                     !existente.getColumnas().equals(dto.getColumnas());

        if (dimensionesCambiadas && sepulturaRepository.existsByAgrupacionIdAndOcupadoTrue(id)) {
            throw new BadRequestException("No se pueden cambiar dimensiones con sepulturas ocupadas.");
        }

        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setTipo_forma(dto.getTipo_forma());
        existente.setCoordenadas_mapa(dto.getCoordenadas_mapa());

        if (dimensionesCambiadas) {
            sepulturaRepository.deleteByAgrupacionId(id);
            sepulturaRepository.flush(); 
            existente.setFilas(dto.getFilas());
            existente.setColumnas(dto.getColumnas());
            generarSepulturas(existente);
        }

        return agrupacionMapper.toDTO(agrupacionRepository.save(existente));
    }

    /**
     * Elimina una agrupación tras validar existencia y ocupación.
     */
    @Transactional
    public void eliminar(Integer id) {
        Agrupacion agrupacion = agrupacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agrupación no encontrada"));

        if (sepulturaRepository.existsByAgrupacionIdAndOcupadoTrue(id)) {
            throw new BadRequestException("No se puede eliminar: hay sepulturas ocupadas.");
        }

        sepulturaRepository.deleteByAgrupacionId(id);
        agrupacionRepository.delete(agrupacion);
    }

    @Transactional(readOnly = true)
    public List<AgrupacionDTO> listarPorCementerio(Integer idCementerio) {
        return agrupacionRepository.findByCementerioId(idCementerio).stream()
                .map(agrupacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void generarSepulturas(Agrupacion agrupacion) {
        List<Sepultura> sepulturas = new ArrayList<>();
        for (int f = 1; f <= agrupacion.getFilas(); f++) {
            for (int c = 1; c <= agrupacion.getColumnas(); c++) {
                sepulturas.add(Sepultura.builder()
                        .fila(f)
                        .columna(c)
                        .agrupacion(agrupacion)
                        .ocupado(false)
                        .codigo_visual(String.format("%s-F%d-C%d", agrupacion.getNombre(), f, c))
                        .build());
            }
        }
        sepulturaRepository.saveAll(sepulturas);
    }
}