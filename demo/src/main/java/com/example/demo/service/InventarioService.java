package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream; 


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.example.demo.model.Categoria;
import com.example.demo.model.Producto;
import com.example.demo.model.TipoRopa;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.TipoRopaRepository;

import jakarta.transaction.Transactional;

@Service
public class InventarioService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private TipoRopaRepository tipoRopaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    // Guardar producto
    @Transactional
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // Listar todos los productos disponibles (no vendidos)
    public List<Producto> listarProductos() {
        return productoRepository.findByVendidoFalse();
    }

    // Filtrar productos
    public List<Producto> filtrarProductos(Long tipoId, Long categoriaId, String talla, String color) {
    if (tipoId != null && categoriaId != null && talla != null && color != null) {
        return productoRepository.findByTipoAndCategoriaAndTallaAndColor(tipoId, categoriaId, talla, color);
    } else if (tipoId != null && categoriaId != null && talla != null) {
        return productoRepository.findByTipoAndCategoriaAndTalla(tipoId, categoriaId, talla);
    } else if (tipoId != null && categoriaId != null && color != null) {
        return productoRepository.findByTipoAndCategoriaAndColor(tipoId, categoriaId, color);
    } else if (tipoId != null && talla != null && color != null) {
        return productoRepository.findByTipoAndTallaAndColor(tipoId, talla, color);
    } else if (categoriaId != null && talla != null && color != null) {
        return productoRepository.findByCategoriaAndTallaAndColor(categoriaId, talla, color);
    } else if (tipoId != null && categoriaId != null) {
        return productoRepository.findByTipoIdAndCategoriaId(tipoId, categoriaId);
    } else if (tipoId != null && talla != null) {
        return productoRepository.findByTipoIdAndTalla(tipoId, talla);
    } else if (tipoId != null && color != null) {
        return productoRepository.findByTipoIdAndColor(tipoId, color);
    } else if (categoriaId != null && talla != null) {
        return productoRepository.findByCategoriaIdAndTalla(categoriaId, talla);
    } else if (categoriaId != null && color != null) {
        return productoRepository.findByCategoriaIdAndColor(categoriaId, color);
    } else if (talla != null && color != null) {
        return productoRepository.findByTallaAndColor(talla, color);
    } else if (tipoId != null) {
        return productoRepository.findByTipoIdAndVendidoFalse(tipoId);
    } else if (categoriaId != null) {
        return productoRepository.findByCategoriaIdAndVendidoFalse(categoriaId);
    } else if (talla != null) {
        return productoRepository.findByTallaAndVendidoFalse(talla);
    } else if (color != null) {
        return productoRepository.findByColor(color);
    }
    return listarProductos();
}

    // Obtener tipos de ropa
    public List<TipoRopa> listarTiposRopa() {
        return tipoRopaRepository.findAll();
    }

    // Obtener categorías por tipo
    public List<Categoria> listarCategoriasPorTipo(Long tipoId) {
        return categoriaRepository.findByTipoId(tipoId);
    }
    public List<Categoria> listarCategorias() {
    return categoriaRepository.findAll();
}
public List<Producto> buscarPorCodigo(String codigoBarras) {
    return productoRepository.findByCodigoBarrasContainingIgnoreCase(codigoBarras);
}

/**
 * Exporta la lista de productos del inventario a un archivo Excel (.xlsx)
 * @return byte[] con el contenido del Excel listo para descargar
 * @throws IOException Si ocurre un error al generar el archivo
 */
public byte[] exportarInventarioExcel(Long tipoId, Long categoriaId, String talla,String color) throws IOException {
    List<Producto> productos = filtrarProductos(tipoId, categoriaId, talla,color); // usa tu lógica actual

    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("Inventario");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        String[] headers = {"Código", "Tipo", "Categoría", "Talla", "Color", "Precio"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (Producto p : productos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(p.getCodigoBarras());
            row.createCell(1).setCellValue(p.getTipo().getNombreTipo());
            row.createCell(2).setCellValue(p.getCategoria().getNombreCategoria());
            row.createCell(3).setCellValue(p.getTalla());
            row.createCell(4).setCellValue(p.getColor()); // Nueva columna para el color
            row.createCell(5).setCellValue(p.getPrecio().doubleValue());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        return out.toByteArray();
    }
}
public Map<String, Long> contarProductosPorTipo() {
    return productoRepository.findByVendidoFalse().stream()
        .collect(Collectors.groupingBy(
            p -> p.getTipo().getNombreTipo(),
            Collectors.counting()
        ));
}

public Map<String, Long> contarProductosPorTipoYCategoria() {
    return productoRepository.findByVendidoFalse().stream()
        .collect(Collectors.groupingBy(
            p -> p.getTipo().getNombreTipo() + " - " + p.getCategoria().getNombreCategoria(),
            Collectors.counting()
        ));
}

public Map<String, Long> contarProductosPorTipoCategoriaYTalla() {
    return productoRepository.findByVendidoFalse().stream()
        .collect(Collectors.groupingBy(
            p -> p.getTipo().getNombreTipo() + " - " + 
                 p.getCategoria().getNombreCategoria() + " - " + 
                 p.getTalla(),
            Collectors.counting()
        ));
}

/// metodos para graficos



public Map<String, Long> contarProductosPorCategoria() {
    return productoRepository.findByVendidoFalse().stream()
        .collect(Collectors.groupingBy(
            p -> p.getCategoria().getNombreCategoria(),
            Collectors.counting()
        ));
}

}
