package tds.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemColor;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.itextpdf.text.DocumentException;

import pulsador.IEncendidoListener;
import pulsador.Luz;
import tds.controlador.ControladorVistaModelo;
import umu.tds.componente.CargadorCanciones;

public class VentanaPrincipal {

	private JFrame frmPrincipal;
	private String name;
	private Luz luz;
	private CargadorCanciones cargador;

	public VentanaPrincipal() {
		name = ControladorVistaModelo.getUnicaInstancia().getNombreUsuarioActual();
		initialize();
	}

	public void mostrarVentana() {
		frmPrincipal.setLocationRelativeTo(null);
		frmPrincipal.setVisible(true);
	}

	public void initialize() {
		frmPrincipal = new JFrame();
		frmPrincipal.setBackground(new Color(62, 60, 60));
		frmPrincipal.setTitle("App Music");
		frmPrincipal.setMinimumSize(new Dimension(700, 400));
		frmPrincipal.setMaximumSize(new Dimension(700, 400));
		frmPrincipal.setPreferredSize(new Dimension(700, 400));
		frmPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPrincipal.getContentPane().setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		panel.add(crearMensajeBienvenida(), BorderLayout.CENTER);
		panel.add(crearBotonesArriba(), BorderLayout.EAST);

		frmPrincipal.getContentPane().add(panel, BorderLayout.NORTH);

		JPanel panelIzq = new JPanel();
		panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));

		panelIzq.add(crearBotonesIzquierda());
		panelIzq.add(Box.createRigidArea(new Dimension(200, 60)));

		frmPrincipal.getContentPane().add(panelIzq, BorderLayout.WEST);
		JPanel hello = new JPanel();

		// Espacio que ocuparia la tabla de canciones
		hello.add(Box.createRigidArea(new Dimension(300, 200)));
		frmPrincipal.getContentPane().add(hello);

		frmPrincipal.setResizable(false);
		frmPrincipal.pack();

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
		nombre.setBackground(SystemColor.text);
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
			frmPrincipal.dispose();
		});
	}

	private void crearManejadorBotonPremium(JButton premium) {

		premium.addActionListener(ev -> {

			if (!ControladorVistaModelo.getUnicaInstancia().getUsuarioActual().isPremium()) {
				Object[] options = { "Si", "No" };
				ImageIcon imageIcon = VentanaNuevaLista.getIcon("img/question.png", 4f);
				int n = JOptionPane.showOptionDialog(frmPrincipal, "¿Quieres convertirte en usuario premium?",
						"PREMIUM", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, imageIcon, options,
						options[1]);
				if (n == 0) {
					// Convertir el usuario a premium
					if (ControladorVistaModelo.getUnicaInstancia().hacerPremium()) {
						JOptionPane.showMessageDialog(frmPrincipal, "Eres un usuario premium", "ENHORABUENA",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(frmPrincipal, "Hubo un fallo al intentar convertirte a Premium",
								"ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			if (ControladorVistaModelo.getUnicaInstancia().getUsuarioActual().isPremium()) {
				Object[] options = { "Crear PDF", "Cambiar Contraseña", "Cambiar Email" };
				ImageIcon imageIcon = VentanaNuevaLista.getIcon("img/pdf.png", 4f);
				int n = JOptionPane.showOptionDialog(frmPrincipal, "Selecciona la acción premium a realizar", "PREMIUM",
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
							JOptionPane.showMessageDialog(frmPrincipal, "Fichero creado con éxito!", "PDF",
									JOptionPane.INFORMATION_MESSAGE);
						} else
							JOptionPane.showMessageDialog(frmPrincipal,
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
							JOptionPane.showMessageDialog(frmPrincipal, "Email cambiado con éxito!", "Cambio",
									JOptionPane.INFORMATION_MESSAGE);

						} else {
							a.setVisible(false);
							JOptionPane.showMessageDialog(frmPrincipal, "Este email ya existe!", "Cambio",
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
							JOptionPane.showMessageDialog(frmPrincipal, "Contraseña cambiada con éxito!", "Cambio",
									JOptionPane.INFORMATION_MESSAGE);

						} else {
							a.setVisible(false);
							JOptionPane.showMessageDialog(frmPrincipal, "Debes de escribir una contraseña", "Cambio",
									JOptionPane.INFORMATION_MESSAGE);

						}

					});

				}
			}

		});
	}

	private void crearManejadorBotonExplorar(JButton explorar) {
		explorar.addActionListener(ev -> {

			frmPrincipal.dispose();

			VentanaExplorar nueva = new VentanaExplorar();
			nueva.mostrarVentana();

		});
	}

	private void crearManejadorBotonNuevaLista(JButton newList) {
		newList.addActionListener(ev -> {
			frmPrincipal.dispose();
			VentanaNuevaLista nueva = new VentanaNuevaLista();
			nueva.mostrarVentana();
		});
	}

	private void crearManejadorBotonReciente(JButton reciente) {
		reciente.addActionListener(ev -> {

			frmPrincipal.dispose();

			VentanaRecientes hola = new VentanaRecientes();
			hola.mostrarVentana();

		});
	}

	private void crearManejadorBotonMisListas(JButton myList) {
		myList.addActionListener(ev -> {

			frmPrincipal.dispose();

			VentanaMisListas hola = new VentanaMisListas();
			hola.mostrarVentana();

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
					Math.round(factor * tamanoLetra), // altura: tamano de la letra
					java.awt.Image.SCALE_SMOOTH // Método para reescalar (Calidad:SCALE_SMOOTH o rapidez SCALE_FAST)
			);
			// Se crea un ImageIcon
			return new ImageIcon(newimg);
		} catch (IOException e) {
			// Si falla la lectura de la imagen, el botón se generará sin icono. No es
			// necesario parar la ejecucion.
			return null;
		}
	}

	private void fixedSize(JComponent c, int x, int y) {
		c.setMinimumSize(new Dimension(x, y));
		c.setMaximumSize(new Dimension(x, y));
		c.setPreferredSize(new Dimension(x, y));
	}

}