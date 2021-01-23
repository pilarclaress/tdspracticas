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

/**
 * 
 * Clase que implementa el Adaptador DAO concreto de Cancion para el tipo H2.
 * 
 */
public final class TDSCancionDAO implements CancionDAO {

	private static final String CANCION = "Cancion";
	private static final String TITULO = "titulo";
	private static final String INTERPRETES = "interpretes";
	private static final String ESTILO = "estilo";
	private static final String RUTA = "rutaFichero";
	private static final String NUMREP = "numReproducciones";

	private ServicioPersistencia servPersistencia;

	public TDSCancionDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	private Cancion entidadToCancion(Entidad eCancion) {
		String titulo = servPersistencia.recuperarPropiedadEntidad(eCancion, TITULO);
		String interp = servPersistencia.recuperarPropiedadEntidad(eCancion, INTERPRETES);
		String estilo = servPersistencia.recuperarPropiedadEntidad(eCancion, ESTILO);
		String rutaFichero = servPersistencia.recuperarPropiedadEntidad(eCancion, RUTA);
		String numRep = servPersistencia.recuperarPropiedadEntidad(eCancion, NUMREP);
		String[] interpretes = interp.split("&");

		Cancion cancion = new Cancion(titulo, estilo, rutaFichero, interpretes);
		for (int i = 0; i < Integer.parseInt(numRep); i++)
			cancion.addReproduccion();
		cancion.setId(eCancion.getId());

		return cancion;
	}

	private Entidad cancionToEntidad(Cancion cancion) {
		Entidad eCancion = new Entidad();
		eCancion.setNombre(CANCION);

		String interp = cancion.getInterpretes()[0];
		int i = 1;
		while (i < cancion.getInterpretes().length) {
			interp = interp + "&" + cancion.getInterpretes()[i];
			i++;
		}
		eCancion.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad(TITULO, cancion.getTitulo()), new Propiedad(ESTILO, cancion.getEstilo()),
						new Propiedad(INTERPRETES, interp), new Propiedad(RUTA, cancion.getRutaFichero()),
						new Propiedad(NUMREP, ((Integer) cancion.getNumReproducciones()).toString()))));
		return eCancion;
	}

	public void create(Cancion cancion) {
		Entidad eCancion = cancionToEntidad(cancion);
		eCancion = servPersistencia.registrarEntidad(eCancion);
		cancion.setId(eCancion.getId());
	}

	public boolean delete(Cancion cancion) {
		Entidad eCancion = servPersistencia.recuperarEntidad(cancion.getId());
		if (eCancion != null)
			return servPersistencia.borrarEntidad(eCancion);
		return true;
	}

	public void update(Cancion cancion) {
		Entidad eCancion = servPersistencia.recuperarEntidad(cancion.getId());
		if (eCancion != null) {
			servPersistencia.eliminarPropiedadEntidad(eCancion, NUMREP);
			servPersistencia.anadirPropiedadEntidad(eCancion, NUMREP,
					((Integer) cancion.getNumReproducciones()).toString());
		}
	}

	public Cancion get(int id) {
		Entidad eCancion = servPersistencia.recuperarEntidad(id);
		if (eCancion != null)
			return entidadToCancion(eCancion);
		return null;
	}

	public List<Cancion> getAll() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades(CANCION);

		List<Cancion> canciones = new LinkedList<Cancion>();
		for (Entidad eCancion : entidades) {
			Cancion c = get(eCancion.getId());
			if (c != null)
				canciones.add(c);
		}

		return canciones;
	}

}