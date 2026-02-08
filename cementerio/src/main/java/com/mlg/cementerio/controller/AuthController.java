package com.mlg.cementerio.controller;

import com.mlg.cementerio.dto.LoginRequest;
import com.mlg.cementerio.dto.RegistroClienteRequest;
import com.mlg.cementerio.dto.TokenResponse;
import com.mlg.cementerio.entity.Cliente;
import com.mlg.cementerio.entity.Usuario;
import com.mlg.cementerio.service.ClienteService;
import com.mlg.cementerio.service.UsuarioService;
import com.mlg.cementerio.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestión de autenticación y registro de usuarios.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") 
public class AuthController {

    private final ClienteService clienteService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UsuarioService usuarioService,
                          ClienteService clienteService,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.clienteService = clienteService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Registra un nuevo cliente junto con su usuario en el sistema.
     * @param request Datos de registro (nombre, email, password, etc.).
     * @return Entidad Cliente creada.
     */
    @PostMapping("/register")
    public ResponseEntity<Cliente> register(@Valid @RequestBody RegistroClienteRequest request) {
        Cliente cliente = clienteService.registrarClienteCompleto(request);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Autentica a un usuario y genera un token JWT.
     * @param loginRequest Credenciales de acceso (username y password).
     * @return TokenResponse que contiene el JWT generado.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        
        Usuario usuario = (Usuario) auth.getPrincipal();
        String token = jwtService.generateToken(usuario);
        
        return ResponseEntity.ok(new TokenResponse(token));
    }
}