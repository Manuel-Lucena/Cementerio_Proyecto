package com.mlg.cementerio.repository;

import com.mlg.cementerio.entity.Concesion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcesionRepository extends JpaRepository<Concesion, Integer> {

    List<Concesion> findByClienteId(Long clienteId);

    @Query(value = "SELECT COUNT(*) FROM concesion WHERE fecha_fin < CURRENT_DATE", nativeQuery = true)
    Long contarVencidas();

    @Query(value = "SELECT COUNT(*) FROM concesion WHERE fecha_fin >= CURRENT_DATE", nativeQuery = true)
    Long contarActivas();
}
