package uniandes.dpoo.hamburguesas.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import uniandes.dpoo.hamburguesas.excepciones.*;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest {
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
    	restaurante = new Restaurante();
        File carpetaFacturas = new File("data/facturas");
       
    }
  
    @Test
    void testIniciarPedido() throws YaHayUnPedidoEnCursoException {
        restaurante.iniciarPedido("Juan Pérez", "Calle 123");
        assertNotNull(restaurante.getPedidoEnCurso(), "El pedido en curso no debería ser null después de iniciarlo.");

        assertThrows(YaHayUnPedidoEnCursoException.class, () -> {
            restaurante.iniciarPedido("Ana Gómez", "Calle 456");
        }, "Debería lanzar YaHayUnPedidoEnCursoException al intentar iniciar otro pedido en curso.");
    }

    @Test
    void testCerrarYGuardarPedido() throws YaHayUnPedidoEnCursoException, IOException, NoHayPedidoEnCursoException {
        // Establecer la ruta para las facturas en la carpeta `data/facturas`
        File carpetaFacturas = new File("data/facturas");
        restaurante.iniciarPedido("Juan Pérez", "Calle 123");
        restaurante.cerrarYGuardarPedido();
        assertNull(restaurante.getPedidoEnCurso(), "El pedido en curso debería ser null después de cerrarlo.");
        assertFalse(restaurante.getPedidos().isEmpty(), "La lista de pedidos cerrados no debería estar vacía.");

        // Verificar que se lanza la excepción si no hay un pedido en curso
        assertThrows(NoHayPedidoEnCursoException.class, () -> {
            restaurante.cerrarYGuardarPedido();
        }, "Debería lanzar NoHayPedidoEnCursoException al intentar cerrar un pedido sin haber iniciado uno.");
    }


    @Test
    void testCargarInformacionRestaurante() throws IOException, HamburguesaException {
        // Crear archivos temporales para ingredientes, menú y combos
        File archivoIngredientes = File.createTempFile("ingredientesTest", ".txt");
        File archivoMenu = File.createTempFile("menuTest", ".txt");
        File archivoCombos = File.createTempFile("combosTest", ".txt");

        archivoIngredientes.deleteOnExit();
        archivoMenu.deleteOnExit();
        archivoCombos.deleteOnExit();

        // Escribir datos de prueba en los archivos temporales
        Files.write(archivoIngredientes.toPath(), List.of("Lechuga;200", "Tomate;300"));
        Files.write(archivoMenu.toPath(), List.of("Hamburguesa;5000", "Papas Fritas;2000"));
        Files.write(archivoCombos.toPath(), List.of("Combo1;10%;Hamburguesa;Papas Fritas"));

        assertDoesNotThrow(() -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        });

        assertFalse(restaurante.getIngredientes().isEmpty(), "La lista de ingredientes no debería estar vacía.");
        assertFalse(restaurante.getMenuBase().isEmpty(), "La lista del menú base no debería estar vacía.");
        assertFalse(restaurante.getMenuCombos().isEmpty(), "La lista de combos no debería estar vacía.");
    }

    @Test
    void testGuardarFacturaEnRutaInvalida() throws YaHayUnPedidoEnCursoException {
        restaurante.iniciarPedido("Juan Pérez", "Calle 123");

        // Intentar guardar una factura en una ruta inválida
        File archivoInvalido = new File("/ruta/invalida/factura.txt");
        assertThrows(IOException.class, () -> {
            restaurante.cerrarYGuardarPedido();
        }, "Debería lanzar IOException cuando la ruta del archivo es inválida.");
    }

    @Test
    void testGetPedidoEnCursoSinPedidoIniciado() {
        assertNull(restaurante.getPedidoEnCurso(), "El pedido en curso debería ser null si no se ha iniciado ningún pedido.");
    }

    @Test
    void testGetPedidosVacio() {
        assertTrue(restaurante.getPedidos().isEmpty(), "La lista de pedidos cerrados debería estar vacía al inicio.");
    }

    @Test
    void testGetIngredientesMenuBaseYCombos() throws IOException, HamburguesaException {
        // Crear archivos temporales y agregar datos para probar la carga de información
        File archivoIngredientes = File.createTempFile("ingredientesTest", ".txt");
        File archivoMenu = File.createTempFile("menuTest", ".txt");
        File archivoCombos = File.createTempFile("combosTest", ".txt");

        archivoIngredientes.deleteOnExit();
        archivoMenu.deleteOnExit();
        archivoCombos.deleteOnExit();

        Files.write(archivoIngredientes.toPath(), List.of("Lechuga;200", "Tomate;300"));
        Files.write(archivoMenu.toPath(), List.of("Hamburguesa;5000", "Papas Fritas;2000"));
        Files.write(archivoCombos.toPath(), List.of("Combo1;10%;Hamburguesa;Papas Fritas"));

        restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);

        // Verificar que las listas no están vacías después de la carga
        assertFalse(restaurante.getIngredientes().isEmpty(), "La lista de ingredientes no debería estar vacía.");
        assertFalse(restaurante.getMenuBase().isEmpty(), "La lista del menú base no debería estar vacía.");
        assertFalse(restaurante.getMenuCombos().isEmpty(), "La lista de combos no debería estar vacía.");
    }
}

