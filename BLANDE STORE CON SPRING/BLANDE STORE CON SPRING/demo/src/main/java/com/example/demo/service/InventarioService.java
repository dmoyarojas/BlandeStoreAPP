package com.example.demo.service;

import java.io.IOException;
import java.util.List;

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

    // Listar todos los productos
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    // Filtrar productos
    public List<Producto> filtrarProductos(Long tipoId, Long categoriaId, String talla) {
        if (tipoId != null && categoriaId != null && talla != null) {
            return productoRepository.findByTipoAndCategoriaAndTalla(tipoId, categoriaId, talla);
        } else if (tipoId != null && categoriaId != null) {
            return productoRepository.findByTipoIdAndCategoriaId(tipoId, categoriaId);
        } else if (tipoId != null) {
            return productoRepository.findByTipoId(tipoId);
        } else if (categoriaId != null) {
            return productoRepository.findByCategoriaId(categoriaId);
        } else if (talla != null) {
            return productoRepository.findByTalla(talla);
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
public byte[] exportarInventarioExcel(Long tipoId, Long categoriaId, String talla) throws IOException {
    List<Producto> productos = filtrarProductos(tipoId, categoriaId, talla); // usa tu lógica actual

    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("Inventario");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        String[] headers = {"Código", "Tipo", "Categoría", "Talla", "Precio"};
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
            row.createCell(4).setCellValue(p.getPrecio().doubleValue());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        return out.toByteArray();
    }
}



}