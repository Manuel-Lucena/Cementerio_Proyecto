package com.mlg.cementerio.service;

import com.mlg.cementerio.dto.*;
import com.mlg.cementerio.entity.*;
import com.mlg.cementerio.exceptions.BadRequestException;
import com.mlg.cementerio.exceptions.ResourceNotFoundException;
import com.mlg.cementerio.repository.*;
import lombok.RequiredArgsConstructor;
import com.mlg.cementerio.mapper.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcesionService {

    private final ConcesionRepository concesionRepo;
    private final DifuntoRepository difuntoRepo;
    private final SepulturaRepository sepulturaRepo;
    private final ClienteRepository clienteRepo;
    private final FacturaRepository facturaRepo;
    private final UsuarioRepository usuarioRepo;

    private final ConcesionMapper concesionMapper;
    private final DifuntoMapper difuntoMapper;
    private final FacturacionMapper facturacionMapper;

    /**
     * Registra un trámite completo de concesión: valida disponibilidad, crea la
     * concesión,
     * registra al difunto, ocupa la sepultura y genera la factura inicial.
     */
    @Transactional
    public void registrarTramiteCompleto(TramiteConcesionDTO dto, String username) {

        Usuario usuario = usuarioRepo.findByNombreUsuario(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));

        Cliente cliente = clienteRepo.findByUsuarioId(usuario.getId().longValue())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró perfil de cliente"));

        Sepultura sepultura = sepulturaRepo.findById(dto.getIdSepultura())
                .orElseThrow(() -> new ResourceNotFoundException("Sepultura no encontrada"));

        if (Boolean.TRUE.equals(sepultura.getOcupado())) {
            throw new BadRequestException("La sepultura ya se encuentra ocupada");
        }

        Concesion concesion = concesionRepo.save(concesionMapper.toEntity(dto, cliente));

        Difunto difunto = difuntoMapper.toEntity(dto.getDifunto(), concesion);
        difunto.setEstado("PENDIENTE");
        difuntoRepo.save(difunto);

        sepultura.setOcupado(true);
        sepultura.setConcesion(concesion);
        sepulturaRepo.save(sepultura);

        String concepto = "Adquisición de Concesión: " + sepultura.getCodigo_visual();
        Factura factura = facturacionMapper.toFactura(concesion, sepultura, dto.getTotalPagar(), concepto);
        factura.setEstado("Pendiente");

        facturaRepo.save(factura);
    }

    /**
     * Obtiene el listado de concesiones de un cliente para mostrar en el panel
     * admin.
     * Realiza la conversión de Entidad a DTO usando el mapper.
     */
    @Transactional(readOnly = true)
    public List<ConcesionDTO> obtenerPorClienteId(Integer clienteId) {
   
        List<Concesion> concesiones = concesionRepo.findByClienteId(clienteId.longValue());

        return concesiones.stream()
                .map(concesionMapper::toDto)
                .collect(Collectors.toList());
    }
}