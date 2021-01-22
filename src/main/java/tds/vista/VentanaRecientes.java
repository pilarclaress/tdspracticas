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
import tds.modelo.ListaCanciones;
import umu.tds.componente.CargadorCanciones;

public class VentanaRecientes {

	private JFrame frmReciente;
	private String name;
	private ListaCanciones lista;
	private JTable table;
	private Luz luz;
	private CargadorCanciones cargador;

	private JPanel panelTabla;

	public VentanaRecientes() {
		name = ControladorVistaModelo.getUnicaInstancia().getNombreUsuarioActual();
		lista = ControladorVistaModelo.getUnicaInstancia().getRecientes();
		initialize();
	}

	public void mostrarVentana() {
		frmReciente.setLocationRelativeTo(null);
		frmReciente.setVisible(true);
	}

	private void initialize() {
		frmReciente = new JFrame();
		frmReciente.setTitle("App Music");
		frmReciente.setMinimumSize(new Dimension(700, 400));
		frmReciente.setMaximumSize(new Dimension(700, 400));
		frmReciente.setPreferredSize(new Dimension(700, 400));
		frmReciente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmReciente.getContentPane().setLayout(new BorderLayout());

		JPanel panelNorte = new JPanel();
		panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));

		panelNorte.add(crearMensajeBienvenida());
		panelNorte.add(crearBotonesArriba());

		JPanel panelIzq = new JPanel();
		panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));

		panelIzq.add(crearBotonesIzquierda());

		JPanel panelCentro = new JPanel();
		panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
		panelCentro.add(crearTablaCanciones());
		panelCentro.add(crearBotonesReproductor(), BorderLayout.SOUTH);

		frmReciente.getContentPane().add(panelIzq, BorderLayout.WEST);
		frmReciente.getContentPane().add(panelNorte, BorderLayout.NORTH);
		frmReciente.getContentPane().add(panelCentro, BorderLayout.CENTER);

		frmReciente.setResizable(false);
		frmReciente.pack();

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
			frmReciente.dispose();
		});
	}

	private void crearManejadorBotonPremium(JButton premium) {
		premium.addActionListener(ev -> {
			if (!ControladorVistaModelo.getUnicaInstancia().getUsuarioActual().isPremium()) {
				Object[] options = { "Si", "No" };
				ImageIcon imageIcon = VentanaNuevaLista.getIcon("img/question.png", 4f);
				int n = JOptionPane.showOptionDialog(frmReciente, "¿Quieres convertirte en usuario premium?", "PREMIUM",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, imageIcon, options, options[1]);
				if (n == 0) {
					// Convertir el usuario a premium
					if (ControladorVistaModelo.getUnicaInstancia().hacerPremium()) {
						JOptionPane.showMessageDialog(frmReciente, "Eres un usuario premium", "ENHORABUENA",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(frmReciente, "Hubo un fallo al intentar convertirte a Premium",
								"ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			if (ControladorVistaModelo.getUnicaInstancia().getUsuarioActual().isPremium()) {
				Object[] options = { "Crear PDF", "Cambiar Contraseña", "Cambiar Email" };
				ImageIcon imageIcon = VentanaNuevaLista.getIcon("img/pdf.png", 4f);
				int n = JOptionPane.showOptionDialog(frmReciente, "Selecciona la acción premium a realizar", "PREMIUM",
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
							JOptionPane.showMessageDialog(frmReciente, "Fichero creado con éxito!", "PDF",
									JOptionPane.INFORMATION_MESSAGE);
						} else
							JOptionPane.showMessageDialog(frmReciente,
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
							JOptionPane.showMessageDialog(frmReciente, "Email cambiado con éxito!", "Cambio",
									JOptionPane.INFORMATION_MESSAGE);

						} else {
							a.setVisible(false);
							JOptionPane.showMessageDialog(frmReciente, "Este email ya existe!", "Cambio",
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
							JOptionPane.showMessageDialog(frmReciente, "Contraseña cambiada con éxito!", "Cambio",
									JOptionPane.INFORMATION_MESSAGE);

						} else {
							a.setVisible(false);
							JOptionPane.showMessageDialog(frmReciente, "Debes de escribir una contraseña", "Cambio",
									JOptionPane.INFORMATION_MESSAGE);

						}

					});

				}
			}
		});
	}

	private void crearManejadorBotonExplorar(JButton explorar) {
		explorar.addActionListener(ev -> {

			frmReciente.dispose();

			VentanaExplorar nueva = new VentanaExplorar();
			nueva.mostrarVentana();

		});
	}

	private void crearManejadorBotonNuevaLista(JButton newList) {
		newList.addActionListener(ev -> {
			frmReciente.dispose();

			VentanaNuevaLista nueva = new VentanaNuevaLista();
			nueva.mostrarVentana();
		});
	}

	private void crearManejadorBotonReciente(JButton reciente) {
		reciente.addActionListener(ev -> {

			frmReciente.dispose();

			VentanaRecientes hola = new VentanaRecientes();
			hola.mostrarVentana();

		});
	}

	private void crearManejadorBotonMisListas(JButton myList) {
		myList.addActionListener(ev -> {

			frmReciente.dispose();

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

		return panel;
	}

	// Crea la tabla con las canciones de la lista
	private JPanel crearTablaCanciones() {
		panelTabla = new JPanel();
		panelTabla.setMaximumSize(new Dimension(700, 150));
		JScrollPane scrollPane = new JScrollPane();
		panelTabla.add(scrollPane);

		DefaultTableModel modelo = new DefaultTableModel();
		modelo.addColumn("Titulo");
		modelo.addColumn("Interprete");

		modelo.fireTableDataChanged();
		table = new JTable(modelo);
		table.setPreferredScrollableViewportSize(new Dimension(450, 120));
		fixedSize(scrollPane, 500, 120);
		scrollPane.setViewportView(table);
		panelTabla.add(scrollPane);
		panelTabla.setVisible(false);
		rellenarTabla();
		crearManejadorTabla(table);
		return panelTabla;
	}

	private void rellenarTabla() {
		panelTabla.setVisible(true);
		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		modelo.setRowCount(0);
		lista = ControladorVistaModelo.getUnicaInstancia().getRecientes();

		if (ControladorVistaModelo.getUnicaInstancia().getRecientes() != null) {
			for (Cancion cancion : lista.getCanciones()) {
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

	// Clase interna que implementa AbstractTableModel
	// IGNORAR
	/*
	 * class MiTableModel extends AbstractTableModel { private String[] columnNames
	 * = { "Titulo", "Interprete" };
	 * 
	 * //Estos nombres eran para probar la tabla para que no falle private
	 * Object[][] data = { { "Cati", "Molina" }, { "Juan", "Diaz" }, { "Susana",
	 * "Blanco" }, { "Juana", "P�rez" }, };
	 * 
	 * @Override public int getColumnCount() { return columnNames.length; }
	 * 
	 * @Override public int getRowCount() { return data.length; }
	 * 
	 * @Override public Object getValueAt(int row, int col) { // TODO Ap�ndice de
	 * m�todo generado autom�ticamente return data[row][col]; }
	 * 
	 * }
	 */
	private void fixedSize(JComponent c, int x, int y) {
		c.setMinimumSize(new Dimension(x, y));
		c.setMaximumSize(new Dimension(x, y));
		c.setPreferredSize(new Dimension(x, y));
	}

}
