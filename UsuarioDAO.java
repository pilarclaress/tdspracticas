package tds.dao;

import java.util.List;

import tds.modelo.Usuario;

public interface UsuarioDAO {

	void create(Usuario asistente);

	boolean delete(Usuario asistente);

	void updatePerfil(Usuario asistente);

	Usuario get(int id);

	List<Usuario> getAll();

}
