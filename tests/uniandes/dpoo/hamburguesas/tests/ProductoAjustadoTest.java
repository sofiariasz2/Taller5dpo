package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;

public class ProductoAjustadoTest {

    private ProductoAjustado productoAjustado;
    private ProductoMenu productoBase;
    private Ingrediente ingrediente1;
    private Ingrediente ingrediente2;
    private Ingrediente ingrediente3;

    @BeforeEach
    void setUp() {
        // Crear el producto base
        productoBase = new ProductoMenu("Hamburguesa", 5000);
        
        // Crear el producto ajustado basado en el producto base
        productoAjustado = new ProductoAjustado(productoBase);

        // Crear ingredientes de prueba
        ingrediente1 = new Ingrediente("Queso", 800);
        ingrediente2 = new Ingrediente("Tocineta", 1000);
        ingrediente3 = new Ingrediente("Cebolla", 500);
    }

    @Test
    void testGetNombre() {
        assertEquals("Hamburguesa", productoAjustado.getNombre(), "El nombre del producto ajustado no es el esperado.");
    }

    @Test
    void testGetPrecio() {
        // Agregar ingredientes adicionales usando el nuevo método
        productoAjustado.agregarIngrediente(ingrediente1);
        productoAjustado.agregarIngrediente(ingrediente2);

        // Calcular el precio esperado: precio base + ingredientes adicionales
        int precioEsperado = productoBase.getPrecio() + ingrediente1.getCostoAdicional() + ingrediente2.getCostoAdicional();
        
        assertEquals(precioEsperado, productoAjustado.getPrecio(), "El precio del producto ajustado no es el esperado.");
    }

    @Test
    void testGenerarTextoFactura() {
        // Agregar ingredientes adicionales y eliminados usando los nuevos métodos
        productoAjustado.agregarIngrediente(ingrediente1);
        productoAjustado.agregarIngrediente(ingrediente2);
        productoAjustado.eliminarIngrediente(ingrediente3);

        // Generar el texto esperado para la factura
        String textoEsperado = "Hamburguesa\n" +
                               "    +Queso                800\n" +
                               "    +Tocineta                1000\n" +
                               "    -Cebolla\n" +
                               "            " + productoAjustado.getPrecio() + "\n";

        assertEquals(textoEsperado, productoAjustado.generarTextoFactura(), "El texto de la factura no es el esperado.");
    }
}

