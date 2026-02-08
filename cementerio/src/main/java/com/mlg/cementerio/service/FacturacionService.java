package com.mlg.cementerio.service;

import com.mlg.cementerio.dto.ContratarServiciosDTO;
import com.mlg.cementerio.dto.FacturaDTO;
import com.mlg.cementerio.entity.*;
import com.mlg.cementerio.exceptions.ResourceNotFoundException;
import com.mlg.cementerio.mapper.FacturacionMapper;
import com.mlg.cementerio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturacionService {

    private final FacturaRepository facturaRepo;
    private final ServicioContratadoRepository servContratadoRepo;
    private final CementerioServicioRepository cemServicioRepo;
    private final DifuntoRepository difuntoRepo;
    private final UsuarioRepository usuarioRepo;
    private final FacturacionMapper mapper;

    /**
     * Registra la contratación de nuevos servicios para un difunto.
     */
    @Transactional
    public FacturaDTO contratarServicios(ContratarServiciosDTO dto) {
        Difunto difunto = difuntoRepo.findById(dto.getIdDifunto())
                .orElseThrow(() -> new ResourceNotFoundException("Difunto no encontrado"));

        List<CementerioServicio> servicios = cemServicioRepo.findAllById(dto.getIdsCementerioServicios());
        if (servicios.isEmpty()) {
            throw new ResourceNotFoundException("Seleccione al menos un servicio válido");
        }

        Double total = servicios.stream().mapToDouble(CementerioServicio::getCoste).sum();
        String conceptoDinamico = servicios.stream()
                .map(cs -> cs.getServicio().getNombre())
                .collect(Collectors.joining(", "));

        Factura nuevaFactura = mapper.toFactura(difunto.getConcesion(), difunto.getConcesion().getSepultura(), total, conceptoDinamico);
        Factura facturaGuardada = facturaRepo.save(nuevaFactura);

        servicios.forEach(cs -> {
            ServicioContratado sc = new ServicioContratado();
            sc.setDifunto(difunto);
            sc.setCementerioServicio(cs);
            sc.setFechaContratacion(LocalDate.now());
            sc.setCoste(cs.getCoste());
            sc.setEstado("Pendiente");
            servContratadoRepo.save(sc);
        });

        return mapper.toDTO(facturaGuardada);
    }

    /**
     * Lista las facturas con paginacion desde la base de datos.
     */
    @Transactional(readOnly = true)
    public Page<FacturaDTO> listarFacturasUsuario(String username, String estado, String campo, String dir, int page, int size) {
        Usuario usuario = usuarioRepo.findByNombreUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // 1. Creamos la configuración de ordenación
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(campo).ascending() : Sort.by(campo).descending();
        
        // 2. Creamos el objeto Pageable
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Factura> paginaEntidades;

        // 3. Consultamos al repositorio según si hay filtro de estado o no
        if (estado == null || estado.equalsIgnoreCase("TODAS")) {
            paginaEntidades = facturaRepo.findByConcesionClienteUsuarioId(usuario.getId(), pageable);
        } else {
            paginaEntidades = facturaRepo.findByConcesionClienteUsuarioIdAndEstadoIgnoreCase(usuario.getId(), estado, pageable);
        }

        // 4. Convertimos la página de Entidades a página de DTOs usando el mapper
        return paginaEntidades.map(mapper::toDTO);
    }

    /**
     * Anula una factura existente.
     */
    @Transactional
    public void eliminarFactura(Integer id) {
        Factura factura = facturaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada"));
        factura.setEstado("Anulada");
        facturaRepo.save(factura);
    }
}