package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
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
        HttpServletRequest request,
        RedirectAttributes redirectAttributes) {

    Usuario usuario = usuarioService.autenticar(username, password);
    if (usuario != null && "admin".equals(usuario.getRol())) {
        // Prevenir Session Fixation
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("usuario", usuario);

        redirectAttributes.addFlashAttribute("loginSuccess", true);
        redirectAttributes.addFlashAttribute("nombreUsuario", usuario.getUsername());
        redirectAttributes.addFlashAttribute("userRol", usuario.getRol()); // Nuevo atributo
        return "redirect:/admin/menu";
    } else {
        model.addAttribute("error", "Credenciales incorrectas o no tiene permisos de administrador");
        return "login-admin";
    }
}

@PostMapping("/login-cajero")
public String loginCajero(@RequestParam String username,
        @RequestParam String password,
        Model model,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes) {

    Usuario usuario = usuarioService.autenticar(username, password);
    if (usuario != null && "cajero".equals(usuario.getRol())) {
        // Prevenir Session Fixation
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("usuario", usuario);
        
        redirectAttributes.addFlashAttribute("loginSuccess", true);
        redirectAttributes.addFlashAttribute("nombreUsuario", usuario.getUsername());
        redirectAttributes.addFlashAttribute("userRol", usuario.getRol()); // Nuevo atributo
        return "redirect:/cajero/menu";
    } else {
        model.addAttribute("error", "Credenciales incorrectas o no tiene permisos de cajero");
        return "login-cajero";
    }
}

    @GetMapping("/logout")
    public String cerrarSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
