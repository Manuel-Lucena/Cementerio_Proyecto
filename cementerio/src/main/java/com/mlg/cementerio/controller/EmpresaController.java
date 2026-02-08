package com.mlg.cementerio.controller;

import com.mlg.cementerio.entity.Empresa;
import com.mlg.cementerio.mapper.EmpresaMapper;
import com.mlg.cementerio.service.EmpresaService;
import com.mlg.cementerio.dto.EmpresaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * Controlador para la gestión de empresas propietarias de cementerios.
 */
@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmpresaController {

    private final EmpresaService empresaService;
    private final EmpresaMapper empresaMapper;

    /**
     * Obtiene todas las empresas registradas en el sistema.
     * @return Lista de EmpresaDTO.
     */
    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> obtenerEmpresas() {
        return ResponseEntity.ok(empresaService.listarTodas());
    }

    /**
     * Registra una nueva empresa y crea automáticamente su usuario de acceso.
     * @param dto Datos de la empresa (vía ModelAttribute por el soporte de archivos).
     * @param archivo Logo o imagen de la empresa (opcional).
     * @return La empresa creada con estado 201.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmpresaDTO> crearEmpresa(
            @Valid @ModelAttribute EmpresaDTO dto, 
            @RequestParam(value = "imagenArchivo", required = false) MultipartFile archivo) {
        
        Empresa entity = empresaService.crearEmpresaConUsuario(dto, archivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaMapper.toDto(entity));
    }

    /**
     * Actualiza los datos de una empresa y permite cambiar su imagen.
     * @param id ID de la empresa a modificar.
     * @param dto Nuevos datos de la empresa.
     * @param archivo Nuevo archivo de imagen (opcional).
     * @return Empresa actualizada.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmpresaDTO> actualizar(
            @PathVariable Integer id,
            @Valid @ModelAttribute EmpresaDTO dto,
            @RequestParam(value = "imagenArchivo", required = false) MultipartFile archivo) {

        Empresa entity = empresaService.actualizarEmpresa(id, dto, archivo);
        return ResponseEntity.ok(empresaMapper.toDto(entity));
    }

    /**
     * Elimina una empresa del sistema.
     * @param id ID de la empresa.
     * @return 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        empresaService.eliminarEmpresa(id);
        return ResponseEntity.noContent().build();
    }
}