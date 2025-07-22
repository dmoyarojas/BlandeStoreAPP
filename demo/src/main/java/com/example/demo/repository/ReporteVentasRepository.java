package com.example.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Venta;

public interface ReporteVentasRepository extends JpaRepository<Venta, Long> {
    @Query("""
            SELECT v FROM Venta v WHERE
            (COALESCE(:tipo, '') = '' OR v.tipoComprobante = :tipo) AND
            (:fechaInicio IS NULL OR v.fecha >= :fechaInicio) AND
            (:fechaFin IS NULL OR v.fecha <= :fechaFin)
            ORDER BY v.id ASC
            """)
    List<Venta> filtrarVentas(
        @Param("tipo") String tipo,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin
    );

   @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin")
BigDecimal sumTotalByFechaBetween(
    @Param("inicio") LocalDateTime inicio,
    @Param("fin") LocalDateTime fin
);
}