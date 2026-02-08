package com.mlg.cementerio.controller;

import com.mlg.cementerio.dto.SepulturaDTO;
import com.mlg.cementerio.service.SepulturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestión y consulta de sepulturas individuales.
 */
@RestController
@RequestMapping("/api/sepulturas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SepulturaController {

    private final SepulturaService sepulturaService;

    /**
     * Obtiene todas las sepulturas que forman parte de una agrupación (bloque/sector).
     * @param id ID de la agrupación.
     * @return Lista de sepulturas con su estado (libre/ocupado) y coordenadas.
     */
    @GetMapping("/agrupacion/{id}")
    public ResponseEntity<List<SepulturaDTO>> getByAgrupacion(@PathVariable Integer id) {
        List<SepulturaDTO> sepulturas = sepulturaService.listarPorAgrupacion(id);
        return ResponseEntity.ok(sepulturas);
    }

    /**
     * Obtiene el detalle de una sepultura específica por su ID.
     * @param id ID de la sepultura.
     * @return Datos detallados de la sepultura.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SepulturaDTO> getById(@PathVariable Integer id) {
        SepulturaDTO dto = sepulturaService.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }
}