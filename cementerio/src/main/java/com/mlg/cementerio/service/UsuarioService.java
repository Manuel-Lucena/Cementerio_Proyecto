package com.mlg.cementerio.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mlg.cementerio.entity.Rol;
import com.mlg.cementerio.entity.Usuario;
import com.mlg.cementerio.repository.RolRepository;
import com.mlg.cementerio.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario en el sistema, cifrando su contraseña y asignando el rol de usuario por defecto.
     */
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
            throw new RuntimeException("Usuario ya existe");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Rol rol = rolRepository.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no existe"));

        usuario.setRol(rol); 

        return usuarioRepository.save(usuario);
    }
}