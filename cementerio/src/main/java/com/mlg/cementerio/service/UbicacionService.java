package com.mlg.cementerio.service;

import com.mlg.cementerio.exceptions.ResourceNotFoundException;
import com.mlg.cementerio.mapper.UbicacionMapper;
import com.mlg.cementerio.dto.LocalidadDTO;
import com.mlg.cementerio.dto.ProvinciaDTO;
import com.mlg.cementerio.repository.ProvinciaRepository;
import com.mlg.cementerio.repository.LocalidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UbicacionService {

    private final ProvinciaRepository provinciaRepo;
    private final LocalidadRepository localidadRepo;
    private final UbicacionMapper mapper;

    /**
     * Obtiene el listado de todas las provincias disponibles.
     */
    @Transactional(readOnly = true)
    public List<ProvinciaDTO> listarProvincias() {
        List<ProvinciaDTO> provincias = provinciaRepo.findAll().stream()
                .map(mapper::toProvinciaDTO)
                .toList();
        return provincias;
    }

    /**
     * Lista las localidades pertenecientes a una provincia determinada.
     */
    @Transactional(readOnly = true)
    public List<LocalidadDTO> listarLocalidadesPorProvincia(Integer idProvincia) {
        List<LocalidadDTO> localidades = localidadRepo.findByProvinciaId(idProvincia).stream()
                .map(mapper::toLocalidadDTO)
                .toList();
        return localidades;
    }

    /**
     * Recupera la información de una localidad específica por su ID.
     */
    @Transactional(readOnly = true)
    public LocalidadDTO obtenerLocalidadPorId(Integer id) {
        LocalidadDTO resultado = localidadRepo.findById(id)
                .map(mapper::toLocalidadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Localidad no encontrada"));
        return resultado;
    }
}