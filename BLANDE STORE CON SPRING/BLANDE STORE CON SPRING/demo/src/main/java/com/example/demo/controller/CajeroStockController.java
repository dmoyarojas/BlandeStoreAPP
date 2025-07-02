package com.example.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.example.demo.model.Categoria;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.TipoRopaRepository;
import com.example.demo.service.InventarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cajero/stock")
public class CajeroStockController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private TipoRopaRepository tipoRopaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public String mostrarStockCajero(
        @RequestParam(required = false) Long tipoId,
        @RequestParam(required = false) Long categoriaId,
        @RequestParam(required = false) String talla,
        HttpSession session,
        Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"cajero".equals(usuario.getRol())) {
            return "redirect:/";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("productos", inventarioService.filtrarProductos(tipoId, categoriaId, talla));
        model.addAttribute("tipos", tipoRopaRepository.findAll());
        model.addAttribute("categorias", inventarioService.listarCategorias());

        return "stock";
    }

    @GetMapping("/categorias")
    @ResponseBody
    public List<Map<String, Object>> listarCategoriasPorTipo(@RequestParam Long tipoId) {
        List<Categoria> categorias = categoriaRepository.findByTipoId(tipoId);
        return categorias.stream().map(cat -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cat.getId());
            map.put("nombreCategoria", cat.getNombreCategoria());
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarInventarioExcelCajero(
        @RequestParam(required = false) Long tipoId,
        @RequestParam(required = false) Long categoriaId,
        @RequestParam(required = false) String talla
    ) throws IOException {
        byte[] contenidoExcel = inventarioService.exportarInventarioExcel(tipoId, categoriaId, talla);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Inventario.xlsx");

        return new ResponseEntity<>(contenidoExcel, headers, HttpStatus.OK);
    }
}