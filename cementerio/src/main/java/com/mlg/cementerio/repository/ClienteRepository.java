package com.mlg.cementerio.repository;

import com.mlg.cementerio.entity.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// El ID de Cliente es Integer, pero el de Usuario es Long
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Query("SELECT c FROM Cliente c WHERE c.usuario.id = :idUsuario")
    Optional<Cliente> findByUsuarioId(@Param("idUsuario") Long idUsuario); 
}