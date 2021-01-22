package tds.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EventObject;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.DocumentException;

import pulsador.IEncendidoListener;
import pulsador.Luz;
import tds.controlador.ControladorVistaModelo;
import tds.modelo.Cancion;
import umu.tds.componente.CargadorCanciones;

//Esta ventana es igual en forma a la de canciones recientes y canciones mas escuchadas por el usuario
public class VentanaMisListas {

	private JFrame frmMyList;
	private String name;
	private Luz luz;
	private CargadorCanciones cargador;

	private JTable table;
	private JPanel panelTabla;

	private List<Cancion> canciones = null;

	public VentanaMisListas() {
		name = ControladorVistaModelo.getUnicaInstancia().getNombreUsuarioActual();
		initialize();
	}

	public void mostrarVentana() {
		frmMyList.setLocationRelativeTo(null);
		frmMyList.setVisible(true);
	}

	private void initialize() {
		frmMyList = new JFrame();
		frmMyList.setTitle("App Music");
		frmMyList.setMinimumSize(new Dimension(700, 400));
		frmMyList.setMaximumSize(new Dimension(700, 400));
		frmMyList.setPreferredSize(new Dimension(700, 400));
		frmMyList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMyList.getContentPane().setLayout(new BorderLayout());

		JPanel panelNorte = new JPanel();
		panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));

		panelNorte.add(crearMensajeBienvenida());
		panelNorte.add(crearBotonesArriba());

		JPanel panelIzq = new JPanel();
		panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));

		panelIzq.add(crearBotonesIzquierda());
		panelIzq.add(crearTablaListas());

		JPanel panelCentro = new JPanel();
		panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
		panelCentro.add(panelTabla);
		panelCentro.add(crearBotonesReproductor(), BorderLayout.SOUTH);

		frmMyList.getContentPane().add(panelIzq, BorderLayout.WEST);
		frmMyList.getContentPane().add(panelNorte, BorderLayout.NORTH);
		frmMyList.getContentPane().add(panelCentro, BorderLayout.CENTER);

		frmMyList.setResizable(false);
		frmMyList.pack();

	}

	private JPanel crearMensajeBienvenida() {
		JPanel alc = new JPanel();
		luz = new Luz();
		luz.addEncendidoListener(new IEncendidoListener() {
			public void enteradoCambioEncendido(EventObject e) {
				cargador = new CargadorCanciones();
				cargador.addCancionesListener(ControladorVistaModelo.getUnicaInstancia().getCancionesListener());
				cargador.mostrarVentana();
			}
		});
		alc.add(luz);
		JLabel nombre = new JLabel("Hola " + name, SwingConstants.CENTER);
		fixedSize(nombre, 100, 24);
		alc.add(nombre);
		return alc;
	}

	private JPanel crearBotonesArriba() {
		JPanel alb = new JPanel();

		JButton mejora = new JButton("Premium");
		mejora.setBackground(new Color(236, 236, 236));
		fixedSize(mejora, 150, 30);
		JButton fuera = new JButton("Chao pescao");
		fuera.setBackground(new Color(236, 236, 236));
		fixedSize(fuera, 150, 30);

		alb.add(mejora);
		alb.add(fuera);

		crearManejadorBotonFuera(fuera);
		crearManejadorBotonPremium(mejora);

		return alb;
	}

	private JPanel crearBotonesIzquierda() {
		JPanel ass = new JPanel();
		fixedSize(ass, 200, 250);

		ass.setLayout(new GridLayout(4, 0));
		JButton explorar = new JButton("Explorar");
		explorar.setBackground(Color.LIGHT_GRAY);

		crearManejadorBotonExplorar(explorar);

		JButton newList = new JButton("Nueva Lista");
		newList.setBackground(Color.LIGHT_GRAY);

		crearManejadorBotonNuevaLista(newList);

		JButton reciente = new JButton("Reciente");
		reciente.setBackground(Color.LIGHT_GRAY);

		crearManejadorBotonReciente(reciente);

		JButton myList = new JButton("Mis Listas");
		myList.setBackground(Color.LIGHT_GRAY);

		crearManejadorBotonMisListas(myList);

		ass.add(explorar);
		ass.add(newList);
		ass.add(reciente);
		ass.add(myList);

		ImageIcon imageIcon1 = getIcon("img/lupa.png", 4f);
		ImageIcon imageIcon2 = getIcon("img/nuevalista.png", 4f);
		ImageIcon imageIcon3 = getIcon("img/recientes.png", 4f);
		ImageIcon imageIcon4 = getIcon("img/mislistas.png", 4f);

		// Se añade el icono al boton
		explorar.setIcon(imageIcon1);
		newList.setIcon(imageIcon2);
		reciente.setIcon(imageIcon3);
		myList.setIcon(imageIcon4);

		return ass;
	}

	private void crearManejadorBotonFuera(JButton fuera) {
		fuera.addActionListener(ev -> {
			frmMyList.dispose();
		});
	}

	private void crearManejadorBotonPremium(JButton premium) {
		premium.addActionListener(ev -> {
			if (!ControladorVistaModelo.getUnicaInstancia().getUsuarioActual().isPremium()) {
				Object[] options = { "Si", "No" };
				ImageIcon imageIcon = VentanaNuevaLista.getIcon("img/question.png", 4f);
				int n = JOptionPane.showOptionDialog(frmMyList, "¿Quieres convertirte en usuario premium?", "PREMIUM",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, imageIcon, options, options[1]);
				if (n == 0) {
					// Convertir el usuario a premium
					if (ControladorVistaModelo.getUnicaInstancia().hacerPremium()) {
						JOptionPane.showMessageDialog(frmMyList, "Eres un usuario premium", "ENHORABUENA",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(frmMyList, "Hubo un fallo al intentar convertirte a Premium",
								"ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			if (ControladorVistaModelo.getUnicaInstancia().getUsuarioActual().isPremium()) {
				Object[] options = { "Crear PDF", "Cambiar Contraseña", "Cambiar Email" };
				ImageIcon imageIcon = VentanaNuevaLista.getIcon("img/pdf.png", 4f);
				int n = JOptionPane.showOptionDialog(frmMyList, "Selecciona la acción premium a realizar", "PREMIUM",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, imageIcon, options, options[2]);
				if (n == 0) {
					// Crear PDF
					try {
						// Mostramos un jdialog para que seleccione la carpeta donde se creará el pdf
						JDialog jDialog = new JDialog();
						jDialog.setTitle("Seleccionar la carpeta para el PDF");
						JFileChooser folderChooser = new JFileChooser();
						folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int selection = folderChooser.showSaveDialog(jDialog);
						if (selection == JFileChooser.APPROVE_OPTION) {
							String folder = folderChooser.getSelectedFile().getAbsolutePath();
							ControladorVistaModelo.getUnicaInstancia().generarPDF(folder);
							jDialog.dispose();
							jDialog = null;
							JOptionPane.showMessageDialog(frmMyList, "Fichero creado con éxito!", "PDF",
									JOptionPane.INFORMATION_MESSAGE);
						} else
							JOptionPane.showMessageDialog(frmMyList,
									"No se ha podido crear el fichero en el directorio indicado", "PDF",
									JOptionPane.ERROR_MESSAGE);

					} catch (FileNotFoundException | DocumentException e) {
						e.printStackTrace();
					}
				} else if (n == 2) {

					JFrame a = new JFrame();
					JPanel al = new JPanel();
					JLabel nuevoEmail = new JLabel("Nuevo e-mail: ");
					JTextField as = new JTextField(10);
					JButton aceptar = new JButton("Aceptar");
					al.add(nuevoEmail);
					al.add(as);
					al.add(aceptar);
					a.add(al);
					a.setSize(300, 300);
					a.setVisible(true);
					aceptar.addActionListener(tv -> {
						if (ControladorVistaModelo.getUnicaInstancia().cambiarEmail(as.getText())) {
							a.setVisible(false);
							JOptionPane.showMessageDialog(frmMyList, "Email cambiado con éxito!", "Cambio",
									JOptionPane.INFORMATION_MESSAGE);

						} else {
							a.setVisible(false);
							JOptionPane.showMessageDialog(frmMyList, "Este email ya existe!", "Cambio",
									JOptionPane.INFORMATION_MESSAGE);

						}

					});

				} else if (n == 1) {

					JFrame a = new JFrame();
					JPanel al = new JPanel();
					JLabel nuevoEmail = new JLabel("Nueva contraseña: ");
					JTextField as = new JTextField(10);
					JButton aceptar = new JButton("Aceptar");
					al.add(nuevoEmail);
					al.add(as);
					al.add(aceptar);
					a.add(al);
					a.setSize(300, 300);
					a.setVisible(true);
					aceptar.addActionListener(tv -> {
						if (ControladorVistaModelo.getUnicaInstancia().cambiarContra(as.getText())) {
							a.setVisible(false);
							JOptionPane.showMessageDialog(frmMyList, "Contraseña cambiada con éxito!", "Cambio",
									JOptionPane.INFORMATION_MESSAGE);

						} else {
							a.setVisible(false);
							JOptionPane.showMessageDialog(frmMyList, "Debes de escribir una contraseña", "Cambio",
									JOptionPane.INFORMATION_MESSAGE);

						}

					});

				}
			}
		});
	}

	private void crearManejadorBotonExplorar(JButton explorar) {
		explorar.addActionListener(ev -> {

			frmMyList.dispose();

			VentanaExplorar nueva = new VentanaExplorar();
			nueva.mostrarVentana();

		});
	}

	private void crearManejadorBotonNuevaLista(JButton newList) {
		newList.addActionListener(ev -> {
			frmMyList.dispose();
			VentanaNuevaLista nueva = new VentanaNuevaLista();
			nueva.mostrarVentana();

		});
	}

	private void crearManejadorBotonReciente(JButton reciente) {
		reciente.addActionListener(ev -> {

			frmMyList.dispose();

			VentanaRecientes hola = new VentanaRecientes();
			hola.mostrarVentana();

		});
	}

	private void crearManejadorBotonMisListas(JButton myList) {
		myList.addActionListener(ev -> {

			frmMyList.dispose();

			VentanaMisListas hola = new VentanaMisListas();
			hola.mostrarVentana();

		});
	}

	private JPanel crearBotonesReproductor() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JButton play = new JButton();
		fixedSize(play, 50, 30);
		JButton stop = new JButton();
		fixedSize(stop, 50, 30);
		JButton forward = new JButton();
		fixedSize(forward, 50, 30);
		JButton backward = new JButton();
		fixedSize(backward, 50, 30);

		ImageIcon imageIconP = getIcon("img/play.png", 2f);
		ImageIcon imageIconS = getIcon("img/stop.png", 3f);
		ImageIcon imageIconF = getIcon("img/forward.png", 2f);
		ImageIcon imageIconB = getIcon("img/backward.png", 2f);

		play.setIcon(imageIconP);
		stop.setIcon(imageIconS);
		forward.setIcon(imageIconF);
		backward.setIcon(imageIconB);

		JPanel panelGrid = new JPanel(new GridLayout(2, 3));
		fixedSize(panelGrid, 210, 60);

		panelGrid.add(Box.createRigidArea(new Dimension(30, 30)));
		panelGrid.add(play);
		play.setBackground(Color.WHITE);
		panelGrid.add(Box.createRigidArea(new Dimension(30, 30)));

		panelGrid.add(backward);
		backward.setBackground(Color.WHITE);
		panelGrid.add(stop);
		stop.setBackground(Color.WHITE);

		panelGrid.add(forward);
		forward.setBackground(Color.WHITE);

		panel.add(panelGrid);

		añadirManejadorPlay(play);
		añadirManejadorBackward(backward);
		añadirManejadorForward(forward);
		añadirManejadorStop(stop);

		return panel;
	}

	private void añadirManejadorStop(JButton stop) {
		stop.addActionListener(ev -> {
			ControladorVistaModelo.getUnicaInstancia().stopSong();
		});
	}

	private void añadirManejadorBackward(JButton backward) {
		backward.addActionListener(ev -> {
			ControladorVistaModelo.getUnicaInstancia().stopSong();
			ControladorVistaModelo.getUnicaInstancia().previousSong();
			ControladorVistaModelo.getUnicaInstancia().playSong();
		});
	}

	private void añadirManejadorForward(JButton forward) {
		forward.addActionListener(ev -> {
			ControladorVistaModelo.getUnicaInstancia().stopSong();
			ControladorVistaModelo.getUnicaInstancia().nextSong();
			ControladorVistaModelo.getUnicaInstancia().playSong();
		});
	}

	private void añadirManejadorPlay(JButton play) {
		play.addActionListener(ev -> {
			ControladorVistaModelo.getUnicaInstancia().stopSong();
			ControladorVistaModelo.getUnicaInstancia().playSong();
		});
	}

	// Crea la lista con las listas de canciones que tiene el usuario
	private JPanel crearTablaListas() {
		JPanel panel = new JPanel();
		JScrollPane scrollPanel = new JScrollPane();

		String[] nombresPlaylists = (String[]) ControladorVistaModelo.getUnicaInstancia().obtenerListasUsuario()
				.toArray(new String[0]);
		if (nombresPlaylists.length != 0) {
			canciones = ControladorVistaModelo.getUnicaInstancia().obtenerListaCanciones(nombresPlaylists[0])
					.getCanciones();
			crearTablaCanciones();
		}
		JList<String> playlists = new JList<String>(nombresPlaylists);
		playlists.setSelectedIndex(0);

		scrollPanel.setViewportView(playlists);
		crearManejadorListas(playlists);

		fixedSize(playlists, 200, 1000);
		fixedSize(scrollPanel, 200, 50);
		fixedSize(panel, 200, 50);
		panel.add(scrollPanel);
		return panel;
	}

	private void crearManejadorListas(JList<String> playlist) {
		playlist.addListSelectionListener(ev -> {
			String l = playlist.getSelectedValue();
			if (!l.equals("Más reproducidas"))
				canciones = ControladorVistaModelo.getUnicaInstancia().obtenerListaCanciones(l).getCanciones();
			else
				canciones = ControladorVistaModelo.getUnicaInstancia().obtenerCancionesMasReproducidas().getCanciones();
			rellenarTabla();
		});
	}

	// Crea la tabla con las canciones de la lista
	private JPanel crearTablaCanciones() {
		panelTabla = new JPanel();
		panelTabla.setMaximumSize(new Dimension(450, 170));
		JScrollPane scrollPane = new JScrollPane();
		panelTabla.add(scrollPane);

		DefaultTableModel modelo = new DefaultTableModel();
		modelo.addColumn("Titulo");
		modelo.addColumn("Interprete");

		modelo.fireTableDataChanged();
		table = new JTable(modelo);
		table.setPreferredScrollableViewportSize(new Dimension(450, 170));
		table.setVisible(false);
		fixedSize(scrollPane, 450, 170);
		scrollPane.setViewportView(table);

		crearManejadorTabla(table);

		rellenarTabla();

		return panelTabla;
	}

	private void rellenarTabla() {
		table.setVisible(true);
		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		modelo.setRowCount(0);
		if (canciones != null) {
			for (Cancion cancion : canciones) {
				String[] fila = { cancion.getTitulo(), cancion.interpretesToString() };
				modelo.addRow(fila);
			}
		}
		modelo.fireTableDataChanged();
		table.setModel(modelo);
	}

	private void crearManejadorTabla(JTable tabla) {
		tabla.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {
				int fila = tabla.rowAtPoint(ev.getPoint());
				if (fila > -1) {
					String titulo = (String) tabla.getValueAt(fila, 0);
					String interp = (String) tabla.getValueAt(fila, 1);
					ControladorVistaModelo.getUnicaInstancia().seleccionarCancionDeTabla(titulo, interp);
				}
			}
		});
	}

	/**
	 * Reescala una imagen al tamano de letra del sistema por el parametro <factor>.
	 * 
	 * @param imageUrl URL de la imagen partiendo de la carpeta del proyecto.
	 * @param factor   Factor a multiplicar el tamano de letra del sistema
	 * @return Un ImageIcon para añadir al componente
	 */
	private ImageIcon getIcon(String imageUrl, float factor) {
		try {
			// Leer la imagen
			BufferedImage img = ImageIO.read(new File(imageUrl));
			// Obtenemos la proporcion ancho / altura.
			float proporcion = img.getWidth() / ((float) img.getHeight());
			// Obtenemos la Fuente (letra) por defecto especificada por el SO para un
			// textPane.
			Font font = UIManager.getDefaults().getFont("TextPane.font");
			// Obtenemos el tamaño de letra.
			int tamanoLetra = font.getSize();

			// Se reeescala la iamgen.
			Image newimg = img.getScaledInstance(Math.round(factor * tamanoLetra * proporcion), // Anchura: tamaño de
																								// la letra multiplicado
																								// por la proporcion
																								// original.
					Math.round(factor * tamanoLetra), // altura: tamaño de la letra
					java.awt.Image.SCALE_SMOOTH // Método para reescalar (Calidad:SCALE_SMOOTH o rapidez SCALE_FAST)
			);
			// Se crea un ImageIcon
			return new ImageIcon(newimg);
		} catch (IOException e) {
			// Si falla la lectura de la imagen, el botón se generará sin icono. No es
			// necesario parar la ejecución.
			return null;
		}
	}

	private void fixedSize(JComponent c, int x, int y) {
		c.setMinimumSize(new Dimension(x, y));
		c.setMaximumSize(new Dimension(x, y));
		c.setPreferredSize(new Dimension(x, y));
	}
}
