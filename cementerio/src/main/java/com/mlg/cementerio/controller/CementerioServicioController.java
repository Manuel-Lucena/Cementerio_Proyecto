package com.mlg.cementerio.controller;

import com.mlg.cementerio.dto.CementerioServicioDTO;
import com.mlg.cementerio.service.CementerioServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador para gestionar la relación entre cementerios y los servicios que ofrecen,
 * incluyendo sus costes específicos.
 */
@RestController
@RequestMapping("/api/cementerio-servicios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CementerioServicioController {

    private final CementerioServicioService service;

    /**
     * Obtiene el catálogo de servicios disponibles para un cementerio concreto.
     * @param id ID del cementerio.
     * @return Lista de servicios con sus respectivos precios.
     */
    @GetMapping("/cementerio/{id}")
    public ResponseEntity<List<CementerioServicioDTO>> listarPorCementerio(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerServiciosPorCementerio(id));
    }
}