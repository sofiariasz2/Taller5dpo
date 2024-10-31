package uniandes.dpoo.hamburguesas.tests;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest {

    private ProductoMenu producto;

    
    //Configuro el objeto producto que voy a probar, esto para crear una instancia de cada prueba 
    //para que cada una pueda usar esta misma
    
    
    @BeforeEach
    void setUp() {
        producto = new ProductoMenu("Hamburguesa", 5000);
    }

    //Verifica que getNombre() devuelva el nombre correcto del producto
    @Test
    void testGetNombre() {
        assertEquals("Hamburguesa", producto.getNombre(), "El nombre del producto no es el esperado");
    }
    
    
    //Verifica que getPrecio devuelva el precio base correcto del producto

    @Test
    void testGetPrecio() {
        assertEquals(5000, producto.getPrecio(), "El precio del producto no es el esperado");
    }
    
    
    //Verifica que el texto de la factura este en formato correcto incluyendo el nimbre y el precio del producto

    @Test
    void testGenerarTextoFactura() {
        String textoEsperado = "Hamburguesa\n            5000\n";
        assertEquals(textoEsperado, producto.generarTextoFactura(), "El texto de la factura no es el esperado");
    }
}
