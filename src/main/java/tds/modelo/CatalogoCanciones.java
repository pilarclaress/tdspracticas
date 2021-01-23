package tds.modelo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

import tds.dao.DAOException;
import tds.dao.FactoriaDAO;

public class CatalogoCanciones {

	private static CatalogoCanciones unicaInstancia;
	private HashMap<String, Vector<Cancion>> interpreteCancion;
	private HashMap<String, Vector<Cancion>> tituloCancion;
	private HashMap<String, Vector<Cancion>> estiloCancion;
	private HashMap<Integer, Cancion> idCancion;
	private List<ListaCanciones> listasUsuario;
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
					Vector<Cancion> canciones = interpreteCancion.get(interp.toLowerCase());
					if (canciones == null)
						canciones = new Vector<Cancion>();
					canciones.add(cancion);
					interpreteCancion.put(interp.toLowerCase(), canciones);
				}

				Vector<Cancion> canciones = tituloCancion.get(cancion.getTitulo().toLowerCase());
				if (canciones == null)
					canciones = new Vector<Cancion>();
				canciones.add(cancion);
				tituloCancion.put(cancion.getTitulo().toLowerCase(), canciones);

				canciones = estiloCancion.get(cancion.getEstilo());
				if (canciones == null)
					canciones = new Vector<Cancion>();
				canciones.add(cancion);
				estiloCancion.put(cancion.getEstilo(), canciones);

				// Añadir canciones más escuchadas
				if (masEscuchadas.size() < 10) {
					masEscuchadas.add(cancion);
					masEscuchadas.sort((c1, c2) -> c1.getNumReproducciones().compareTo(c2.getNumReproducciones()));
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

			listasUsuario = factoria.getListaDAO().getListasUsuario(usuarioActual.getId());

		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	public List<Cancion> buscarCancion(String interprete, String titulo, String estilo) {
		List<Cancion> devuelve = new LinkedList<Cancion>();
		int contador = 0;

		if (interprete == null && titulo == null && estilo == null) {
			devuelve.addAll(idCancion.values());
			return devuelve;
		}
		if (interprete != null && interpreteCancion.get(interprete.toLowerCase()) != null) {
			devuelve.addAll(interpreteCancion.get(interprete.toLowerCase()));
			contador = 1;
		}
		// Si no coincide en el interprete se devuelve la lista vacía
		else if (interprete != null)
			return devuelve;

		if (titulo != null && tituloCancion.get(titulo.toLowerCase()) != null) {
			if (contador != 0) {
				devuelve = devuelve.stream().filter(c -> c.getTitulo().toLowerCase().equals(titulo.toLowerCase()))
						.collect(Collectors.toList());
			} else {
				devuelve.addAll(tituloCancion.get(titulo.toLowerCase()));
				contador = 1;
			}
		}
		// Si no coincide en el titulo se devuelve la lista vacía
		else if (titulo != null) {
			return new LinkedList<Cancion>();
		}

		if (estilo != null && estiloCancion.get(estilo) != null) {
			if (contador != 0) {
				devuelve = devuelve.stream().filter(c -> c.getEstilo().equals(estilo)).collect(Collectors.toList());
			} else {
				devuelve.addAll(estiloCancion.get(estilo));
			}
		}
		// Si no coincide en el estilo se devuelve la lista vacía
		else if (estilo != null)
			new LinkedList<Cancion>();

		return devuelve;
	}
	
	public List<Cancion> busquedaIntensiva(String interprete, String titulo, String estilo) {
		List<Cancion> devuelve = new LinkedList<Cancion>();

		if (interprete == null && titulo == null && estilo == null) {
			devuelve.addAll(idCancion.values());
			return devuelve;
		}
		for (Cancion c : idCancion.values()) {
			boolean añadido = false;

			// Si el titulo coincide lo añade a la lista y marca como añadido
			// Si el titulo no coincide se pasa a la siguiente canción
			if (titulo != null && c.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
				devuelve.add(c);
				añadido = true;
			} else if (titulo != null) {
				continue;
			}

			// Si el estilo coincide y no se ha añadido ya la canción, se añade
			// Si no coincide el estilo y ya está añadida la canción, se borra
			// Si no coincide el estilo y no está añadida se pasa a la siguiente canción
			if (estilo != null && c.getEstilo().equals(estilo)) {
				if (!añadido)
					devuelve.add(c);
			} else if (estilo != null && añadido) {
				devuelve.remove(c);
			} else if (estilo != null) {
				continue;
			}

			// Comprueba si alguno de los interpretes coincide y no está añadido se añade
			// Si ya está añadido se marca, y si ninguno ha coincidido
			// Se borra de la lista
			if (interprete != null) {
				boolean interp = false;
				for (String i : c.getInterpretes()) {
					if (i.toLowerCase().contains(interprete.toLowerCase())) {
						if (!añadido) {
							devuelve.add(c);
							interp = true;
							break;
						}
						interp = true;
					}
				}
				if (!interp) {
					devuelve.remove(c);
				}
			}
		}
		return devuelve;
	}

	public Optional<Cancion> getCancion(String titulo, String interprete) {
		Vector<Cancion> aux = tituloCancion.get(titulo.toLowerCase());
		if (aux != null) {
			return aux.stream().filter(c -> c.interpretesToString().toLowerCase().equals(interprete.toLowerCase()))
					.findFirst();
		}
		return Optional.empty();
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

	public List<ListaCanciones> obtenerListasUsuario(Usuario u) {
		return listasUsuario;
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
		for (Cancion c : masEscuchadas) {
			lista.addCancion(c);
		}
		return lista;
	}

	// Actualizar la lista de las más escuchadas
	public void reproducirCancion(Cancion cancion) {
		if (masEscuchadas.getLast().getNumReproducciones() < cancion.getNumReproducciones()) {
			masEscuchadas.removeLast();
			masEscuchadas.addLast(cancion);
		}
	}

	// Si existe una canción con el mismo título, interprétes y estilo devuelve
	// true, en caso contrario devuelve false
	public boolean comprobarNuevaCancion(Cancion cancion) {
		Vector<Cancion> v = tituloCancion.get(cancion.getTitulo().toLowerCase());
		if (v != null) {
			for (Cancion c : v) {
				if (c.interpretesToString().toLowerCase().equals(cancion.interpretesToString().toLowerCase())
						&& c.getEstilo().equals(cancion.getEstilo())) {
					return true;
				}
			}
		}
		return false;
	}
}