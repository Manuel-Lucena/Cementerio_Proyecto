package com.mlg.cementerio.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mlg.cementerio.repository.ConcesionRepository;
import com.mlg.cementerio.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadisticasService {

    private final FacturaRepository facturaRepository;
    private final ConcesionRepository concesionRepository;

    /**
     * Genera un resumen estadístico para el administrador incluyendo ingresos totales y estado de las concesiones.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerResumenAdmin() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("ingresosTotales", facturaRepository.sumarIngresosTotales());
        stats.put("concesionesVencidas", concesionRepository.contarVencidas());
        stats.put("concesionesActivas", concesionRepository.contarActivas());
        
        return stats;
    }
}