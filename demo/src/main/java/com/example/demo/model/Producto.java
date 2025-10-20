package com.example.demo.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_barras")
    private Long codigoBarras;  // Cambiar de String a Long

    @ManyToOne(fetch = FetchType.EAGER)  
    @JoinColumn(name = "id_tipo", nullable = false)
    private TipoRopa tipo;

   @ManyToOne(fetch = FetchType.EAGER)  
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(length = 10)
    private String talla;

    @Column(length = 30) // Nuevo campo para el color
    private String color;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    
    @Column(nullable = false)
    private boolean vendido = false; // Solo este campo adicional

    @Column(name = "etiqueta_impresa", nullable = false)
    private boolean etiquetaImpresa = false;

    // Getters y Setters
    public Long getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(Long codigoBarras) { this.codigoBarras = codigoBarras; }

    // Propiedad transitoria para UI/JSON: 7 dígitos con ceros a la izquierda
    @Transient
    @JsonProperty("codigoBarrasStr")
    public String getCodigoBarrasStr() {
        if (this.codigoBarras == null) return null;
        return String.format("%07d", this.codigoBarras);
    }

    public TipoRopa getTipo() { return tipo; }
    public void setTipo(TipoRopa tipo) { this.tipo = tipo; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    // Getters y Setters (mantén los que ya tienes y añade)
    public boolean isVendido() {
        return vendido;
    }

    public void setVendido(boolean vendido) {
        this.vendido = vendido;
    }

     public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isEtiquetaImpresa() {
        return etiquetaImpresa;
    }

    public void setEtiquetaImpresa(boolean etiquetaImpresa) {
        this.etiquetaImpresa = etiquetaImpresa;
    }
}