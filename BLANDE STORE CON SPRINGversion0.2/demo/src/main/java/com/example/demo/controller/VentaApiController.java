package com.example.demo.controller;
import com.example.demo.model.DetalleVenta;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.model.Venta;
import com.example.demo.repository.MetodoPagoRepository;

import com.example.demo.service.ProductoService;
import com.example.demo.service.VentaService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.demo.model.MetodoPago;

@RestController
@RequestMapping("/api/ventas")
public class VentaApiController {

    @Autowired
    private ProductoService productoService;
    
    
    
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;
    
    @Autowired
    private VentaService ventaService;

    @PostMapping("/agregar-producto")
    public ResponseEntity<?> agregarProducto(@RequestBody Map<String, Object> request, HttpSession session) {
        try {
            String codigoBarras = (String) request.get("codigoBarras");
            
            if (codigoBarras == null || codigoBarras.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Código de barras no proporcionado"));
            }

            // Buscar producto disponible (no vendido)
            Producto producto = productoService.obtenerDisponible(codigoBarras);
            
            // Marcamos como vendido al agregar al carrito
            producto = productoService.marcarComoVendido(codigoBarras);
            
            List<Producto> carrito = obtenerCarritoDeSesion(session);
            
            if (carrito.stream().anyMatch(p -> p.getCodigoBarras().equals(codigoBarras))) {
                // Si ya estaba en el carrito, revertimos el estado
                productoService.marcarComoDisponible(codigoBarras);
                return ResponseEntity.badRequest().body(Map.of("error", "El producto ya está en el carrito"));
            }
            
            carrito.add(producto);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Producto agregado correctamente",
                "producto", Map.of(
                    "codigoBarras", producto.getCodigoBarras(),
                    "tipo", Map.of("nombreTipo", producto.getTipo().getNombreTipo()),
                    "categoria", Map.of("nombreCategoria", producto.getCategoria().getNombreCategoria()),
                    "talla", producto.getTalla(),
                    "precio", producto.getPrecio()
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al agregar producto: " + e.getMessage()));
        }
    }

    @GetMapping("/metodos-pago")
    public ResponseEntity<List<MetodoPago>> obtenerMetodosPago() {
        return ResponseEntity.ok(metodoPagoRepository.findAll());
    }
    @GetMapping("/carrito")
    public ResponseEntity<?> obtenerCarrito(HttpSession session) {
        try {
            List<Producto> carrito = obtenerCarritoDeSesion(session);
            
            if (carrito.isEmpty()) {
                return ResponseEntity.ok(List.of());
            }
            
            List<Map<String, Object>> response = carrito.stream()
                .map(p -> Map.of(
                    "codigoBarras", p.getCodigoBarras(),
                    "tipo", Map.of("nombreTipo", p.getTipo().getNombreTipo()),
                    "categoria", Map.of("nombreCategoria", p.getCategoria().getNombreCategoria()),
                    "talla", p.getTalla(),
                    "precio", p.getPrecio()
                ))
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al cargar el carrito: " + e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar-producto/{codigoBarras}")
    public ResponseEntity<?> eliminarProducto(@PathVariable String codigoBarras, HttpSession session) {
        try {
            List<Producto> carrito = obtenerCarritoDeSesion(session);
            
            boolean removido = carrito.removeIf(p -> {
                if (p.getCodigoBarras().equals(codigoBarras)) {
                    productoService.marcarComoDisponible(codigoBarras); // Cambio clave: Reactivamos el producto
                    return true;
                }
                return false;
            });
            
            if (!removido) {
                return ResponseEntity.badRequest().body(Map.of("error", "Producto no encontrado en el carrito"));
            }
            
            return ResponseEntity.ok(Map.of("success", true, "message", "Producto eliminado del carrito"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al eliminar producto: " + e.getMessage()));
        }
    }

    @GetMapping("/total")
    public ResponseEntity<?> calcularTotal(HttpSession session) {
        try {
            List<Producto> carrito = obtenerCarritoDeSesion(session);
            BigDecimal total = carrito.stream()
                    .map(Producto::getPrecio)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al calcular total: " + e.getMessage()));
        }
    }

    @PostMapping("/confirmar-venta")
public ResponseEntity<?> confirmarVenta(
        @RequestBody Map<String, Object> requestData,
        HttpSession session) {
    
    try {
        // 1. Validar usuario y sesión
        Usuario cajero = (Usuario) session.getAttribute("usuario");
        if (cajero == null || !"cajero".equals(cajero.getRol())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "No autorizado o sesión expirada"));
        }

        // 2. Validar datos de entrada
        //if (requestData.get("nombreCliente") == null || ((String) requestData.get("nombreCliente")).trim().isEmpty()) {
        //    return ResponseEntity.badRequest()
        //            .body(Map.of("error", "El nombre del cliente es requerido"));
        //}
        
        if (requestData.get("tipoComprobante") == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El tipo de comprobante es requerido"));
        }
        
        if (requestData.get("metodoPagoId") == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El método de pago es requerido"));
        }

        //String nombreCliente = ((String) requestData.get("nombreCliente")).trim();
        String tipoComprobante = (String) requestData.get("tipoComprobante");
        
        Long metodoPagoId;
        try {
            metodoPagoId = Long.parseLong(requestData.get("metodoPagoId").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID de método de pago inválido"));
        }

        // 3. Validar carrito
        List<Producto> productosCarrito = obtenerCarritoDeSesion(session);
        if (productosCarrito.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El carrito está vacío"));
        }

        // 4. Preparar venta Y ELIMINAR productos de la base de datos
        Venta venta = new Venta();
        venta.setCajero(cajero);
        //venta.setNombreCliente(nombreCliente);
        venta.setTipoComprobante(tipoComprobante);
        
        MetodoPago metodoPago = metodoPagoRepository.findById(metodoPagoId)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
        venta.setMetodoPago(metodoPago);

        // 5. Preparar detalles
        List<DetalleVenta> detalles = new ArrayList<>();
        for (Producto producto : productosCarrito) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(1);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setVenta(venta);
            detalles.add(detalle);

            // Eliminar el producto de la base de datos
            productoService.eliminarProducto(producto.getCodigoBarras());
        }
        
        // 6. Establecer relación bidireccional
        venta.setDetalles(detalles);

        // 7. Registrar venta
        Venta ventaRegistrada = ventaService.registrarVenta(venta);
        
        // 8. Limpiar carrito solo si todo salió bien
        session.removeAttribute("carrito");
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "ventaId", ventaRegistrada.getId(),
            "message", "Venta registrada exitosamente",
            "total", ventaRegistrada.getTotal()
        ));
        
    } catch (Exception e) {
        // Log del error completo
        e.printStackTrace();
        
        // Reactivar productos en caso de error
        List<Producto> carrito = obtenerCarritoDeSesion(session);
        carrito.forEach(p -> productoService.marcarComoDisponible(p.getCodigoBarras()));
        
        return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage() != null ? e.getMessage() : "Error desconocido al confirmar venta"));
    }
}

    @PostMapping("/cancelar-venta")
    public ResponseEntity<?> cancelarVenta(HttpSession session) {
        try {
            List<Producto> carrito = obtenerCarritoDeSesion(session);
            
            // Reactivar todos los productos
            carrito.forEach(p -> productoService.marcarComoDisponible(p.getCodigoBarras()));
            
            // Limpiar carrito
            session.removeAttribute("carrito");
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Venta cancelada y productos reactivados",
                "productosReactivos", carrito.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al cancelar venta: " + e.getMessage()));
        }
    }

    @SuppressWarnings("unchecked")
    private List<Producto> obtenerCarritoDeSesion(HttpSession session) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }
}
