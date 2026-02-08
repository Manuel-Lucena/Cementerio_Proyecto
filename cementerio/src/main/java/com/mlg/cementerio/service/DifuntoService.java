package com.mlg.cementerio.service;

import com.mlg.cementerio.dto.DifuntoDTO;
import com.mlg.cementerio.mapper.DifuntoMapper;
import com.mlg.cementerio.repository.DifuntoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DifuntoService {

    private final DifuntoRepository difuntoRepository;
    private final DifuntoMapper difuntoMapper;

    /**
     * Recupera la lista completa de difuntos registrados en el sistema.
     */
    @Transactional(readOnly = true)
    public List<DifuntoDTO> listarTodos() {
        return difuntoRepository.findAll().stream()
                .map(difuntoMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los difuntos asociados a un usuario específico que se encuentran en estado ACTIVO.
     */
    @Transactional(readOnly = true)
    public List<DifuntoDTO> listarPorUsuario(Long usuarioId) {
        return difuntoRepository.findMisDifuntosActivos(usuarioId).stream()
                .map(difuntoMapper::toDTO)
                .collect(Collectors.toList());
    }
}