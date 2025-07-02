package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    // Aquí puedes agregar métodos específicos para la entidad Venta si es necesario

}
