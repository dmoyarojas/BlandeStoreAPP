package com.example.demo.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.DetalleVenta;
import com.example.demo.model.Usuario;
import com.example.demo.model.Venta;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ReporteVentasController {

    private final ReporteVentasService reporteVentasService;

    // Inyección de dependencia del servicio
    public ReporteVentasController(ReporteVentasService reporteVentasService) {
        this.reporteVentasService = reporteVentasService;
    }

    @GetMapping("/reporte-ventas")
    public String mostrarReporteVentas(
            HttpSession session,
            Model model,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
            @RequestParam(required = false) String tipo) {

        // Validación de sesión (manteniendo tu lógica actual)
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login-admin";
        }

        // Obtener las ventas filtradas
        List<Venta> ventas = reporteVentasService.generarReporte(fechaInicio, fechaFin, tipo);

        // Calcular ganancias del día actual
        BigDecimal gananciasHoy = reporteVentasService.calcularGananciasDiarias(LocalDate.now());
        // Agregar atributos al modelo
        model.addAttribute("gananciasHoy", gananciasHoy);
        model.addAttribute("usuario", usuario);
        model.addAttribute("ventas", ventas);

        return "reporte-ventas";
    }
    @GetMapping("/ganancias-diarias")
@ResponseBody
public BigDecimal obtenerGananciasDiarias() {
    return reporteVentasService.calcularGananciasDiarias(LocalDate.now());
}

    // Añadir este nuevo método para exportar Excel
    @GetMapping("/reporte-ventas/exportar-excel")
    public ResponseEntity<byte[]> exportarVentasExcel(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
            @RequestParam(required = false) String tipo) throws IOException {

        // Verificar parámetros
        System.out.println("Export Excel - fechaInicio: " + fechaInicio);
        System.out.println("Export Excel - fechaFin: " + fechaFin);
        System.out.println("Export Excel - tipo: " + tipo);

        byte[] excelContent = reporteVentasService.exportarReporteVentasExcel(
                fechaInicio,
                fechaFin,
                tipo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Reporte_Ventas.xlsx");

    return ResponseEntity.ok()
                .headers(headers)
                .body(excelContent);
    }

    @GetMapping("/ventas/{id}")
    @ResponseBody
    public ResponseEntity<?> obtenerDetalleVenta(@PathVariable Long id) {
        return reporteVentasService.obtenerVentaPorId(id)
            .map(venta -> {
                Map<String, Object> response = new HashMap<>();
                response.put("cajero", venta.getCajero().getUsername());
                response.put("detalles", venta.getDetalles().stream().map(detalle -> {
                    Map<String, Object> detalleMap = new HashMap<>();
                    String nombreProducto = detalle.getProducto().getTipo().getNombreTipo() + " " + detalle.getProducto().getCategoria().getNombreCategoria();
                    detalleMap.put("productoNombre", nombreProducto);
                    detalleMap.put("cantidad", detalle.getCantidad());
                    detalleMap.put("precioUnitario", detalle.getPrecioUnitario());
                    detalleMap.put("subtotal", detalle.getSubtotal());
                    return detalleMap;
                }).collect(Collectors.toList()));
                return ResponseEntity.ok(response);
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
