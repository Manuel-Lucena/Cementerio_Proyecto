package com.mlg.cementerio.service;

import com.mlg.cementerio.dto.SepulturaDTO;
import com.mlg.cementerio.mapper.SepulturaMapper;
import com.mlg.cementerio.repository.SepulturaRepository;
import com.mlg.cementerio.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SepulturaService {

    private final SepulturaRepository sepulturaRepository;
    private final SepulturaMapper sepulturaMapper;

    /**
     * Obtiene todas las sepulturas pertenecientes a una agrupación específica.
     */
    @Transactional(readOnly = true)
    public List<SepulturaDTO> listarPorAgrupacion(Integer idAgrupacion) {
        List<SepulturaDTO> lista = sepulturaRepository.findByAgrupacionId(idAgrupacion).stream()
                .map(sepulturaMapper::toDTO)
                .toList();
        return lista;
    }

    /**
     * Busca una sepultura por su ID y la devuelve convertida a DTO.
     */
    @Transactional(readOnly = true)
    public SepulturaDTO obtenerPorId(Integer id) {
        SepulturaDTO dto = sepulturaRepository.findById(id)
                .map(sepulturaMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Sepultura no encontrada: " + id));
        return dto;
    }
}