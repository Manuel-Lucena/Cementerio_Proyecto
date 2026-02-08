package com.mlg.cementerio.service;

import com.mlg.cementerio.entity.Factura;
import com.mlg.cementerio.entity.Pago;
import com.mlg.cementerio.entity.Difunto;
import com.mlg.cementerio.repository.FacturaRepository;
import com.mlg.cementerio.repository.PagoRepository;
import com.mlg.cementerio.repository.DifuntoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PagoRepository pagoRepo;
    private final FacturaRepository facturaRepo;
    private final DifuntoRepository difuntoRepo;

    /**
     * Inicializa un proceso de pago generando una orden única y la URL de redirección a la pasarela.
     */
    @Transactional
    public String inicializarPago(Integer facturaId) {
        Factura factura = facturaRepo.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        String orderId = UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        Pago nuevoPago = Pago.builder()
                .orderId(orderId)
                .idFactura(facturaId)
                .total(factura.getTotal())
                .estado("PENDIENTE")
                .build();

        pagoRepo.save(nuevoPago);
        return "http://localhost:8090/mock-redsys/pay?orderId=" + orderId;
    }

    /**
     * Recupera los detalles de un pago mediante su ID de orden.
     */
    public Pago obtenerPago(String orderId) {
        return pagoRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado: " + orderId));
    }

    /**
     * Gestiona el resultado del pago, actualizando la factura y activando a los difuntos si el resultado es exitoso.
     */
    @Transactional
    public void procesarCallback(String orderId, String responseCode) {
        Optional<Pago> pagoOpt = pagoRepo.findById(orderId);
        if (pagoOpt.isEmpty()) return;

        Pago pago = pagoOpt.get();
        if (!"PENDIENTE".equals(pago.getEstado())) return;

        if ("0000".equals(responseCode)) {
            pago.setEstado("SUCCESS");
            pago.setFechaPago(LocalDateTime.now());

            facturaRepo.findById(pago.getIdFactura()).ifPresent(factura -> {
                factura.setEstado("PAGADA");
                facturaRepo.save(factura);

                if (factura.getConcesion() != null) {
                    List<Difunto> difuntos = difuntoRepo.findByConcesionId(factura.getConcesion().getId());
                    if (!difuntos.isEmpty()) {
                        difuntos.forEach(d -> d.setEstado("ACTIVO"));
                        difuntoRepo.saveAll(difuntos);
                    }
                }
            });
        } else {
            pago.setEstado("FAILED");
        }
        pagoRepo.save(pago);
    }
}