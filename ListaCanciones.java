package tds.modelo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ListaCanciones {

	private int id;
	private String nombre;
	private Usuario usuario;
	private List<Cancion> canciones;

	public ListaCanciones(String nombre, Usuario usuario) {
		this.nombre = nombre;
		this.usuario = usuario;
		canciones = new LinkedList<Cancion>();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getIdUsuario() {
		return Integer.toString(usuario.getId());
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Cancion> getCanciones() {
		return canciones;
	}

	public void setCanciones(List<Cancion> c) {
		canciones = c;
	}

	public void addCancion(Cancion cancion) {
		this.canciones.add(cancion);
	}

	public void addAllCanciones(Object[]... canc) {
		for (Object c : canc) {
			if (c instanceof Cancion)
				addCancion((Cancion) c);
		}
	}

	public void removeCancion(Cancion cancion) {
		Iterator<Cancion> cancionIterator = canciones.iterator();
		while (cancionIterator.hasNext()) {
			Cancion c = cancionIterator.next();
			if (c.equals(cancion)) {
				cancionIterator.remove();
			}
		}
	}

	public String getInformacionCanciones() {
		String ids = "";
		for (Cancion cancion : canciones) {
			ids += cancion.getId() + " ";
		}
		return ids;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
