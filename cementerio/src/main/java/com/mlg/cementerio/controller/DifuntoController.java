package com.mlg.cementerio.controller;

import com.mlg.cementerio.dto.DifuntoDTO;
import com.mlg.cementerio.service.DifuntoService;
import com.mlg.cementerio.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestión y consulta de difuntos registrados.
 */
@RestController
@RequestMapping("/api/difuntos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DifuntoController {

    private final DifuntoService difuntoService;

    /**
     * Lista los difuntos según el rol: el ADMIN ve todos, el CLIENTE solo los suyos.
     * @param usuario Entidad del usuario autenticado obtenida del contexto de seguridad.
     * @return Lista de difuntos filtrada por permisos.
     */
    @GetMapping("/mis-difuntos")
    public ResponseEntity<List<DifuntoDTO>> getMisDifuntos(@AuthenticationPrincipal Usuario usuario) {
        List<DifuntoDTO> resultado;

        if (usuario.getRol().getNombre().equalsIgnoreCase("ADMIN")) {
            resultado = difuntoService.listarTodos();
        } else {
            resultado = difuntoService.listarPorUsuario(usuario.getId());
        }

        return ResponseEntity.ok(resultado);
    }
}