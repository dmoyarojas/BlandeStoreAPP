package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.MetodoPago;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
    // Aquí puedes agregar métodos específicos para la entidad MetodoPago si es necesario

}
