package com.mlg.cementerio.controller;

import com.mlg.cementerio.dto.AgrupacionDTO;
import com.mlg.cementerio.service.AgrupacionService;
import jakarta.validation.Valid; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para gestionar las agrupaciones de sepulturas (sectores, bloques, etc.).
 */
@RestController
@RequestMapping("/api/agrupaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AgrupacionController {

    private final AgrupacionService agrupacionService;

    /**
     * Crea una nueva agrupación en el sistema.
     * @param dto Datos de la agrupación a crear.
     * @return AgrupacionDTO creada con estado 201 Created.
     */
    @PostMapping
    public ResponseEntity<AgrupacionDTO> crear(@Valid @RequestBody AgrupacionDTO dto) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(agrupacionService.crear(dto));
    }

    /**
     * Obtiene la lista de agrupaciones pertenecientes a un cementerio específico.
     * @param id ID del cementerio.
     * @return Lista de agrupaciones encontradas (puede estar vacía).
     */
    @GetMapping("/cementerio/{id}")
    public ResponseEntity<List<AgrupacionDTO>> listarPorCementerio(@PathVariable Integer id) {
        return ResponseEntity.ok(agrupacionService.listarPorCementerio(id));
    }

    /**
     * Actualiza los datos de una agrupación existente.
     * @param id ID de la agrupación a modificar.
     * @param dto Nuevos datos de la agrupación.
     * @return AgrupacionDTO actualizada o error si no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgrupacionDTO> actualizar(
            @PathVariable Integer id, 
            @Valid @RequestBody AgrupacionDTO dto) { 
        return ResponseEntity.ok(agrupacionService.actualizar(id, dto));
    }

    /**
     * Elimina una agrupación del sistema.
     * @param id ID de la agrupación a borrar.
     * @return 204 No Content si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        agrupacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}