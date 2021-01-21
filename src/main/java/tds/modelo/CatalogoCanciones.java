package tds.modelo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import tds.dao.DAOException;
import tds.dao.FactoriaDAO;

public class CatalogoCanciones {

	private static CatalogoCanciones unicaInstancia;
	private HashMap<String, Vector<Cancion>> interpreteCancion;
	private HashMap<String, Vector<Cancion>> tituloCancion;
	private HashMap<String, Vector<Cancion>> estiloCancion;
	private HashMap<Integer, Cancion> idCancion;
	private HashMap<String, Vector<ListaCanciones>> listasPorUsuario;

	private FactoriaDAO factoria;

	public static CatalogoCanciones getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new CatalogoCanciones();
		}
		return unicaInstancia;
	}

	private CatalogoCanciones() {
		interpreteCancion = new HashMap<String, Vector<Cancion>>();
		tituloCancion = new HashMap<String, Vector<Cancion>>();
		estiloCancion = new HashMap<String, Vector<Cancion>>();
		idCancion = new HashMap<Integer, Cancion>();
		listasPorUsuario = new HashMap<String, Vector<ListaCanciones>>();

		try {
			factoria = FactoriaDAO.getInstancia();

			List<Cancion> listaAsistentes = factoria.getCancionDAO().getAll();
			for (Cancion cancion : listaAsistentes) {
				idCancion.put(cancion.getId(), cancion);
				for (String interp : cancion.getInterpretes()) {
					Vector<Cancion> canciones = interpreteCancion.get(interp);
					if (canciones == null)
						canciones = new Vector<Cancion>();
					canciones.add(cancion);
					interpreteCancion.put(interp, canciones);
				}

				Vector<Cancion> canciones = tituloCancion.get(cancion.getTitulo());
				if (canciones == null)
					canciones = new Vector<Cancion>();
				canciones.add(cancion);
				tituloCancion.put(cancion.getTitulo(), canciones);

				canciones = estiloCancion.get(cancion.getEstilo());
				if (canciones == null)
					canciones = new Vector<Cancion>();
				canciones.add(cancion);
				estiloCancion.put(cancion.getEstilo(), canciones);
			}

			List<ListaCanciones> listas = factoria.getListaDAO().getAll();
			for (ListaCanciones l : listas) {
				Vector<ListaCanciones> v = listasPorUsuario.get(l.getUsuario().getUsuario());
				if (v == null)
					v = new Vector<ListaCanciones>();
				v.add(l);
				listasPorUsuario.put(l.getUsuario().getUsuario(), v);
			}

		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}

	}

	public List<Cancion> buscarCancion(String interprete, String titulo, String estilo) {
		List<Cancion> devuelve = new LinkedList<Cancion>();
		if (interprete != null && interpreteCancion.get(interprete) != null)
			devuelve.addAll(interpreteCancion.get(interprete));
		if (titulo != null && tituloCancion.get(titulo) != null)
			devuelve.addAll(tituloCancion.get(titulo));
		if (estilo != null && estiloCancion.get(estilo) != null)
			devuelve.addAll(estiloCancion.get(estilo));

		if(interprete==null && titulo==null && estilo==null) {
			devuelve.addAll(idCancion.values());
		}
			
		return devuelve;
	}

	public Optional<Cancion> getCancion(String titulo, String interprete) {
		Vector<Cancion> aux = tituloCancion.get(titulo);
		if (aux != null) {
			return aux.stream().filter(c -> c.interpretesToString().equals(interprete)).findFirst();
		}
		return null;
	}

	public void addCancion(Cancion cancion) {
		for (String interp : cancion.getInterpretes()) {
			Vector<Cancion> canciones = interpreteCancion.get(interp);
			if (canciones == null)
				canciones = new Vector<Cancion>();
			canciones.add(cancion);
			interpreteCancion.put(interp, canciones);
		}

		Vector<Cancion> canciones = tituloCancion.get(cancion.getTitulo());
		if (canciones == null)
			canciones = new Vector<Cancion>();
		canciones.add(cancion);
		tituloCancion.put(cancion.getTitulo(), canciones);

		canciones = estiloCancion.get(cancion.getEstilo());
		if (canciones == null)
			canciones = new Vector<Cancion>();
		canciones.add(cancion);
		estiloCancion.put(cancion.getEstilo(), canciones);

		idCancion.put(cancion.getId(), cancion);
	}

	public void removeCancion(Cancion cancion) {
		Vector<Cancion> v = tituloCancion.get(cancion.getTitulo());
		v.remove(cancion);
		tituloCancion.put(cancion.getTitulo(), v);

		v = estiloCancion.get(cancion.getEstilo());
		v.remove(cancion);
		estiloCancion.put(cancion.getEstilo(), v);

		idCancion.remove(cancion.getId());

		for (String interp : cancion.getInterpretes()) {
			v = interpreteCancion.get(interp);
			v.remove(cancion);
			interpreteCancion.put(interp, v);
		}
	}

	public boolean comprobarNuevaLista(String l, Usuario usuario) {
		Vector<ListaCanciones> v = listasPorUsuario.get(usuario.getUsuario());
		if (v != null) {
			for (ListaCanciones lista : v) {
				if (lista.getNombre().equals(l))
					return false;
			}
		}
		return true;
	}

	public boolean crearLista(ListaCanciones l) {
		Vector<ListaCanciones> v = listasPorUsuario.get(l.getUsuario().getUsuario());
		if (v == null)
			v = new Vector<ListaCanciones>();
		v.add(l);
		listasPorUsuario.put(l.getUsuario().getUsuario(), v);
		return true;
	}

	public ListaCanciones eliminarLista(String lista, Usuario usuario) {
		Vector<ListaCanciones> v = listasPorUsuario.get(usuario.getUsuario());
		if (v != null) {
			for (ListaCanciones l : v) {
				if (lista.equals(l.getNombre())) {
					v.remove(l);
					listasPorUsuario.put(usuario.getUsuario(), v);
					return l;
				}
			}
		}

		return null;
	}

	public List<String> obtenerListasUsuario(Usuario u) {
		List<String> lista = new LinkedList<String>();
		if (listasPorUsuario.containsKey(u.getUsuario())) {
			for (ListaCanciones l : listasPorUsuario.get(u.getUsuario())) {
				lista.add(l.getNombre());
			}
		}
		return lista;
	}

	public void anadirCancionLista(Cancion c, String lista, Usuario usuario) {
		Vector<ListaCanciones> v = listasPorUsuario.get(usuario.getUsuario());
		if (v != null) {
			for (ListaCanciones l : v) {
				if (lista.equals(l.getNombre())) {
					v.remove(l);
					l.addCancion(c);
					v.add(l);
				}
			}
		}

		listasPorUsuario.put(usuario.getUsuario(), v);
	}

	public ListaCanciones obtenerListaCanciones(String lista, Usuario usuario) {
		Vector<ListaCanciones> v = listasPorUsuario.get(usuario.getUsuario());
		if (v != null) {
			for (ListaCanciones l : v) {
				if (lista.equals(l.getNombre())) {
					return l;
				}
			}
		}
		return null;
	}

	public String[] getEstilos() {
		return estiloCancion.keySet().toArray(new String[estiloCancion.size()]);
	}

	public ListaCanciones getMasEscuchadas() {
		ListaCanciones lista = new ListaCanciones("Más escuchadas", null);
		idCancion.values().stream().sorted((c1, c2) -> c1.getNumReproducciones().compareTo(c2.getNumReproducciones()))
				.limit(10).forEach(c -> lista.addCancion(c));

		return lista;
	}
}
