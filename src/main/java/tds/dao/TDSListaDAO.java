package tds.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import tds.modelo.Cancion;
import tds.modelo.ListaCanciones;
import tds.modelo.Usuario;

public class TDSListaDAO implements ListaDAO {

	private static final String LISTA = "ListaCanciones";

	private static final String USUARIO = "usuario";
	private static final String NOMBRE = "nombre";
	private static final String CANCIONES = "Canciones";

	private ServicioPersistencia servPersistencia;

	public TDSListaDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	private ListaCanciones entidadToLista(Entidad eLista) {
		String idUsuario = servPersistencia.recuperarPropiedadEntidad(eLista, USUARIO);
		String nombre = servPersistencia.recuperarPropiedadEntidad(eLista, NOMBRE);
		String idCanciones = servPersistencia.recuperarPropiedadEntidad(eLista, CANCIONES);

		UsuarioDAO usuarioDAO = null;
		try {
			usuarioDAO = FactoriaDAO.getInstancia().getUsuarioDAO();
		} catch (DAOException e1) {
			e1.printStackTrace();
		}
		Usuario usuario = usuarioDAO.get(Integer.parseInt(idUsuario));
		ListaCanciones lista = new ListaCanciones(nombre, usuario);

		CancionDAO cancionDAO = null;
		try {
			cancionDAO = FactoriaDAO.getInstancia().getCancionDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (idCanciones != null && !idCanciones.equals("")) {
			for (String id : idCanciones.split(" ")) {
				int aid = Integer.parseInt(id);
				Cancion c = cancionDAO.get(aid);
				lista.addCancion(c);
			}
		}

		lista.setId(eLista.getId());
		return lista;
	}

	private Entidad listaToEntidad(ListaCanciones lista) {
		Entidad eLista = new Entidad();
		eLista.setNombre(LISTA);

		eLista.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad(NOMBRE, lista.getNombre()), new Propiedad(USUARIO, lista.getIdUsuario()),
						new Propiedad(CANCIONES, lista.getInformacionCanciones()))));
		return eLista;
	}

	public void create(ListaCanciones lista) {
		Entidad eLista = listaToEntidad(lista);
		eLista = servPersistencia.registrarEntidad(eLista);
		lista.setId(eLista.getId());
	}

	public boolean delete(ListaCanciones lista) {
		Entidad eLista = servPersistencia.recuperarEntidad(lista.getId());
		return servPersistencia.borrarEntidad(eLista);
	}

	/**
	 * Permite actualizar las canciones de una lista de canciones
	 */
	public void updateLista(ListaCanciones lista) {
		Entidad eLista = servPersistencia.recuperarEntidad(lista.getId());
		System.out.println(lista.getId() + "  " + lista.getNombre());
		if (eLista != null) {
			System.out.println("No es null");
			for (Propiedad p : eLista.getPropiedades()) {
				if (p.getNombre().equals(CANCIONES)) {

					System.out.println(lista.getInformacionCanciones());

					p.setValor(lista.getInformacionCanciones());
				}
				servPersistencia.modificarPropiedad(p);
			}
		}
	}

	public ListaCanciones get(int id) {
		Entidad eLista = servPersistencia.recuperarEntidad(id);
		return entidadToLista(eLista);
	}

	public List<ListaCanciones> getAll() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades(LISTA);

		List<ListaCanciones> listas = new LinkedList<ListaCanciones>();
		for (Entidad eLista : entidades) {
			listas.add(get(eLista.getId()));
		}

		return listas;
	}

	public List<ListaCanciones> getListasUsuario(int id) {
		return getAll().stream().filter(l -> Integer.parseInt(l.getIdUsuario()) == id).collect(Collectors.toList());
	}

}
