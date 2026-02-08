package com.mlg.cementerio.controller;

import com.mlg.cementerio.dto.CementerioDTO;
import com.mlg.cementerio.service.CementerioService;
import jakarta.validation.Valid; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Controlador para la gestión de cementerios, incluyendo la subida de imágenes de planos.
 */
@RestController
@RequestMapping("/api/cementerios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CementerioController {

    private final CementerioService cementerioService;

    /**
     * Obtiene la lista completa de todos los cementerios registrados.
     * @return Lista de CementerioDTO.
     */
    @GetMapping
    public ResponseEntity<List<CementerioDTO>> listarTodos() {
        return ResponseEntity.ok(cementerioService.listarTodosDTO());
    }

    /**
     * Filtra y obtiene los cementerios pertenecientes a una empresa específica.
     * @param id ID de la empresa.
     * @return Lista de cementerios de dicha empresa.
     */
    @GetMapping("/empresa/{id}")
    public ResponseEntity<List<CementerioDTO>> listarPorEmpresa(@PathVariable Integer id) {
        return ResponseEntity.ok(cementerioService.obtenerPorEmpresaDTO(id));
    }

    /**
     * Recupera los datos detallados de un cementerio por su ID.
     * @param id ID del cementerio.
     * @return Datos del cementerio encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CementerioDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(cementerioService.obtenerPorId(id));
    }

    /**
     * Obtiene los cementerios asociados al usuario (Empresa) autenticado actualmente.
     * @param authentication Datos de la sesión del usuario.
     * @return Lista de cementerios gestionados por el usuario.
     */
    @GetMapping("/mis-cementerios")
    public ResponseEntity<List<CementerioDTO>> listarMisCementerios(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(cementerioService.listarPorUsername(username));
    }

    /**
     * Crea un nuevo cementerio permitiendo la subida opcional de una imagen del plano.
     * @param dto Datos del cementerio (vía ModelAttribute por el soporte de archivos).
     * @param archivo Archivo de imagen del plano.
     * @return El cementerio creado con estado 201.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CementerioDTO> guardar(
            @Valid @ModelAttribute CementerioDTO dto,
            @RequestParam(value = "imagen_archivo", required = false) MultipartFile archivo) throws IOException {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cementerioService.guardarConImagenDTO(dto, archivo));
    }

    /**
     * Actualiza un cementerio existente y permite reemplazar o añadir el archivo del plano.
     * @param id ID del cementerio a editar.
     * @param dto Nuevos datos del cementerio.
     * @param archivo Nuevo archivo de imagen (opcional).
     * @return El cementerio actualizado.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CementerioDTO> actualizar(
            @PathVariable Integer id,
            @Valid @ModelAttribute CementerioDTO dto,
            @RequestParam(value = "imagen_plano", required = false) MultipartFile archivo) throws IOException {

        return ResponseEntity.ok(cementerioService.actualizarDTO(id, dto, archivo));
    }

    /**
     * Elimina un cementerio y sus recursos asociados (como archivos físicos).
     * @param id ID del cementerio a borrar.
     * @return 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) throws IOException {
        cementerioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}