package tds.controlador;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import tds.modelo.Usuario;

public class ControladorVistaModeloTest {

	private Usuario usuario;

	@Before
	public void init() {
		usuario = new Usuario("Alberto", "Sanchez Soto", "16-11-1999", "alberto_sanchez99@hotmail.com", "albeeerT06",
				"ey");
	}

	@Test
	public void testHacerPremium() {
		boolean premium = usuario.isPremium();
		ControladorVistaModelo.getUnicaInstancia().setUsuarioActual(usuario);
		ControladorVistaModelo.getUnicaInstancia().hacerPremium();
		assertFalse(usuario.isPremium().equals(premium));

	}

	@Test
	public void testCambiarEmail() {
		String antiguo = usuario.getEmail();
		ControladorVistaModelo.getUnicaInstancia().setUsuarioActual(usuario);
		ControladorVistaModelo.getUnicaInstancia().cambiarEmail("alberto.sanchezs@um.es");
		assertFalse(antiguo.equals(usuario.getEmail()));
	}

	@Test
	public void testCambiarContra() {
		String contra = usuario.getContrasena();
		ControladorVistaModelo.getUnicaInstancia().setUsuarioActual(usuario);
		ControladorVistaModelo.getUnicaInstancia().cambiarContra("eey");
		assertFalse(contra.equals(usuario.getContrasena()));
	}

}