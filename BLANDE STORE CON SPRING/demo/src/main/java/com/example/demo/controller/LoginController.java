package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String mostrarLogin(Model model) {
        model.addAttribute("error", null);
        return "index";
    }

    // Nuevo endpoint para mostrar formulario de login admin
    @GetMapping("/login-admin")
    public String mostrarLoginAdmin(Model model) {
        return "login-admin";
    }

    // Nuevo endpoint para mostrar formulario de login cajero
    @GetMapping("/login-cajero")
    public String mostrarLoginCajero(Model model) {
        return "login-cajero";
    }

    // Endpoint para procesar login admin
    @PostMapping("/login-admin")
    public String loginAdmin(@RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        Usuario usuario = usuarioService.autenticar(username, password);
        if (usuario != null && "admin".equals(usuario.getRol())) {
            session.setAttribute("usuario", usuario);
            return "redirect:/admin/menu";
        } else {
            model.addAttribute("error", "Credenciales incorrectas o no tiene permisos de administrador");
            return "login-admin";
        }
    }

    // Endpoint para procesar login cajero
    @PostMapping("/login-cajero")
    public String loginCajero(@RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        Usuario usuario = usuarioService.autenticar(username, password);
        if (usuario != null && "cajero".equals(usuario.getRol())) {
            session.setAttribute("usuario", usuario);
            return "redirect:/cajero/menu";
        } else {
            model.addAttribute("error", "Credenciales incorrectas o no tiene permisos de cajero");
            return "login-cajero";
        }
    }
@GetMapping("/logout")
public String cerrarSesion(HttpSession session) {
    session.invalidate(); // Invalida la sesión actual
    return "redirect:/";  // Redirige al login principal (index)
}

}