package com.mlg.cementerio.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaDTO {
    private Integer id;
    private Double total;
    private LocalDate fechaFactura;
    private String estado;
    private String nombreCliente;
    private String apellidosCliente;
    private String codigoSepultura;

    private String concepto; 
}