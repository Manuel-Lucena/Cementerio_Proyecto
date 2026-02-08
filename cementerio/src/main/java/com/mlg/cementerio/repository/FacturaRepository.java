package com.mlg.cementerio.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.mlg.cementerio.entity.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    
    Page<Factura> findByConcesionClienteUsuarioId(Long usuarioId, Pageable pageable);

    Page<Factura> findByConcesionClienteUsuarioIdAndEstadoIgnoreCase(Long usuarioId, String estado, Pageable pageable);

    @Query(value = "SELECT COALESCE(SUM(total), 0) FROM factura WHERE estado != 'Pendiente'", nativeQuery = true)
    Double sumarIngresosTotales();
}