package com.mlg.cementerio.controller;

import com.mlg.cementerio.service.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador para la obtención de métricas y datos generales del sistema.
 */
@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") 
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    /**
     * Obtiene un mapa de datos.
     * @return Mapa con claves descriptivas y valores numéricos/objetos.
     */
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> getResumen() {
        Map<String, Object> resumen = estadisticasService.obtenerResumenAdmin();
        return ResponseEntity.ok(resumen);
    }
}