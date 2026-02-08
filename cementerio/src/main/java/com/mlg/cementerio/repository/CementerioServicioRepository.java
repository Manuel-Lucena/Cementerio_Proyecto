package com.mlg.cementerio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mlg.cementerio.entity.CementerioServicio;

import java.util.List;

@Repository
public interface CementerioServicioRepository extends JpaRepository<CementerioServicio, Integer> {

    List<CementerioServicio> findByCementerioIdAndActivoTrue(Integer cementerioId);


    List<CementerioServicio> findByCementerioId(Integer cementerioId);
    
    @Modifying
    @Transactional
    @Query("UPDATE CementerioServicio cs SET cs.activo = false WHERE cs.cementerio.id = :id")
    void desactivarServiciosPorCementerioId(@Param("id") Integer id);
    
    @Modifying
    @Transactional
    @Query("UPDATE CementerioServicio cs SET cs.activo = false WHERE cs.id = :id")
    void desactivarServicioPorId(@Param("id") Integer id);
}