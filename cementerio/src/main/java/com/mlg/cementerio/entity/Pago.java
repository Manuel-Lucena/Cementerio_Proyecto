package com.mlg.cementerio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PAGO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "id_factura", nullable = false)
    private Integer idFactura;

    @Column(nullable = false)
    private Double total;


    @Column(length = 20)
    private String estado; 

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;
}