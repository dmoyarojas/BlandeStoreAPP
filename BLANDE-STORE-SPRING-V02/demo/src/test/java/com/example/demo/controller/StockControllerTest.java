package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testMostrarStock() throws Exception {
        // Arrange: Crear un usuario simulado para la sesión
        Usuario usuarioSimulado = new Usuario();
        usuarioSimulado.setUsername("testadmin");
        usuarioSimulado.setRol("ADMIN");

        // Act & Assert
        mockMvc.perform(get("/admin/stock")
                .sessionAttr("usuario", usuarioSimulado)) // Simular el atributo de sesión
                .andExpect(status().isOk()) // Verificar que el estado HTTP es 200 OK
                .andExpect(view().name("stock")) // Verificar que se renderiza la vista "stock"
                .andExpect(model().attributeExists("productos")) // Verificar que el modelo contiene el atributo "productos"
                .andExpect(model().attributeExists("tipos"))
                .andExpect(model().attributeExists("categorias"));
    }

    @Test
    public void testMostrarStock_sinUsuarioEnSesion() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/admin/stock"))
                .andExpect(status().is3xxRedirection()) // Verificar que hay una redirección (302)
                .andExpect(view().name("redirect:/login-admin")); // Verificar que redirige a la página de login
    }
}
