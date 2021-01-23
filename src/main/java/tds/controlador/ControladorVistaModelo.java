package tds.controlador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import tds.dao.CancionDAO;
import tds.dao.DAOException;
import tds.dao.FactoriaDAO;
import tds.dao.TDSCancionDAO;
import tds.dao.TDSListaDAO;
import tds.dao.UsuarioDAO;
import tds.modelo.Cancion;
import tds.modelo.CatalogoCanciones;
import tds.modelo.CatalogoUsuarios;
import tds.modelo.Descuento;
import tds.modelo.DescuentoEmail;
import tds.modelo.DescuentoFijo;
import tds.modelo.ListaCanciones;
import tds.modelo.Usuario;
import umu.tds.componente.Canciones;
import umu.tds.componente.CancionesEvent;
import umu.tds.componente.ICancionesListener;

public class ControladorVistaModelo {

	private static ControladorVistaModelo unicaInstancia;
	private Usuario usuarioActual;
	private ICancionesListener cancionesListener;
	private FactoriaDAO factoria;
	private ListaCanciones listaActual;
	private Cancion cancionActual;

	private MediaPlayer mediaPlayer;
	private String tempPath = null;
	private String binPath = null;

	private static final double PRECIOPREMIUM = 20;
	private LinkedList<Descuento> listaDes;

	private ControladorVistaModelo() {
		usuarioActual = null;
		listaActual = null;
		try {
			factoria = FactoriaDAO.getInstancia();
		} catch (DAOException e) {
			e.printStackTrace();
		}

		mediaPlayer = null;
		binPath = ControladorVistaModelo.class.getClassLoader().getResource(".").getPath();
		// quitar "/" añadida al inicio del path en plataforma Windows
		tempPath = binPath.replace("/bin", "/temp");

		listaDes = new LinkedList<Descuento>();
	}

	public static ControladorVistaModelo getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new ControladorVistaModelo();
		}
		return unicaInstancia;
	}

	public void setUsuarioActual(Usuario usuario) {
		this.usuarioActual = usuario;
		if (usuarioActual != null)
			CatalogoCanciones.getUnicaInstancia().establecerUsuario(usuarioActual);

		// Lista de descuentos
		LocalDate finicio = LocalDate.of(2020, 10, 1);
		LocalDate ffin = LocalDate.of(2022, 1, 1);
		listaDes.add(new DescuentoFijo(finicio, ffin));
		listaDes.add(new DescuentoEmail(usuarioActual.getEmail()));
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public Cancion getCancionActual() {
		return cancionActual;
	}

	public boolean esUsuarioRegistrado(String login) {
		return CatalogoUsuarios.getUnicaInstancia().esUsuarioRegistrado(login);
	}

	public boolean esEmailRegistrado(String email) {
		return CatalogoUsuarios.getUnicaInstancia().esEmailRegistrado(email);
	}

	public Usuario comprobacionUsuarioClave(String u, String clave) {
		return CatalogoUsuarios.getUnicaInstancia().comprobarUsuarioClave(u, clave);
	}

	public String getNombreUsuarioActual() {
		return usuarioActual.getNombre();
	}

	public boolean generarUsuario(String nombre, String apellidos, String fechaNacimiento, String email, String usuario,
			String contrasena) {
		Usuario user = new Usuario(nombre, apellidos, fechaNacimiento, email, usuario, contrasena);

		UsuarioDAO usuarioDAO = factoria.getUsuarioDAO(); /* Adaptador DAO para almacenar el nuevo Usuario en la BD */
		usuarioDAO.create(user);

		CatalogoUsuarios.getUnicaInstancia().generarUsuario(user);
		return true;
	}

	public boolean borrarUsuario(Usuario usuario) {
		if (!esUsuarioRegistrado(usuario.getUsuario()))
			return false;

		UsuarioDAO usuarioDAO = factoria.getUsuarioDAO(); /* Adaptador DAO para borrar el Usuario de la BD */
		usuarioDAO.delete(usuario);

		CatalogoUsuarios.getUnicaInstancia().eliminarUsuario(usuario);
		return true;
	}

	public List<Cancion> buscarCancion(String interprete, String titulo, String estilo) {
		List<Cancion> busq = CatalogoCanciones.getUnicaInstancia().buscarCancion(interprete, titulo, estilo);
		listaActual = new ListaCanciones("listaActual", usuarioActual);
		listaActual.addAllCanciones(busq.toArray());
		return busq;
	}

	public List<Cancion> buscarAvanzada(String interprete, String titulo, String estilo) {
		List<Cancion> busq = CatalogoCanciones.getUnicaInstancia().busquedaIntensiva(interprete, titulo, estilo);
		listaActual = new ListaCanciones("listaActual", usuarioActual);
		listaActual.addAllCanciones(busq.toArray());
		return busq;
	}

	public ListaCanciones getRecientes() {
		ListaCanciones lista = new ListaCanciones("Recientes", usuarioActual);
		for (Cancion c : usuarioActual.getRecientes()) {
			lista.addCancion(c);
		}
		return lista;
	}

	public boolean crearNuevaLista(String nLista) {
		ListaCanciones lista = new ListaCanciones(nLista, usuarioActual);
		factoria.getListaDAO().create(lista);
		return CatalogoCanciones.getUnicaInstancia().crearLista(lista);
	}

	public boolean comprobarNuevaLista(String nLista) {
		return CatalogoCanciones.getUnicaInstancia().comprobarNuevaLista(nLista, usuarioActual);
	}

	public void eliminarLista(String lista) {
		ListaCanciones l = CatalogoCanciones.getUnicaInstancia().eliminarLista(lista, usuarioActual);
		if (l != null)
			factoria.getListaDAO().delete(l);
	}

	public boolean cambiarEmail(String email) {
		if (email.equals(null))
			return false;
		if (this.esEmailRegistrado(email))
			return false;
		usuarioActual.setEmail(email);
		CatalogoUsuarios.getUnicaInstancia().eliminarUsuario(usuarioActual); // COn modificar el usuario vale no?
		CatalogoUsuarios.getUnicaInstancia().generarUsuario(usuarioActual);
		UsuarioDAO usuarioDAO = factoria.getUsuarioDAO(); /* Adaptador DAO para almacenar el nuevo Usuario en la BD */
		usuarioDAO.updatePerfil(usuarioActual);
		return true;
	}

	public boolean cambiarContra(String contra) {
		if (contra.equals(""))
			return false;
		usuarioActual.setContrasena(contra);
		UsuarioDAO usuarioDAO = factoria.getUsuarioDAO(); /* Adaptador DAO para almacenar el nuevo Usuario en la BD */
		usuarioDAO.updatePerfil(usuarioActual);
		return true;
	}

	public boolean hacerPremium() {
		double min = PRECIOPREMIUM;
		Descuento enviar = null;
		if (listaDes != null) {
			for (Descuento d : listaDes) {
				if (d.isAplicableDescuento()) {
					if (d.calcDescuento(PRECIOPREMIUM) < min) {
						min = d.calcDescuento(PRECIOPREMIUM);
						enviar = d;
					}
				}
			}
		}
		boolean realizado = usuarioActual.hacerPremium(enviar, PRECIOPREMIUM);
		if (realizado) {
			CatalogoUsuarios.getUnicaInstancia().eliminarUsuario(usuarioActual);
			CatalogoUsuarios.getUnicaInstancia().generarUsuario(usuarioActual);

			UsuarioDAO usuarioDAO = factoria
					.getUsuarioDAO(); /* Adaptador DAO para almacenar el nuevo Usuario en la BD */
			usuarioDAO.updatePerfil(usuarioActual);
			return true;
		}
		return false;
	}

	public ICancionesListener getCancionesListener() {
		if (cancionesListener == null) {
			cancionesListener = new ICancionesListener() {
				public void nuevasCanciones(EventObject e) {
					TDSCancionDAO cancionDAO = (TDSCancionDAO) factoria.getCancionDAO();
					if (e instanceof CancionesEvent) {
						Canciones canciones = ((CancionesEvent) e).getCanciones();
						for (umu.tds.componente.Cancion c : canciones.getCancion()) {
							// Convertir de Cancion.java de componente a Cancion.java de nuestra aplicación
							String[] s = c.getInterprete().split(", ");
							Cancion cancion = new Cancion(c.getTitulo(), c.getEstilo(), c.getURL(), s);
							if (!CatalogoCanciones.getUnicaInstancia().comprobarNuevaCancion(cancion)) {
								cancionDAO.create(cancion);
								CatalogoCanciones.getUnicaInstancia().addCancion(cancion);
							}
						}
					}
				}
			};
		}
		return cancionesListener;
	}

	public List<String> obtenerListasUsuario() {
		List<String> listas = CatalogoCanciones.getUnicaInstancia().obtenerListasUsuario(usuarioActual).stream()
				.map(lista -> lista.getNombre()).collect(Collectors.toList());
		if (usuarioActual.isPremium())
			listas.add("Más reproducidas");
		return listas;
	}

	public ListaCanciones obtenerListaCanciones(String nombreLista) {
		listaActual = CatalogoCanciones.getUnicaInstancia().obtenerListaCanciones(nombreLista, usuarioActual);
		return listaActual;
	}

	public void actualizarLista(List<Cancion> canciones) {
		CatalogoCanciones.getUnicaInstancia().anadirCancionesLista(canciones, listaActual, usuarioActual);
		TDSListaDAO cancionDAO = (TDSListaDAO) factoria.getListaDAO();
		cancionDAO.updateLista(listaActual);
	}

	public ListaCanciones obtenerCancionesMasReproducidas() {
		listaActual = CatalogoCanciones.getUnicaInstancia().getMasEscuchadas();
		return listaActual;
	}

	public ListaCanciones getListaActual() {
		return listaActual;
	}

	public void playSong() {
		if (cancionActual != null) {
			try {
				com.sun.javafx.application.PlatformImpl.startup(() -> {
				});
				if (cancionActual.getRutaFichero().startsWith("https")) {
					playSongHTTPS(new URL(cancionActual.getRutaFichero()));
				} else {
					File f = new File(cancionActual.getRutaFichero());
					System.out.println("Reproduciendo: " + f.getName());
					Media hit = new Media(f.toURI().toString());
					mediaPlayer = new MediaPlayer(hit);
					mediaPlayer.play();
				}

			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			cancionActual.addReproduccion();
			CancionDAO cancionDAO = factoria.getCancionDAO();
			cancionDAO.update(cancionActual);
			CatalogoCanciones.getUnicaInstancia().reproducirCancion(cancionActual);

			UsuarioDAO usuarioDAO = factoria.getUsuarioDAO();
			usuarioActual.addReciente(cancionActual);
			usuarioDAO.updatePerfil(usuarioActual);
		}
	}

	private void playSongHTTPS(URL uri) {

		System.setProperty("java.io.tmpdir", tempPath);
		Path mp3 = null;
		try {
			mp3 = Files.createTempFile("now-playing", ".mp3");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Descargando: " + mp3.getFileName());
		try (InputStream stream = uri.openStream()) {
			Files.copy(stream, mp3, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("finished-copy: " + mp3.getFileName());

		Media media = new Media(mp3.toFile().toURI().toString());
		mediaPlayer = new MediaPlayer(media);

		mediaPlayer.play();
	}

	public void stopSong() {
		if (mediaPlayer != null)
			mediaPlayer.stop();
		File directorio = new File(tempPath);
		String[] files = directorio.list();
		for (String archivo : files) {
			File fichero = new File(tempPath + File.separator + archivo);
			fichero.delete();
		}
	}

	public void nextSong() {
		if (listaActual != null) {
			LinkedList<Cancion> lista = (LinkedList<Cancion>) listaActual.getCanciones();
			int i = 0;
			for (Cancion c : lista) {
				if (cancionActual != null && (c.getId() == cancionActual.getId()) && i < lista.size() - 1) {
					cancionActual = lista.get(i + 1);
					return;
				}
				if (cancionActual != null && (c.getId() == cancionActual.getId()) && i == lista.size() - 1) {
					cancionActual = lista.getFirst();
					return;
				}
				i++;
			}
		}
	}

	public void previousSong() {
		if (listaActual != null) {
			LinkedList<Cancion> lista = (LinkedList<Cancion>) listaActual.getCanciones();
			int i = 0;
			for (Cancion c : lista) {
				if (cancionActual != null && (c.getId() == cancionActual.getId()) && i > 0) {
					cancionActual = lista.get(i - 1);
					return;
				}
				if (cancionActual != null && (c.getId() == cancionActual.getId()) && i == 0) {
					cancionActual = lista.getLast();
					return;
				}
				i++;
			}
		}
	}

	public String[] getEstilos() {
		return CatalogoCanciones.getUnicaInstancia().getEstilos();
	}

	public void seleccionarCancionDeTabla(String titulo, String interprete) {
		Optional<Cancion> c = CatalogoCanciones.getUnicaInstancia().getCancion(titulo, interprete);
		if (c.isPresent())
			cancionActual = c.get();
	}

	public Optional<Cancion> obtenerCancion(String titulo, String interprete) {
		Optional<Cancion> c = CatalogoCanciones.getUnicaInstancia().getCancion(titulo, interprete);
		if (c.isPresent())
			return c;
		return null;

	}

	public void generarPDF(String folder) throws FileNotFoundException, DocumentException {
		if (usuarioActual.isPremium()) {
			FileOutputStream archivo = new FileOutputStream(folder + "/" + usuarioActual.getUsuario() + ".pdf");
			Document documento = new Document();
			PdfWriter.getInstance(documento, archivo);
			documento.open();
			documento.add(new Paragraph("Listas del usuario " + usuarioActual.getUsuario() + ":"));

			List<String> listas = obtenerListasUsuario();
			if (listas != null) {
				for (String nombreLista : listas) {
					documento.add(new Paragraph("Lista: " + nombreLista));
					ListaCanciones lista = obtenerListaCanciones(nombreLista);
					if (lista != null && lista.getCanciones() != null) {
						lista.getCanciones().stream().forEach(c -> {
							try {
								documento.add(new Paragraph("   Titulo: " + c.getTitulo() + ", Interprete: "
										+ c.interpretesToString() + ", Estilo: " + c.getEstilo()));
							} catch (DocumentException e) {
								e.printStackTrace();
							}
						});

					}
				}
			}
			documento.close();
		}
	}

}
