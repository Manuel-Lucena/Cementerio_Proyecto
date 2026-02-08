package com.mlg.cementerio.controller;

import com.mlg.cementerio.dto.ContratarServiciosDTO;
import com.mlg.cementerio.dto.FacturaDTO;
import com.mlg.cementerio.service.FacturacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestión de facturas.
 */
@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FacturacionController {

    private final FacturacionService facturacionService;

    @PostMapping("/contratar")
    public ResponseEntity<FacturaDTO> contratar(@RequestBody ContratarServiciosDTO dto) {
        FacturaDTO respuesta = facturacionService.contratarServicios(dto);
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Recupera el historial de facturas paginado.
     * @param page Número de página (empieza en 0).
     * @param size Cantidad de registros por página.
     */
    @GetMapping("/mis-facturas")
    public ResponseEntity<Page<FacturaDTO>> getMisFacturas(
            Authentication auth,
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "fechaFactura") String campo,
            @RequestParam(defaultValue = "desc") String dir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        
    
        Page<FacturaDTO> pagina = facturacionService.listarFacturasUsuario(
                auth.getName(), estado, campo, dir, page, size);
        
        return ResponseEntity.ok(pagina);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        facturacionService.eliminarFactura(id);
        return ResponseEntity.noContent().build();
    }
}