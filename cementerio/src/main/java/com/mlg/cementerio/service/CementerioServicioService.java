package com.mlg.cementerio.service;

import com.mlg.cementerio.dto.CementerioServicioDTO;
import com.mlg.cementerio.mapper.CementerioServicioMapper;
import com.mlg.cementerio.repository.CementerioServicioRepository;
import com.mlg.cementerio.entity.CementerioServicio; // Importante
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Recomendado para updates
import java.util.List;

/**
 * Servicio para consultar la disponibilidad de servicios por cementerio.
 */
@Service
@RequiredArgsConstructor
public class CementerioServicioService {

    private final CementerioServicioRepository repository;
    private final CementerioServicioMapper mapper;

    /**
     * Recupera y mapea a DTO solo los servicios activos de un cementerio.
     */
    public List<CementerioServicioDTO> obtenerServiciosPorCementerio(Integer idCementerio) {

        return repository.findByCementerioIdAndActivoTrue(idCementerio).stream()
                .map(mapper::toDTO)
                .toList();
    }

    /**
     * Método para "anular" o "quitar" un servicio sin borrarlo físicamente.
     */
    @Transactional
    public void desactivarServicio(Integer idCementerioServicio) {
        CementerioServicio cs = repository.findById(idCementerioServicio)
            .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        
        cs.setActivo(false);
        repository.save(cs);
    }
}