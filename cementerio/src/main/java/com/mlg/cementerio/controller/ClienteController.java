package com.mlg.cementerio.controller;

import com.mlg.cementerio.dto.ClienteDTO;
import com.mlg.cementerio.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador para la gestión de los datos personales y de contacto de los clientes.
 */
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Obtiene la lista completa de clientes registrados.
     * @return Lista de ClienteDTO.
     */
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        List<ClienteDTO> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Actualiza el perfil de un cliente existente.
     * @param id ID del cliente a modificar.
     * @param dto Nuevos datos del cliente.
     * @return ClienteDTO actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody ClienteDTO dto) {
        ClienteDTO actualizado = clienteService.actualizarCliente(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Elimina a un cliente del sistema.
     * @param id ID del cliente a borrar.
     * @return 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}