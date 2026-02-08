package com.mlg.cementerio.repository;

import com.mlg.cementerio.entity.Agrupacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AgrupacionRepository extends JpaRepository<Agrupacion, Integer> {
    List<Agrupacion> findByCementerioId(Integer idCementerio);
}