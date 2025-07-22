package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, String> {
    // Buscar por tipo, categoría y talla (solo disponibles)
    @Query("SELECT p FROM Producto p WHERE p.tipo.id = :tipoId AND p.categoria.id = :categoriaId AND p.talla = :talla AND p.vendido = false")
    List<Producto> findByTipoAndCategoriaAndTalla(
        @Param("tipoId") Long tipoId,
        @Param("categoriaId") Long categoriaId,
        @Param("talla") String talla
    );
    @Query("SELECT p FROM Producto p WHERE p.tipo.id = :tipoId AND p.categoria.id = :categoriaId AND p.vendido = false")
    List<Producto> findByTipoIdAndCategoriaId(
    @Param("tipoId") Long tipoId, 
    @Param("categoriaId") Long categoriaId
    );
    // Buscar por tipo (solo disponibles)
    List<Producto> findByTipoIdAndVendidoFalse(Long tipoId);

    // Buscar por categoría (solo disponibles)
    List<Producto> findByCategoriaIdAndVendidoFalse(Long categoriaId);

    // Buscar por talla (solo disponibles)
    List<Producto> findByTallaAndVendidoFalse(String talla);
    


    //estos metodos son para validar si un producto ya fue vendido
    List<Producto> findByCodigoBarrasContainingIgnoreCase(String codigoBarras);

    // Métodos para validar si un producto ya fue vendido
    Optional<Producto> findByCodigoBarrasAndVendidoFalse(String codigoBarras);
    
    // Buscar todos los disponibles
    List<Producto> findByVendidoFalse();
    
      // Nuevos métodos para filtrar por color (solo disponibles)
@Query("SELECT p FROM Producto p WHERE p.tipo.id = :tipoId AND p.categoria.id = :categoriaId AND p.talla = :talla AND p.color = :color AND p.vendido = false")
    List<Producto> findByTipoAndCategoriaAndTallaAndColor(
        @Param("tipoId") Long tipoId,
        @Param("categoriaId") Long categoriaId,
        @Param("talla") String talla,
        @Param("color") String color
    );

    @Query("SELECT p FROM Producto p WHERE p.tipo.id = :tipoId AND p.categoria.id = :categoriaId AND p.color = :color AND p.vendido = false")
    List<Producto> findByTipoAndCategoriaAndColor(
        @Param("tipoId") Long tipoId,
        @Param("categoriaId") Long categoriaId,
        @Param("color") String color
    );

    @Query("SELECT p FROM Producto p WHERE p.tipo.id = :tipoId AND p.talla = :talla AND p.color = :color AND p.vendido = false")
    List<Producto> findByTipoAndTallaAndColor(
        @Param("tipoId") Long tipoId,
        @Param("talla") String talla,
        @Param("color") String color
    );

    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId AND p.talla = :talla AND p.color = :color AND p.vendido = false")
    List<Producto> findByCategoriaAndTallaAndColor(
        @Param("categoriaId") Long categoriaId,
        @Param("talla") String talla,
        @Param("color") String color
    );

    @Query("SELECT p FROM Producto p WHERE p.tipo.id = :tipoId AND p.color = :color AND p.vendido = false")
    List<Producto> findByTipoIdAndColor(
        @Param("tipoId") Long tipoId,
        @Param("color") String color
    );

    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId AND p.color = :color AND p.vendido = false")
    List<Producto> findByCategoriaIdAndColor(
        @Param("categoriaId") Long categoriaId,
        @Param("color") String color
    );

    @Query("SELECT p FROM Producto p WHERE p.talla = :talla AND p.color = :color AND p.vendido = false")
    List<Producto> findByTallaAndColor(
        @Param("talla") String talla,
        @Param("color") String color
    );

    @Query("SELECT p FROM Producto p WHERE p.color = :color AND p.vendido = false")
    List<Producto> findByColor(@Param("color") String color);

    @Query("SELECT DISTINCT p.color FROM Producto p WHERE p.color IS NOT NULL AND p.vendido = false")
    List<String> findDistinctColors();
    
    
    @Query("SELECT p FROM Producto p WHERE p.tipo.id = :tipoId AND p.talla = :talla AND p.vendido = false")
    List<Producto> findByTipoIdAndTalla(
        @Param("tipoId") Long tipoId,
        @Param("talla") String talla
    );

    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId AND p.talla = :talla AND p.vendido = false")
    List<Producto> findByCategoriaIdAndTalla(
        @Param("categoriaId") Long categoriaId,
        @Param("talla") String talla
    );
}
