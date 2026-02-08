package com.mlg.cementerio.repository;

import com.mlg.cementerio.entity.Sepultura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SepulturaRepository extends JpaRepository<Sepultura, Integer> {

    // Para listar las sepulturas de un sector
    List<Sepultura> findByAgrupacionId(Integer agrupacionId);

    @Modifying
    @Query("DELETE FROM Sepultura s WHERE s.agrupacion.id = :agrupacionId") 
    void deleteByAgrupacionId(@Param("agrupacionId") Integer agrupacionId);

    boolean existsByAgrupacionIdAndOcupadoTrue(Integer agrupacionId);
}