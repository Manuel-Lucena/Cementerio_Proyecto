package com.mlg.cementerio.repository;

import com.mlg.cementerio.entity.ServicioContratado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioContratadoRepository extends JpaRepository<ServicioContratado, Integer> {

}