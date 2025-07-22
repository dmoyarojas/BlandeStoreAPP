package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String mostrarLogin(Model model) {
        model.addAttribute("error", null);
        return "index";

    }

    @GetMapping("/login-admin")
    public String mostrarLoginAdmin(Model model) {
        return "login-admin";
    }

    @GetMapping("/login-cajero")
    public String mostrarLoginCajero(Model model) {
        return "login-cajero";
    }

    @PostMapping("/login-admin")
    public String loginAdmin(@RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        long inicio = System.currentTimeMillis(); // 🔍 inicio

        Usuario usuario = usuarioService.autenticar(username, password);
        if (usuario != null && "admin".equals(usuario.getRol())) {
            session.setAttribute("usuario", usuario);
            redirectAttributes.addFlashAttribute("loginSuccess", true);
            redirectAttributes.addFlashAttribute("nombreUsuario", usuario.getUsername());
            redirectAttributes.addFlashAttribute("userRol", usuario.getRol());

            long fin = System.currentTimeMillis(); // 🔍 fin
            logger.info("Login ADMIN exitoso de '{}'. Tiempo: {} ms", username, (fin - inicio));

            return "redirect:/admin/menu";
        } else {
            long fin = System.currentTimeMillis();
            logger.warn("Login ADMIN fallido para '{}'. Tiempo: {} ms", username, (fin - inicio));

            model.addAttribute("error", "Credenciales incorrectas o no tiene permisos de administrador");
            return "login-admin";
        }
    }

    @PostMapping("/login-cajero")
    public String loginCajero(@RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        long inicio = System.currentTimeMillis(); // 🔍 inicio

        Usuario usuario = usuarioService.autenticar(username, password);
        if (usuario != null && "cajero".equals(usuario.getRol())) {
            session.setAttribute("usuario", usuario);
            redirectAttributes.addFlashAttribute("loginSuccess", true);
            redirectAttributes.addFlashAttribute("nombreUsuario", usuario.getUsername());
            redirectAttributes.addFlashAttribute("userRol", usuario.getRol());

            long fin = System.currentTimeMillis(); // 🔍 fin
            logger.info("Login CAJERO exitoso de '{}'. Tiempo: {} ms", username, (fin - inicio));

            return "redirect:/cajero/menu";
        } else {
            long fin = System.currentTimeMillis();
            logger.warn("Login CAJERO fallido para '{}'. Tiempo: {} ms", username, (fin - inicio));

            model.addAttribute("error", "Credenciales incorrectas o no tiene permisos de cajero");
            return "login-cajero";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}