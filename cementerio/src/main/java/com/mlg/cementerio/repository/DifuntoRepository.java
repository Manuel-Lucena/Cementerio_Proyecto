package com.mlg.cementerio.repository;

import com.mlg.cementerio.entity.Difunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DifuntoRepository extends JpaRepository<Difunto, Integer> {

    @Query("SELECT d FROM Difunto d WHERE d.concesion.cliente.usuario.id = :usuarioId AND d.estado = 'ACTIVO'")
    List<Difunto> findMisDifuntosActivos(@Param("usuarioId") Long usuarioId);

    List<Difunto> findByConcesionId(Integer idConcesion);
}