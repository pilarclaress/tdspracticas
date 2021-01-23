package tds.modelo;

import java.util.HashMap;
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
	private LinkedList<ListaCanciones> listasUsuario;
	private LinkedList<Cancion> masEscuchadas;

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
		listasUsuario = new LinkedList<ListaCanciones>();
		masEscuchadas = new LinkedList<Cancion>();

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

				// Añadir canciones más escuchadas
				if (masEscuchadas.size() < 10) {
					masEscuchadas.add(cancion);
				} else if (cancion.getNumReproducciones() > masEscuchadas.getLast().getNumReproducciones()) {
					masEscuchadas.removeLast();
					masEscuchadas.add(cancion);
					masEscuchadas.sort((c1, c2) -> c1.getNumReproducciones().compareTo(c2.getNumReproducciones()));
				}
			}

		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}

	}

	public void establecerUsuario(Usuario usuarioActual) {
		try {
			factoria = FactoriaDAO.getInstancia();

			listasUsuario = (LinkedList<ListaCanciones>) factoria.getListaDAO().getListasUsuario(usuarioActual.getId());

		} catch (DAOException e) {
			e.printStackTrace();
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

		if (interprete == null && titulo == null && estilo == null) {
			devuelve.addAll(idCancion.values());
		}

		return devuelve;
	}

	public List<Cancion> busquedaIntensiva(String interprete, String titulo, String estilo) {
		List<Cancion> devuelve = new LinkedList<Cancion>();
		for (Cancion c : idCancion.values()) {
			if (titulo != null && c.getTitulo().contains(titulo)) {
				devuelve.add(c);
			} else if (estilo != null && c.getEstilo().contains(estilo)) {
				devuelve.add(c);
			} else if (interprete != null) {
				for (String i : c.getInterpretes())
					if (i.contains(interprete)) {
						devuelve.add(c);
						break;
					}
			}
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
		for (ListaCanciones lista : listasUsuario) {
			if (lista.getNombre().equals(l))
				return false;
		}
		return true;
	}

	public boolean crearLista(ListaCanciones l) {
		listasUsuario.add(l);
		return true;
	}

	public ListaCanciones eliminarLista(String lista, Usuario usuario) {
		for (ListaCanciones l : listasUsuario) {
				if (lista.equals(l.getNombre())) {
					listasUsuario.remove(l);
					return l;
				}
			}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String> obtenerListasUsuario(Usuario u) {
		return (List<String>) listasUsuario.clone();
	}

	public void anadirCancionesLista(List<Cancion> canciones, ListaCanciones lista, Usuario usuario) {
			listasUsuario.stream().filter(l -> l.equals(lista)).forEach(l -> l.setCanciones(canciones));
	}

	public ListaCanciones obtenerListaCanciones(String lista, Usuario usuario) {
		for (ListaCanciones l : listasUsuario) {
				if (lista.equals(l.getNombre())) {
					return l;
				}
			}
		return null;
	}

	public String[] getEstilos() {
		return estiloCancion.keySet().toArray(new String[estiloCancion.size()]);
	}

	public ListaCanciones getMasEscuchadas() {
		// No se pone un usuario concreto porque son las más reproducidas de todo el
		// sistema
		ListaCanciones lista = new ListaCanciones("Más Escuchadas", new Usuario(null, null, null, null, null, null));
		lista.addAllCanciones(masEscuchadas.toArray());
		return lista;
	}

	// Actualizar la lista de las más escuchadas
	public void reproducirCancion(Cancion cancion) {
		if (masEscuchadas.getLast().getNumReproducciones() < cancion.getNumReproducciones()) {
			masEscuchadas.removeLast();
			masEscuchadas.addLast(cancion);
		}
	}
}
