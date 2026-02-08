package com.mlg.cementerio.controller;

import com.mlg.cementerio.entity.Pago;
import com.mlg.cementerio.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.util.Map;

/**
 * Controlador que gestiona el flujo de pagos con la pasarela.
 */
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Inicia el proceso de pago generando una orden y retornando la URL de la pasarela.
     * @param body Mapa que contiene el id_factura.
     * @return JSON con la URL a la que debe redirigirse el cliente.
     */
    @PostMapping("/init")
    public ResponseEntity<Map<String, String>> init(@RequestBody Map<String, Object> body) {
        Integer facturaId = (Integer) body.get("id_factura");
        String redirectUrl = paymentService.inicializarPago(facturaId);
        return ResponseEntity.ok(Map.of("redirectUrl", redirectUrl));
    }

    /**
     * Consulta el estado de un pago mediante su ID de orden.
     * @param orderId Identificador único de la transacción.
     * @return Información de la entidad Pago.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Pago> getPago(@PathVariable String orderId) {
        return ResponseEntity.ok(paymentService.obtenerPago(orderId));
    }

    /**
     * Recibe la respuesta de la pasarela y redirige al usuario al frontend de Angular.
     * @param orderId ID de la transacción procesada.
     * @param responseCode Código resultado (0000 para éxito).
     * @return Redirección a la página de resultado en el frontend.
     */
    @GetMapping("/callback")
    public RedirectView callback(@RequestParam String orderId, @RequestParam String responseCode) {
        paymentService.procesarCallback(orderId, responseCode);
        return new RedirectView("http://localhost:4200/pago-resultado?orderId=" + orderId);
    }
}