package com.mlg.cementerio.repository;

import com.mlg.cementerio.entity.Localidad;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



public interface LocalidadRepository extends JpaRepository<Localidad, Integer> {

    List<Localidad> findByProvinciaId(Integer idProvincia);
}