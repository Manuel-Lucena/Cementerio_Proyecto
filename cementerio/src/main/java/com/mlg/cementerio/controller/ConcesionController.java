package com.mlg.cementerio.controller;

import com.mlg.cementerio.dto.ConcesionDTO;
import com.mlg.cementerio.dto.TramiteConcesionDTO;
import com.mlg.cementerio.service.ConcesionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/concesiones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConcesionController {

    private final ConcesionService concesionService;

    @PostMapping
    public ResponseEntity<Void> registrar(@Valid @RequestBody TramiteConcesionDTO dto, Authentication auth) {
        concesionService.registrarTramiteCompleto(dto, auth.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * NUEVO: Obtiene todas las concesiones de un cliente específico para el Admin.
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ConcesionDTO>> listarPorCliente(@PathVariable Integer clienteId) {
        // Asegúrate de tener este método en tu Service que devuelva List<ConcesionDTO>
        return ResponseEntity.ok(concesionService.obtenerPorClienteId(clienteId));
    }
}