package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.Usuario;

import jakarta.servlet.http.HttpSession;

@Controller
public class GenerarVentasController {
     @GetMapping("/Generar-Ventas")
    public String mostrarReporteVentas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login-cajero";
        }

        model.addAttribute("usuario", usuario); 
        return "generarVentas"; 
    }
}
