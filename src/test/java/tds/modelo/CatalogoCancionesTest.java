package tds.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class CatalogoCancionesTest {

	private Usuario user;
	private ListaCanciones lista;
	private Cancion prueba;

	@Before
	public void init() {
		user = new Usuario("Pilar", "Clares", "07/02/2000", "pcc@gmail.es", "pilarcc", "tds");
		lista = new ListaCanciones("L1", user);
		prueba = new Cancion("Purpurina", "Reggaeton", "www.youtube.com", "Alberto Gambino");

		CatalogoCanciones.getUnicaInstancia().crearLista(lista);
	}

	@Test
	public void testGetCancion() {
		CatalogoCanciones.getUnicaInstancia().addCancion(prueba);
		Optional<Cancion> cancion = CatalogoCanciones.getUnicaInstancia().getCancion(prueba.getTitulo(),
				prueba.getInterpretes()[0]);
		assert (cancion.isPresent());
		assertEquals(cancion.get(), prueba);
	}

	@Test
	public void testComprobarNuevaLista() {
		boolean comp = CatalogoCanciones.getUnicaInstancia().comprobarNuevaLista(lista.getNombre(), user);
		assertFalse(comp);

	}

	@Test
	public void testObtenerListaCanciones() {
		ListaCanciones devuelve = CatalogoCanciones.getUnicaInstancia().obtenerListaCanciones(lista.getNombre(), user);
		assertEquals(lista, devuelve);
	}

}
