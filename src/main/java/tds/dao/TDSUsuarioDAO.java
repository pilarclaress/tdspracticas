package tds.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import tds.modelo.Cancion;
import tds.modelo.Usuario;

/**
 * 
 * Clase que implementa el Adaptador DAO concreto de Usuario para el tipo H2.
 * 
 */
public final class TDSUsuarioDAO implements UsuarioDAO {

	private static final String USUARIO = "Usuario";

	private static final String NOMBRE = "nombre";
	private static final String APELLIDOS = "apellidos";
	private static final String EMAIL = "email";
	private static final String LOGIN = "usuario";
	private static final String PASSWORD = "contrasena";
	private static final String FECHA_NACIMIENTO = "fechaNacimiento";
	private static final String PREMIUM = "premium";
	private static final String RECIENTES = "recientes";

	private ServicioPersistencia servPersistencia;

	public TDSUsuarioDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	private Usuario entidadToUsuario(Entidad eUsuario) {

		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, NOMBRE);
		String apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, APELLIDOS);
		String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, EMAIL);
		String login = servPersistencia.recuperarPropiedadEntidad(eUsuario, LOGIN);
		String password = servPersistencia.recuperarPropiedadEntidad(eUsuario, PASSWORD);
		String fechaNacimiento = servPersistencia.recuperarPropiedadEntidad(eUsuario, FECHA_NACIMIENTO);
		String premium = servPersistencia.recuperarPropiedadEntidad(eUsuario, PREMIUM);
		String recientes = servPersistencia.recuperarPropiedadEntidad(eUsuario, RECIENTES);
		Usuario usuario = new Usuario(nombre, apellidos, fechaNacimiento, email, login, password);

		CancionDAO cancionDAO = null;
		try {
			cancionDAO = FactoriaDAO.getInstancia().getCancionDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (recientes != null && !recientes.equals("")) {
			for (String id : recientes.split(" ")) {
				int aid = Integer.parseInt(id);
				Cancion c = cancionDAO.get(aid);
				usuario.addReciente(c);
			}
		}

		usuario.setPremium(Boolean.parseBoolean(premium));
		usuario.setId(eUsuario.getId());

		return usuario;
	}

	private Entidad usuarioToEntidad(Usuario usuario) {
		Entidad eUsuario = new Entidad();
		eUsuario.setNombre(USUARIO);

		eUsuario.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad(NOMBRE, usuario.getNombre()),
				new Propiedad(APELLIDOS, usuario.getApellidos()), new Propiedad(EMAIL, usuario.getEmail()),
				new Propiedad(LOGIN, usuario.getUsuario()), new Propiedad(PASSWORD, usuario.getContrasena()),
				new Propiedad(FECHA_NACIMIENTO, usuario.getFechaNacimiento()),
				new Propiedad(PREMIUM, usuario.isPremium().toString()),
				new Propiedad(RECIENTES, usuario.getRecientesId()))));
		return eUsuario;
	}

	public void create(Usuario usuario) {
		Entidad nUsuario = usuarioToEntidad(usuario);
		nUsuario = servPersistencia.registrarEntidad(nUsuario);
		usuario.setId(nUsuario.getId());
	}

	public boolean delete(Usuario usuario) {
		Entidad eUsuario;
		eUsuario = servPersistencia.recuperarEntidad(usuario.getId());
		if (eUsuario != null)
			return servPersistencia.borrarEntidad(eUsuario);
		return true;
	}

	/**
	 * Permite que un Usuario modifique su perfil: premium, password y email
	 */
	public void updatePerfil(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getId());
		if (eUsuario != null) {

			for (Propiedad p : eUsuario.getPropiedades()) {
				if (p.getNombre().equals(PASSWORD)) {
					p.setValor(usuario.getContrasena());
				}
				if (p.getNombre().equals(EMAIL)) {
					p.setValor(usuario.getEmail());
				}
				if (p.getNombre().equals(PREMIUM)) {
					p.setValor(usuario.isPremium().toString());
				}
				servPersistencia.modificarPropiedad(p);
			}

		}
	}

	public Usuario get(int id) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(id);
		if (eUsuario != null)
			return entidadToUsuario(eUsuario);
		return null;
	}

	public List<Usuario> getAll() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades(USUARIO);

		List<Usuario> usuarios = new LinkedList<Usuario>();
		for (Entidad eUsuario : entidades) {
			Usuario user = get(eUsuario.getId());
			if (user != null)
				usuarios.add(user);
		}

		return usuarios;
	}

}
