package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.Producto;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class PedidoTest {

    private Pedido pedido;
    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        pedido = new Pedido("Juan Perez", "Calle 123");
        producto1 = new ProductoMenu("Hamburguesa", 5000);
        producto2 = new ProductoMenu("Papas", 2000);
    }

    @Test
    void testAgregarProducto() {
        pedido.agregarProducto(producto1);
        pedido.agregarProducto(producto2);
        int precioNetoEsperado = producto1.getPrecio() + producto2.getPrecio();
        int precioTotalEsperado = precioNetoEsperado + (int) (precioNetoEsperado * Pedido.getIVA());
        assertEquals(precioTotalEsperado, pedido.getPrecioTotalPedido(), 
                     "El precio total del pedido no coincide con la suma de los productos agregados más el IVA.");
    }

    @Test
    void testAgregarProductoDuplicado() {
        pedido.agregarProducto(producto1);
        pedido.agregarProducto(producto1); // Producto duplicado
        int precioNetoEsperado = producto1.getPrecio() * 2;
        int precioTotalEsperado = precioNetoEsperado + (int) (precioNetoEsperado * Pedido.getIVA());
        assertEquals(precioTotalEsperado, pedido.getPrecioTotalPedido(), 
                     "El precio total no coincide al agregar el mismo producto dos veces más el IVA.");
    }

    @Test
    void testAgregarProductoNulo() {
        assertThrows(NullPointerException.class, () -> {
            pedido.agregarProducto(null);
        }, "Debería lanzar NullPointerException al intentar agregar un producto nulo.");
    }

    @Test
    void testGetIdPedido() {
        int idEsperado = 2; 
        assertEquals(idEsperado, pedido.getIdPedido(), "El ID del pedido no coincide con el valor esperado.");
    }

    @Test
    void testGetNombreCliente() {
        String nombreEsperado = "Juan Perez";
        assertEquals(nombreEsperado, pedido.getNombreCliente(), "El nombre del cliente no coincide con el valor esperado.");
    }

    @Test
    void testGenerarTextoFactura() {
        pedido.agregarProducto(producto1);
        pedido.agregarProducto(producto2);
        String textoFactura = pedido.generarTextoFactura();
        assertTrue(textoFactura.contains("Cliente: Juan Perez"), "La factura debe contener el nombre del cliente.");
        assertTrue(textoFactura.contains("Dirección: Calle 123"), "La factura debe contener la dirección del cliente.");
        assertTrue(textoFactura.contains("Hamburguesa"), "La factura debe contener el nombre del primer producto.");
        assertTrue(textoFactura.contains("Papas"), "La factura debe contener el nombre del segundo producto.");
        assertTrue(textoFactura.contains("Precio Total"), "La factura debe contener el precio total.");
    }

    @Test
    void testGuardarFactura() throws IOException {
        pedido.agregarProducto(producto1);
        pedido.agregarProducto(producto2);

        // Crear un archivo temporal para la factura
        File archivoFactura = File.createTempFile("facturaTest", ".txt");
        archivoFactura.deleteOnExit();

        // Guardar la factura en el archivo
        pedido.guardarFactura(archivoFactura);

        // Leer el contenido del archivo directamente como String
        String contenidoArchivo = Files.readString(archivoFactura.toPath());

        // Comparar el contenido del archivo con el texto esperado
        assertEquals(pedido.generarTextoFactura(), contenidoArchivo, "El contenido de la factura guardada no coincide con el texto generado.");
    }

    @Test
    void testGuardarFacturaPedidoVacio() throws IOException {
        File archivoFactura = File.createTempFile("facturaVacio", ".txt");
        archivoFactura.deleteOnExit();
        pedido.guardarFactura(archivoFactura);

        String contenidoArchivo = Files.readString(archivoFactura.toPath());
        assertEquals(pedido.generarTextoFactura(), contenidoArchivo, "El contenido de la factura guardada para un pedido vacío no coincide con el texto generado.");
    }

    @Test
    void testGuardarFacturaArchivoInvalido() throws IOException {
        // Crear un archivo temporal en modo solo lectura
        File archivoInvalido = File.createTempFile("facturaInvalida", ".txt");
        archivoInvalido.setReadOnly(); // Poner el archivo en modo de solo lectura
        archivoInvalido.deleteOnExit(); // Asegurar que el archivo se elimine después de la prueba

        // Intentar guardar la factura en el archivo de solo lectura
        assertThrows(FileNotFoundException.class, () -> {
            pedido.guardarFactura(archivoInvalido);
        }, "Debería lanzar FileNotFoundException cuando el archivo es de solo lectura.");
    }


    @Test
    void testPedidoVacio() {
        String textoFactura = pedido.generarTextoFactura();
        assertTrue(textoFactura.contains("Precio Total: 0"), "La factura de un pedido vacío debe tener precio total 0.");
    }

    @Test
    void testCalculoIVA() {
        pedido.agregarProducto(producto1);
        double precioNeto = producto1.getPrecio();
        int precioIVAEesperado = (int) (precioNeto * Pedido.getIVA());
        assertEquals(precioIVAEesperado, pedido.getPrecioTotalPedido() - (int) precioNeto, "El cálculo del IVA no coincide con el valor esperado.");
    }

    @Test
    void testGetIVANoNegativo() {
        double iva = Pedido.getIVA();
        assertTrue(iva >= 0, "El valor del IVA debería ser no negativo.");
    }
}


