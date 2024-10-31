package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;



public class ComboTest {
	
	
	private Combo combo;
	
	private ArrayList<ProductoMenu>itemsCombo;
	
	@BeforeEach
	
	void setUp()throws Exception {
		
		itemsCombo = new ArrayList <>(); 
			
			itemsCombo.add(new ProductoMenu("Hamburguesa",5000));
			itemsCombo.add(new ProductoMenu("Papas",2000));
			itemsCombo.add(new ProductoMenu("Bebida",1500));
			combo = new Combo("Combo Especial",0.9,itemsCombo);// Descuento del 10%
	
		}
		
		@Test
		void testGetNombre(){
			
			assertEquals("Combo Especial",combo.getNombre(), "El nombre del combo no es lo esperado");
			
		}
		
		@Test
		void testGetPrecio() {
			
			int precioEsperado = (int) ((5000+200+1500)*0.9); //Aplicar el descuento 
			assertEquals(precioEsperado,combo.getPrecio(),"el precio no es el esperado");
		
		}
		
		@Test
		void testGenerarTextoFactura() {
			
			String textoEsperado =  "Combo Combo Especial\n" +
									"Descuento: 0.9\n" +
									"            " + combo.getPrecio()+"\n";
			
			
			assertEquals(textoEsperado, combo.generarTextoFactura(),"El texto de la factura no es el esperado");
				
		}
	
}

	