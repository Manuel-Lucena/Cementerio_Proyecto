package com.mlg.cementerio.repository;

import com.mlg.cementerio.entity.Cementerio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CementerioRepository extends JpaRepository<Cementerio, Integer> {
    List<Cementerio> findByEmpresaId( Integer idEmpresa);
}