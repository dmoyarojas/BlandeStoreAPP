package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Venta;
import com.example.demo.repository.ReporteVentasRepository;
@Service
@Transactional
public class ReporteVentasService {

    private final ReporteVentasRepository reporteVentasRepository;

    public ReporteVentasService(ReporteVentasRepository reporteVentasRepository) {
        this.reporteVentasRepository = reporteVentasRepository;
    }

public BigDecimal calcularGananciasDiarias(LocalDate fecha) {
    LocalDateTime inicioDia = fecha.atStartOfDay();
    LocalDateTime finDia = fecha.atTime(23, 59, 59);
    
    return reporteVentasRepository.sumTotalByFechaBetween(inicioDia, finDia);
}


 public List<Venta> generarReporte(LocalDate fechaInicio, LocalDate fechaFin, String tipo) {
    System.out.println("Generando reporte con:");
    System.out.println("Tipo: " + tipo);
    System.out.println("Fecha inicio: " + fechaInicio);
    System.out.println("Fecha fin: " + fechaFin);
    
    LocalDateTime inicio = (fechaInicio != null) ? fechaInicio.atStartOfDay() : null;
    LocalDateTime fin = (fechaFin != null) ? fechaFin.atTime(23, 59, 59) : null;
    
    // Si el tipo es vacío, pasar null
    String tipoConsulta = (tipo == null || tipo.isEmpty()) ? null : tipo;
    
    List<Venta> ventas = reporteVentasRepository.filtrarVentas(tipoConsulta, inicio, fin);
    
    System.out.println("Ventas encontradas: " + ventas.size());
    return ventas;
}

public byte[] exportarReporteVentasExcel(LocalDate fechaInicio, LocalDate fechaFin, String tipo) throws IOException {
    try {
        List<Venta> ventas = generarReporte(fechaInicio, fechaFin, tipo);
        
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Ventas");
            
            // Estilo para cabeceras
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            // Cabeceras
            String[] headers = {"ID", "Fecha", "Tipo Comprobante", "Total (S/)"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Formato de fecha
            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm"));
            
            // Datos
            int rowNum = 1;
            for (Venta venta : ventas) {
                Row row = sheet.createRow(rowNum++);
                
                // ID
                row.createCell(0).setCellValue(venta.getId());
                
                // Fecha
                Cell fechaCell = row.createCell(1);
                fechaCell.setCellValue(venta.getFecha());
                fechaCell.setCellStyle(dateStyle);
                
                // Tipo Comprobante
                row.createCell(2).setCellValue(venta.getTipoComprobante());
                
                // Total
                if (venta.getTotal() != null) {
                    row.createCell(3).setCellValue(venta.getTotal().doubleValue());
                } else {
                    row.createCell(3).setCellValue(0.0);
                }
            }
            
            // Autoajustar columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    } catch (Exception e) {
        throw new IOException("Error al generar Excel: " + e.getMessage(), e);
    }
}
}
