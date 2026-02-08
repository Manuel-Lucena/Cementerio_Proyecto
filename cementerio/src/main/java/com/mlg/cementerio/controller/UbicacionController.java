package com.mlg.cementerio.controller;

import com.mlg.cementerio.dto.LocalidadDTO;
import com.mlg.cementerio.dto.ProvinciaDTO;
import com.mlg.cementerio.service.UbicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la consulta de datos geográficos (Provincias y Localidades).
 */
@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    /**
     * Lista todas las provincias disponibles.
     * 
     * @return Lista de provincias.
     */
    @GetMapping("/provincias")
    public ResponseEntity<List<ProvinciaDTO>> getProvincias() {
        List<ProvinciaDTO> lista = ubicacionService.listarProvincias();
        return ResponseEntity.ok(lista);
    }

    // En UbicacionController.java

    @GetMapping("/localidades/{id}")
    public ResponseEntity<LocalidadDTO> getLocalidadPorId(@PathVariable Integer id) {
        // Este método es vital para que al editar sepamos que la localidad X
        // pertenece a la provincia Y
        LocalidadDTO dto = ubicacionService.obtenerLocalidadPorId(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Lista las localidades pertenecientes a una provincia concreta.
     * 
     * @param id ID de la provincia.
     * @return Lista de localidades de esa provincia.
     */
    @GetMapping("/provincias/{id}/localidades")
    public ResponseEntity<List<LocalidadDTO>> getLocalidades(@PathVariable Integer id) {
        List<LocalidadDTO> lista = ubicacionService.listarLocalidadesPorProvincia(id);
        return ResponseEntity.ok(lista);
    }
}