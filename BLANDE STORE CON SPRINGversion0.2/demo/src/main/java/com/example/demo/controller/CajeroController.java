package com.example.demo.controller;
import com.example.demo.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/cajero")
public class CajeroController {
     
    @GetMapping("/menu")
    public String menuCajero(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"cajero".equals(usuario.getRol())) {
            return "menu-cajero";
        }
        model.addAttribute("usuario", usuario);
        return "menu-cajero";
    }
}