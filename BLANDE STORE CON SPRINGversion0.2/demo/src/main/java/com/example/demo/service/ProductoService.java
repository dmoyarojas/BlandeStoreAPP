package com.example.demo.service;
import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public Producto obtenerPorCodigo(String codigoBarras) {
        Optional<Producto> producto = productoRepository.findById(codigoBarras);
        return producto.orElse(null);
    }
    public void eliminarProducto(String codigoBarras) {
        productoRepository.deleteById(codigoBarras);
    }

    public Producto marcarComoVendido(String codigoBarras) {
        Producto producto = productoRepository.findById(codigoBarras)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setVendido(true);
        return productoRepository.save(producto);
    }

    public Producto marcarComoDisponible(String codigoBarras) {
        Producto producto = productoRepository.findById(codigoBarras)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setVendido(false);
        return productoRepository.save(producto);
    }

    public Producto obtenerDisponible(String codigoBarras) {
        return productoRepository.findByCodigoBarrasAndVendidoFalse(codigoBarras)
                .orElseThrow(() -> new RuntimeException("Producto no disponible"));
    }
}
