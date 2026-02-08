package com.mlg.cementerio.controller;

import com.mlg.cementerio.dto.CementerioServicioDTO;
import com.mlg.cementerio.dto.ServicioDTO;
import com.mlg.cementerio.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestión del catálogo de servicios y precios por cementerio.
 */
@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
public class ServicioController {

    private final ServicioService servicioService;

    /**
     * Obtiene la lista base de todos los servicios definidos en el sistema.
     * @return Lista de servicios generales (mantenimiento, entierro, etc.).
     */
    @GetMapping("/catalogo")
    public ResponseEntity<List<ServicioDTO>> getCatalogo() {
        List<ServicioDTO> catalogo = servicioService.listarCatalogoCompleto();
        return ResponseEntity.ok(catalogo);
    }

    /**
     * Obtiene los servicios configurados para un cementerio con sus costes específicos.
     * @param id ID del cementerio.
     * @return Lista de servicios vinculados al cementerio.
     */
    @GetMapping("/cementerio/{id}")
    public ResponseEntity<List<CementerioServicioDTO>> getByCementerio(@PathVariable Integer id) {
        List<CementerioServicioDTO> servicios = servicioService.listarPorCementerio(id);
        return ResponseEntity.ok(servicios);
    }

    /**
     * Guarda o actualiza los precios de los servicios para un cementerio determinado.
     * @param id ID del cementerio.
     * @param servicios Lista de servicios y sus nuevos precios.
     * @return 200 OK si la configuración se guardó correctamente.
     */
    @PostMapping("/cementerio/{id}")
    public ResponseEntity<Void> guardarConfiguracion(
            @PathVariable Integer id,
            @RequestBody List<CementerioServicioDTO> servicios) {

        servicioService.guardarConfiguracion(id, servicios);
        return ResponseEntity.ok().build();
    }
}