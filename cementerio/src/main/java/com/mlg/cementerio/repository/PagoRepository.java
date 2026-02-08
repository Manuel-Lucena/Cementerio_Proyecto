package com.mlg.cementerio.repository;

import com.mlg.cementerio.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, String> {
}