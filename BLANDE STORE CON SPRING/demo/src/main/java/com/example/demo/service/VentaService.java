package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.demo.model.Venta;

import com.example.demo.repository.VentaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VentaService {
    
    @Autowired
    private VentaRepository ventaRepository;

    public Venta registrarVenta(Venta venta) {
        // Calcular total basado en los detalles
        BigDecimal total = venta.getDetalles().stream()
            .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        venta.setTotal(total);
        venta.setFecha(LocalDateTime.now());
        
        // Guardar la venta (esto debería guardar en cascada los detalles)
        return ventaRepository.save(venta);
    }
}

