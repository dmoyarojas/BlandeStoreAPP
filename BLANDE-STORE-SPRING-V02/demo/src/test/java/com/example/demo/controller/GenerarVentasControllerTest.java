package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Usuario;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GenerarVentasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testMostrarReporteVentas_conCajeroEnSesion() throws Exception {
        // Arrange
        Usuario cajero = new Usuario();
        cajero.setUsername("testcajero");
        cajero.setRol("cajero");

        // Act & Assert
        mockMvc.perform(get("/Generar-Ventas")
                .sessionAttr("usuario", cajero))
                .andExpect(status().isOk())
                .andExpect(view().name("generarVentas"));
    }

    @Test
    public void testMostrarReporteVentas_sinUsuarioEnSesion() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/Generar-Ventas"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login-cajero"));
    }

    @Test
    public void testMostrarReporteVentas_conRolIncorrecto() throws Exception {
        // Arrange
        Usuario admin = new Usuario();
        admin.setUsername("testadmin");
        admin.setRol("ADMIN");

        // Act & Assert
        mockMvc.perform(get("/Generar-Ventas")
                .sessionAttr("usuario", admin))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login-cajero"));
    }
}
