package com.mlg.cementerio.service;

import com.mlg.cementerio.dto.CementerioDTO;
import com.mlg.cementerio.entity.*;
import com.mlg.cementerio.exceptions.ResourceNotFoundException;
import com.mlg.cementerio.mapper.CementerioMapper;
import com.mlg.cementerio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * Servicio para gestionar cementerios y el almacenamiento físico de sus planos.
 */
@Service
@RequiredArgsConstructor
public class CementerioService {

    private final CementerioRepository cementerioRepository;
    private final EmpresaRepository empresaRepository;
    private final LocalidadRepository localidadRepository;
    private final CementerioMapper cementerioMapper;

    private final Path root = Paths.get("..", "PROYECTO_CEMENTERIO", "cementerio_frontend", "public", "planos");

    @Transactional(readOnly = true)
    public List<CementerioDTO> obtenerPorEmpresaDTO(Integer idEmpresa) {
        return cementerioRepository.findByEmpresaId(idEmpresa).stream()
                .map(cementerioMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CementerioDTO> listarTodosDTO() {
        return cementerioRepository.findAll().stream()
                .map(cementerioMapper::toDTO).toList();
    }

    /**
     * Busca un cementerio y lo mapea directamente a DTO.
     */
    @Transactional(readOnly = true)
    public CementerioDTO obtenerPorId(Integer id) {
        return cementerioRepository.findById(id)
                .map(cementerioMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Cementerio no encontrado: " + id));
    }

    /**
     * Lista cementerios filtrando por empresa asociada al usuario.
     */
    @Transactional(readOnly = true)
    public List<CementerioDTO> listarPorUsername(String username) {
        return empresaRepository.findByUsuarioNombreUsuario(username)
                .map(empresa -> empresa.getCementerios().stream()
                        .map(cementerioMapper::toDTO)
                        .toList())
                .orElse(List.of());
    }

    /**
     * Guarda un cementerio vinculando empresa y localidad.
     */
    @Transactional
    public CementerioDTO guardarConImagenDTO(CementerioDTO dto, MultipartFile archivo) throws IOException {
        Empresa empresa = empresaRepository.findById(dto.getId_empresa())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        Localidad localidad = (dto.getId_localidad() != null)
                ? localidadRepository.findById(dto.getId_localidad()).orElse(null)
                : null;

        Cementerio cementerio = cementerioRepository.save(cementerioMapper.toEntity(dto, empresa, localidad));

        if (archivo != null && !archivo.isEmpty()) {
            actualizarImagen(cementerio, archivo);
            cementerio = cementerioRepository.save(cementerio);
        }

        return cementerioMapper.toDTO(cementerio);
    }

    /**
     * Actualiza datos y plano de un cementerio existente.
     */
    @Transactional
    public CementerioDTO actualizarDTO(Integer id, CementerioDTO dto, MultipartFile archivo) throws IOException {
        Cementerio cementerio = cementerioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cementerio no encontrado"));

        Empresa empresa = empresaRepository.findById(dto.getId_empresa())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        cementerio.setNombre(dto.getNombre());
        cementerio.setDireccion(dto.getDireccion());
        cementerio.setEmpresa(empresa);
        cementerio.setLocalidad(dto.getId_localidad() != null
                ? localidadRepository.findById(dto.getId_localidad()).orElse(null)
                : null);

        if (archivo != null && !archivo.isEmpty()) {
            actualizarImagen(cementerio, archivo);
        }

        return cementerioMapper.toDTO(cementerioRepository.save(cementerio));
    }

    /**
     * Elimina el cementerio y su archivo de plano físico.
     */
    @Transactional
    public void eliminar(Integer id) {
        Cementerio cementerio = cementerioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cementerio no encontrado: " + id));

        if (cementerio.getEmpresa() != null) {
            cementerio.getEmpresa().getCementerios().remove(cementerio);
            cementerio.setEmpresa(null);
        }

        cementerioRepository.delete(cementerio);
        cementerioRepository.flush();
    }

    private void actualizarImagen(Cementerio cementerio, MultipartFile archivo) throws IOException {
        if (!Files.exists(root))
            Files.createDirectories(root);
        String nombreImagen = "plano_" + cementerio.getId() + ".jpg";
        Files.copy(archivo.getInputStream(), this.root.resolve(nombreImagen), StandardCopyOption.REPLACE_EXISTING);
        cementerio.setImagenPlano(nombreImagen);
    }
}