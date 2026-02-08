package com.mlg.cementerio.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mlg.cementerio.dto.RegistroClienteRequest;
import com.mlg.cementerio.dto.ClienteDTO;
import com.mlg.cementerio.entity.*;
import com.mlg.cementerio.repository.*;
import com.mlg.cementerio.mapper.ClienteMapper;
import com.mlg.cementerio.exceptions.BadRequestException;
import com.mlg.cementerio.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteMapper clienteMapper;

    /**
     * Registra un nuevo cliente creando simultáneamente su usuario de acceso y
     * asignando el rol correspondiente.
     */
    @Transactional
    public Cliente registrarClienteCompleto(RegistroClienteRequest request) {
        if (!request.getPassword().equals(request.getPassword_confirm())) {
            throw new BadRequestException("Las contraseñas no coinciden");
        }

        if (usuarioRepository.findByNombreUsuario(request.getNombreUsuario()).isPresent()) {
            throw new BadRequestException("El nombre de usuario ya está en uso");
        }

        Rol rolCliente = rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("Rol CLIENTE no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getNombreUsuario());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rolCliente);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Cliente cliente = clienteMapper.toEntity(request);
        cliente.setUsuario(usuarioGuardado);

        Cliente resultado = clienteRepository.save(cliente);
        return resultado;
    }

    /**
     * Obtiene la lista de todos los clientes registrados convertidos a DTO.
     */
    public List<ClienteDTO> listarTodos() {
        List<ClienteDTO> lista = clienteRepository.findAll().stream()
                .map(clienteMapper::toDTO)
                .toList();
        return lista;
    }

    /**
     * Actualiza los datos personales de un cliente y permite el cambio de
     * contraseña si se proporciona una nueva.
     */
    @Transactional
    public ClienteDTO actualizarCliente(Integer id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
                throw new BadRequestException("Las contraseñas de actualización no coinciden");
            }
            Usuario usuario = cliente.getUsuario();
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            usuarioRepository.save(usuario);
        }

        clienteMapper.updateEntityFromDTO(dto, cliente);

        ClienteDTO resultado = clienteMapper.toDTO(clienteRepository.save(cliente));
        return resultado;
    }

    /**
     * Elimina el perfil del cliente y su cuenta de usuario asociada del sistema.
     */
    @Transactional
    public void eliminarCliente(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        Usuario usuario = cliente.getUsuario();

        clienteRepository.delete(cliente);

        if (usuario != null) {
            usuarioRepository.delete(usuario);
        }
    }
}