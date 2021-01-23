package tds.dao;

import java.util.List;

import tds.modelo.Cancion;

public interface CancionDAO {
	void create(Cancion asistente);

	boolean delete(Cancion asistente);

	void update(Cancion asistente);

	Cancion get(int id);

	List<Cancion> getAll();
}
