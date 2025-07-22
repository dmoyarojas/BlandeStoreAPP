package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Usuario;
import com.example.demo.service.InventarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class GenerarVentasController {
     private final InventarioService inventarioService;
    
    public GenerarVentasController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }
    
    @GetMapping("/Generar-Ventas")
    public String mostrarReporteVentas(
            HttpSession session, 
            Model model,
            @RequestParam(required = false) String codigoBarras) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"cajero".equals(usuario.getRol())) {
            return "redirect:/login-cajero";
        }

        if (codigoBarras != null && !codigoBarras.isEmpty()) {
            model.addAttribute("productos", inventarioService.buscarPorCodigo(codigoBarras));
        }
        
        model.addAttribute("usuario", usuario); 
        return "generarVentas"; 
    }
}
