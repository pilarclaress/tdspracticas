package tds.modelo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import tds.dao.DAOException;
import tds.dao.FactoriaDAO;

public class CatalogoUsuarios {

	private static CatalogoUsuarios unicaInstancia;
	private FactoriaDAO factoria;
	private HashMap<Integer, Usuario> asistentesPorID;
	private HashMap<String, Usuario> asistentesPorLogin;
	private HashMap<String, Usuario> asistentesPorEmail;

	public static CatalogoUsuarios getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new CatalogoUsuarios();
		return unicaInstancia;
	}

	private CatalogoUsuarios() {
		asistentesPorID = new HashMap<Integer, Usuario>();
		asistentesPorLogin = new HashMap<String, Usuario>();
		asistentesPorEmail = new HashMap<String, Usuario>();

		try {
			factoria = FactoriaDAO.getInstancia();

			List<Usuario> listaAsistentes = factoria.getUsuarioDAO().getAll();
			for (Usuario usuario : listaAsistentes) {
				asistentesPorID.put(usuario.getId(), usuario);
				asistentesPorLogin.put(usuario.getUsuario(), usuario);
				asistentesPorEmail.put(usuario.getEmail(), usuario);
			}
		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}

	public Usuario comprobarUsuarioClave(String u, String clave) {
		Usuario user = asistentesPorLogin.get(u);

		if (user != null && user.comprobacion(clave))
			return user;
		return null;
	}

	public List<Usuario> getUsuarios() throws DAOException {
		return new LinkedList<Usuario>(asistentesPorLogin.values());
	}

	public Usuario getUsuarioPorLogin(String login) {
		return asistentesPorLogin.get(login);
	}

	public Usuario getUsuario(int id) {
		return asistentesPorID.get(id);
	}

	public Usuario getUsuarioPorEmail(String email) {
		return asistentesPorEmail.get(email);
	}

	public boolean esUsuarioRegistrado(String u) {
		return asistentesPorLogin.containsKey(u);
	}

	public boolean esEmailRegistrado(String em) {
		return asistentesPorEmail.containsKey(em);
	}

	public void generarUsuario(Usuario usuario) {
		asistentesPorID.put(usuario.getId(), usuario);
		asistentesPorLogin.put(usuario.getUsuario(), usuario);
		asistentesPorEmail.put(usuario.getEmail(), usuario);
	}

	public void eliminarUsuario(Usuario usuario) {
		asistentesPorID.remove(usuario.getId());
		asistentesPorLogin.remove(usuario.getUsuario());
		asistentesPorEmail.remove(usuario.getEmail());
	}

}