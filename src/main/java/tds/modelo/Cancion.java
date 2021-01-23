package tds.modelo;

public class Cancion {

	private int id;

	private String titulo;
	private String[] interpretes;
	private String estilo;
	private String rutaFichero;
	private int numReproducciones;

	public Cancion(String titulo, String estilo, String rutaFichero, String... interpretes) {
		this.titulo = titulo;
		this.interpretes = interpretes;
		this.estilo = estilo;
		this.rutaFichero = rutaFichero;
		numReproducciones = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String[] getInterpretes() {
		return interpretes;
	}

	public void setInterpretes(String[] interpretes) {
		this.interpretes = interpretes;
	}

	public String getEstilo() {
		return estilo;
	}

	public void setEstilo(String estilo) {
		this.estilo = estilo;
	}

	public String getRutaFichero() {
		return rutaFichero;
	}

	public void setRutaFichero(String rutaFichero) {
		this.rutaFichero = rutaFichero;
	}

	public Integer getNumReproducciones() {
		return numReproducciones;
	}

	public void addReproduccion() {
		numReproducciones++;
	}

	public String getNombreFichero() {
		return interpretesToString() + "-" + titulo + ".mp3";
	}

	public String interpretesToString() {
		String sinterpretes;
		sinterpretes = interpretes[0];
		for (int i = 1; i < interpretes.length; i++) {
			sinterpretes += "&";
			sinterpretes += interpretes[i];
		}
		return sinterpretes;
	}

}