package com.mlg.cementerio.service;

import com.mlg.cementerio.dto.EmpresaDTO;
import com.mlg.cementerio.entity.Empresa;
import com.mlg.cementerio.entity.Usuario;
import com.mlg.cementerio.exceptions.ResourceNotFoundException;
import com.mlg.cementerio.entity.Rol;
import com.mlg.cementerio.mapper.EmpresaMapper;
import com.mlg.cementerio.repository.EmpresaRepository;
import com.mlg.cementerio.repository.UsuarioRepository;
import com.mlg.cementerio.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EmpresaMapper empresaMapper;
    private final PasswordEncoder passwordEncoder;

    private final Path root = Paths.get("..", "PROYECTO_CEMENTERIO", "cementerio_frontend", "public", "logos");

    /**
     * Obtiene el listado de todas las empresas registradas.
     */
    @Transactional(readOnly = true)
    public List<EmpresaDTO> listarTodas() {
        List<EmpresaDTO> lista = empresaRepository.findAll().stream()
                .map(empresaMapper::toDto)
                .toList();
        return lista;
    }

    /**
     * Crea una nueva empresa y genera automáticamente su usuario de acceso con el rol correspondiente.
     */
    @Transactional
    public Empresa crearEmpresaConUsuario(EmpresaDTO dto, MultipartFile archivo) {
        Rol rolEmpresa = rolRepository.findById(2L)
                .orElseThrow(() -> new ResourceNotFoundException("Rol EMPRESA no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(dto.getNombre_usuario());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(rolEmpresa);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Empresa empresa = empresaMapper.toEntity(dto);
        empresa.setUsuario(usuarioGuardado);
        Empresa empresaGuardada = empresaRepository.save(empresa);

        if (archivo != null && !archivo.isEmpty()) {
            empresaGuardada.setImagen(actualizarImagenDisco(empresaGuardada.getId(), archivo));
            empresaGuardada = empresaRepository.save(empresaGuardada);
        }

        return empresaGuardada;
    }

    /**
     * Actualiza los datos de una empresa existente y gestiona el cambio de credenciales de su usuario asociado.
     */
    @Transactional
    public Empresa actualizarEmpresa(Integer id, EmpresaDTO dto, MultipartFile archivo) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        empresa.setNombre(dto.getNombre());
        empresa.setDireccion(dto.getDireccion());
        empresa.setTelefono(dto.getTelefono());
        empresa.setEmail(dto.getEmail());
        empresa.setAñosReutilizarNichos(dto.getAños_reutilizar_nichos());

        if (archivo != null && !archivo.isEmpty()) {
            empresa.setImagen(actualizarImagenDisco(id, archivo));
        }

        Usuario usuario = empresa.getUsuario();
        if (usuario != null) {
            if (dto.getNombre_usuario() != null) usuario.setNombreUsuario(dto.getNombre_usuario());
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            usuarioRepository.save(usuario);
        }

        Empresa resultado = empresaRepository.save(empresa);
        return resultado;
    }

    /**
     * Elimina una empresa, su usuario de acceso y el archivo de imagen del logo en disco.
     */
    @Transactional
    public void eliminarEmpresa(Integer id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        Long usuarioId = empresa.getUsuario().getId();

        try {
            Files.deleteIfExists(this.root.resolve("empresa_" + id + ".jpg"));
        } catch (IOException ignored) {}

        empresaRepository.delete(empresa);
        usuarioRepository.deleteById(usuarioId);
    }

    private String actualizarImagenDisco(Integer id, MultipartFile archivo) {
        String nombreArchivo = "empresa_" + id + ".jpg";
        try {
            if (!Files.exists(root)) Files.createDirectories(root);
            Files.copy(archivo.getInputStream(), this.root.resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar logo de empresa", e);
        }
        return nombreArchivo;
    }
}