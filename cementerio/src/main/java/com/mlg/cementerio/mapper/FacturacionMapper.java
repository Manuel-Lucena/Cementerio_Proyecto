package com.mlg.cementerio.mapper;

import com.mlg.cementerio.entity.Concesion;
import com.mlg.cementerio.entity.Factura;
import com.mlg.cementerio.entity.Sepultura;
import com.mlg.cementerio.dto.FacturaDTO;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class FacturacionMapper {

    public Factura toFactura(Concesion c, Sepultura s, Double total, String concepto) {
        Factura factura = null;

        if (c != null && s != null) {
            factura = Factura.builder()
                    .concesion(c)
                    .sepultura(s)
                    .total(total)
                    .fechaFactura(LocalDate.now())
                    .estado("Pendiente")
                    .concepto(concepto)
                    .build();
        }

        return factura;
    }

    public FacturaDTO toDTO(Factura factura) {
        FacturaDTO dto = null;

        if (factura != null) {
            dto = FacturaDTO.builder()
                    .id(factura.getId())
                    .total(factura.getTotal())
                    .fechaFactura(factura.getFechaFactura())
                    .estado(factura.getEstado())
                    .nombreCliente(factura.getConcesion() != null && factura.getConcesion().getCliente() != null 
                            ? factura.getConcesion().getCliente().getNombre() : "N/A")
                    .apellidosCliente(factura.getConcesion() != null && factura.getConcesion().getCliente() != null 
                            ? factura.getConcesion().getCliente().getApellidos() : "N/A")
                    .codigoSepultura(factura.getSepultura() != null 
                            ? factura.getSepultura().getCodigo_visual() : "N/A")
                    .concepto(factura.getConcepto())
                    .build();
        }

        return dto;
    }
}