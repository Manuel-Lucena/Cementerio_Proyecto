package com.mlg.cementerio.repository;

import com.mlg.cementerio.entity.Empresa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    Optional<Empresa> findByUsuarioNombreUsuario(String nombreUsuario);
}