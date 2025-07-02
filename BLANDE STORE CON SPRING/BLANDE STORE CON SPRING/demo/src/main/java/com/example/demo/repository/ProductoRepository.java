package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, String> {
 // Buscar por tipo, categoría y talla
    @Query("SELECT p FROM Producto p WHERE p.tipo.id = :tipoId AND p.categoria.id = :categoriaId AND p.talla = :talla")
    List<Producto> findByTipoAndCategoriaAndTalla(
        @Param("tipoId") Long tipoId,
        @Param("categoriaId") Long categoriaId,
        @Param("talla") String talla
    );
    @Query("SELECT p FROM Producto p WHERE p.tipo.id = :tipoId AND p.categoria.id = :categoriaId")
    List<Producto> findByTipoIdAndCategoriaId(
    @Param("tipoId") Long tipoId, 
    @Param("categoriaId") Long categoriaId
    );
    // Buscar por tipo
    List<Producto> findByTipoId(Long tipoId);

    // Buscar por categoría
    List<Producto> findByCategoriaId(Long categoriaId);

    // Buscar por talla
    List<Producto> findByTalla(String talla);
    


    //estos metodos son para validar si un producto ya fue vendido
    List<Producto> findByCodigoBarrasContainingIgnoreCase(String codigoBarras);

    // Métodos para validar si un producto ya fue vendido
    Optional<Producto> findByCodigoBarrasAndVendidoFalse(String codigoBarras);
    
    // Buscar todos los disponibles
    List<Producto> findByVendidoFalse();
    
}
