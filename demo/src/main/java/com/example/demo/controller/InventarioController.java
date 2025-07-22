package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Categoria;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.TipoRopaRepository;
import com.example.demo.service.InventarioService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin/inventario")
public class InventarioController {
    private final TipoRopaRepository tipoRopaRepository;
    @Autowired
    private InventarioService inventarioService;
    @Autowired
    private CategoriaRepository CategoriaRepository;
    InventarioController(TipoRopaRepository tipoRopaRepository) {
        this.tipoRopaRepository = tipoRopaRepository;
    }

    // Mostrar formulario de inventario
   @GetMapping
    public String mostrarInventario(Model model, HttpSession session) {
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        return "redirect:/login-admin"; // Redirige si no hay sesión
    }
    model.addAttribute("usuario", usuario); // Pasa el usuario a la vista
    model.addAttribute("tipos", tipoRopaRepository.findAll());

    return "inventario";
}
    // Guardar producto
   @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        inventarioService.guardarProducto(producto);
        return "redirect:/admin/stock";
    }

    
    // API para categorías (usada por AJAX)
    @GetMapping("/categorias")
@ResponseBody
public List<Map<String, Object>> listarCategoriasPorTipo(@RequestParam Long tipoId) {
    List<Categoria> categorias = CategoriaRepository.findByTipoId(tipoId);
    return categorias.stream().map(cat -> {
        Map<String, Object> map = new HashMap<>();
        map.put("id", cat.getId()); 
        map.put("nombreCategoria", cat.getNombreCategoria());
        return map;
    }).collect(Collectors.toList());
}

}
