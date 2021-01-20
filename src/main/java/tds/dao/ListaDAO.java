package tds.dao;

import java.util.List;

import tds.modelo.ListaCanciones;

public interface ListaDAO {
	void create(ListaCanciones lista);

	boolean delete(ListaCanciones lista);

	void updateLista(ListaCanciones lista);

	ListaCanciones get(int id);

	List<ListaCanciones> getAll();
}
