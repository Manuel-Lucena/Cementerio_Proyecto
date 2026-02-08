package com.mlg.cementerio.controller;

import com.mlg.cementerio.entity.Pago;
import com.mlg.cementerio.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controlador de simulación (Mock) para emular la interfaz de Redsys.
 * Utiliza vistas de servidor (Thymeleaf) en lugar de JSON.
 */
@Controller
@RequestMapping("/mock-redsys")
public class RedsysMockController {

    @Autowired
    private PagoRepository pagoRepo;

    /**
     * Carga la página visual del banco simulado.
     * @param orderId ID de la orden de pago.
     * @param model Modelo para pasar datos a la vista HTML.
     * @return Nombre del template HTML "redsys-mock".
     */
    @GetMapping("/pay")
    public String showMockPage(@RequestParam String orderId, Model model) {
        Pago pago = pagoRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        model.addAttribute("orderId", orderId);
        model.addAttribute("total", pago.getTotal());
        model.addAttribute("facturaId", pago.getIdFactura());
        
        return "redsys-mock"; 
    }

    /**
     * Procesa la acción del usuario en el simulador (Aceptar/Cancelar) y retorna al backend.
     * @param orderId ID de la transacción.
     * @param responseCode Código simulado enviado por el formulario del mock.
     * @return Redirección al endpoint de callback de la aplicación.
     */
    @PostMapping("/result")
    public RedirectView processResult(@RequestParam String orderId, @RequestParam String responseCode) {
        return new RedirectView("/api/payment/callback?orderId=" + orderId + "&responseCode=" + responseCode);
    }
}