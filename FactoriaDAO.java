package tds.dao;

/**
 * Factoria abstracta DAO.
 */

public abstract class FactoriaDAO {

	public static final String DAO_TDS = "tds.dao.TDSFactoriaDAO";

	private static FactoriaDAO unicaInstancia = null;

	/**
	 * Crea un tipo de factoria DAO. Solo existe el tipo TDSFactoriaDAO
	 * 
	 * @Override
	 */
	@SuppressWarnings("deprecation")
	public static FactoriaDAO getInstancia(String tipo) throws DAOException {
		if (unicaInstancia == null)
			try {
				unicaInstancia = (FactoriaDAO) Class.forName(tipo).newInstance();
			} catch (Exception e) {
				throw new DAOException(e.getMessage());
			}
		return unicaInstancia;
	}

	public static FactoriaDAO getInstancia() throws DAOException {
		return getInstancia(FactoriaDAO.DAO_TDS);
	}

	protected FactoriaDAO() {
	}

	// Metodos factoria para obtener adaptadores

	public abstract UsuarioDAO getUsuarioDAO();

	public abstract CancionDAO getCancionDAO();

	public abstract ListaDAO getListaDAO();
}
