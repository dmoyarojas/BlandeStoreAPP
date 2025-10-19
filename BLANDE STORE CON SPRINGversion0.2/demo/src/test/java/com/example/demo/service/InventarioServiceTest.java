// package com.example.demo.service;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;

// import java.math.BigDecimal;
// import java.util.Arrays;
// import java.util.List;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import com.example.demo.model.Categoria;
// import com.example.demo.model.Producto;
// import com.example.demo.model.TipoRopa;
// import com.example.demo.repository.ProductoRepository;

// @ExtendWith(MockitoExtension.class)
// public class InventarioServiceTest {

//     @Mock
//     private ProductoRepository productoRepository;

//     @InjectMocks
//     private InventarioService inventarioService;

//     @Test
//     public void testListarProductos() {
//         // 1. Arrange: Configurar el comportamiento del mock
//         TipoRopa tipo = new TipoRopa();
//         tipo.setId(1L);
//         tipo.setNombreTipo("Polo");

//         Categoria categoria = new Categoria();
//         categoria.setId(1L);
//         categoria.setNombreCategoria("Algodon");
//         categoria.setTipo(tipo);

//         Producto p1 = new Producto();
//         p1.setCodigoBarras("12345");
//         p1.setTipo(tipo);
//         p1.setCategoria(categoria);
//         p1.setTalla("M");
//         p1.setPrecio(new BigDecimal("29.90"));

//         Producto p2 = new Producto();
//         p2.setCodigoBarras("67890");
//         p2.setTipo(tipo);
//         p2.setCategoria(categoria);
//         p2.setTalla("L");
//         p2.setPrecio(new BigDecimal("39.90"));

//         List<Producto> productosMock = Arrays.asList(p1, p2);

//         when(productoRepository.findAll()).thenReturn(productosMock);

//         // 2. Act: Llamar al método que se está probando
//         List<Producto> productosResult = inventarioService.listarProductos();

//         // 3. Assert: Verificar el resultado
//         assertEquals(2, productosResult.size());
//         assertEquals("12345", productosResult.get(0).getCodigoBarras());
//         assertEquals("67890", productosResult.get(1).getCodigoBarras());
//     }
// }
