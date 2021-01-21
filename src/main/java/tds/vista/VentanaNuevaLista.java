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
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
import umu.tds.componente.CargadorCanciones;

public class VentanaNuevaLista {

	private JFrame frmNewList;
	private String name;
	private JTextField nombreLista;
	private JTextField txtInterprete;
	private JTextField txtTitulo;
	private JTextField txtEstilo;

	private JLabel lblNombreError;
	private JPanel panelNuevaLista;
	private JComboBox<String> comboEstilo;
	private JButton btnEliminar;
	private Luz luz;
	private CargadorCanciones cargador;

	private JPanel panelIzq;
	private JPanel panelDer;
	private JTable tableIzq = new JTable();
	private JTable tableDer = new JTable();

	private List<Cancion> canciones;

	private String interprete;
	private String estilo;
	private String titulo;

	public VentanaNuevaLista() {
		name = ControladorVistaModelo.getUnicaInstancia().getNombreUsuarioActual();
		initialize();
	}

	public void mostrarVentana() {
		frmNewList.setLocationRelativeTo(null);
		frmNewList.setVisible(true);
	}

	public void initialize() {
		frmNewList = new JFrame();
		frmNewList.setTitle("App Music");
		frmNewList.setMinimumSize(new Dimension(700, 400));
		frmNewList.setMaximumSize(new Dimension(700, 400));
		frmNewList.setPreferredSize(new Dimension(700, 400));
		frmNewList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNewList.getContentPane().setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		panel.add(crearMensajeBienvenida(), BorderLayout.CENTER);
		panel.add(crearBotonesArriba(), BorderLayout.EAST);

		frmNewList.getContentPane().add(panel, BorderLayout.NORTH);

		JPanel panelIzq = new JPanel();
		panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));

		panelIzq.add(crearBotonesIzquierda());
		panelIzq.add(Box.createRigidArea(new Dimension(200, 60)));

		frmNewList.getContentPane().add(panelIzq, BorderLayout.WEST);

		JPanel panelCentro = new JPanel();
		fixedSize(panelCentro, 300, 300);
		panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

		panelCentro.add(crearNombreLista());

		panelNuevaLista = crearBotonesNuevaLista();
		panelNuevaLista.setVisible(false);

		// Espacio que ocuparia la tabla de canciones
		panelCentro.add(panelNuevaLista);
		frmNewList.getContentPane().add(panelCentro);

		frmNewList.setResizable(false);
		frmNewList.pack();

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
			frmNewList.dispose();
		});
	}

	private void crearManejadorBotonPremium(JButton premium) {
		premium.addActionListener(ev -> {
			if (!ControladorVistaModelo.getUnicaInstancia().getUsuarioActual().isPremium()) {
				Object[] options = { "Si", "No" };
				ImageIcon imageIcon = VentanaNuevaLista.getIcon("img/question.png", 4f);
				int n = JOptionPane.showOptionDialog(frmNewList, "¿Quieres convertirte en usuario premium?", "PREMIUM",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, imageIcon, options, options[1]);
				if (n == 0) {
					// Convertir el usuario a premium
					if (ControladorVistaModelo.getUnicaInstancia().hacerPremium()) {
						JOptionPane.showMessageDialog(frmNewList, "Eres un usuario premium", "ENHORABUENA",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(frmNewList, "Hubo un fallo al intentar convertirte a Premium",
								"ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			if (ControladorVistaModelo.getUnicaInstancia().getUsuarioActual().isPremium()) {
				Object[] options = { "Crear PDF", "Cambiar Contraseña", "Cambiar Email" };
				ImageIcon imageIcon = VentanaNuevaLista.getIcon("img/pdf.png", 4f);
				int n = JOptionPane.showOptionDialog(frmNewList, "Selecciona la acción premium a realizar", "PREMIUM",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, imageIcon, options, options[2]);
				if (n == 0) {
					// Crear PDF
					try {
						ControladorVistaModelo.getUnicaInstancia().generarPDF();
						JOptionPane.showMessageDialog(frmNewList, "Fichero creado con éxito!", "PDF",
								JOptionPane.INFORMATION_MESSAGE);
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
							JOptionPane.showMessageDialog(frmNewList, "Email cambiado con éxito!", "Cambio",
									JOptionPane.INFORMATION_MESSAGE);

						} else {
							a.setVisible(false);
							JOptionPane.showMessageDialog(frmNewList, "Este email ya existe!", "Cambio",
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
							JOptionPane.showMessageDialog(frmNewList, "Contraseña cambiada con éxito!", "Cambio",
									JOptionPane.INFORMATION_MESSAGE);

						} else {
							a.setVisible(false);
							JOptionPane.showMessageDialog(frmNewList, "Debes de escribir una contraseña", "Cambio",
									JOptionPane.INFORMATION_MESSAGE);

						}

					});

				}
			}
		});
	}

	private void crearManejadorBotonExplorar(JButton explorar) {
		explorar.addActionListener(ev -> {

			frmNewList.dispose();

			VentanaExplorar nueva = new VentanaExplorar();
			nueva.mostrarVentana();

		});
	}

	private void crearManejadorBotonNuevaLista(JButton newList) {
		newList.addActionListener(ev -> {
			frmNewList.dispose();
			VentanaNuevaLista nueva = new VentanaNuevaLista();
			nueva.mostrarVentana();
		});
	}

	private void crearManejadorBotonReciente(JButton reciente) {
		reciente.addActionListener(ev -> {

			frmNewList.dispose();

			VentanaRecientes hola = new VentanaRecientes();
			hola.mostrarVentana();

		});
	}

	private void crearManejadorBotonMisListas(JButton myList) {
		myList.addActionListener(ev -> {

			frmNewList.dispose();

			VentanaMisListas hola = new VentanaMisListas();
			hola.mostrarVentana();

		});
	}

	private JPanel crearNombreLista() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel campo1 = new JPanel();
		campo1.setLayout(new BoxLayout(campo1, BoxLayout.X_AXIS));
		nombreLista = new JTextField();
		nombreLista.setText("Nombre de la lista");
		fixedSize(nombreLista, 150, 24);
		campo1.add(nombreLista);

		JButton crear = new JButton("Crear");
		fixedSize(crear, 100, 24);

		crearManejadorBotonCrear(crear);

		btnEliminar = new JButton("Eliminar");
		fixedSize(btnEliminar, 100, 24);
		btnEliminar.setVisible(false);
		crearManejadorBotonEliminar(btnEliminar);

		JPanel pLabel = new JPanel();
		fixedSize(pLabel, 200, 20);

		lblNombreError = new JLabel("Introducir un nombre para la lista");
		fixedSize(lblNombreError, 200, 20);
		lblNombreError.setForeground(Color.RED);
		lblNombreError.setVisible(false);
		pLabel.add(lblNombreError);

		campo1.add(crear);
		campo1.add(btnEliminar, BorderLayout.PAGE_END);

		panel.add(campo1, BorderLayout.CENTER);
		panel.add(pLabel);
		return panel;
	}

	private void crearManejadorBotonCrear(JButton crear) {
		crear.addActionListener(ev -> {
			if (!nombreLista.getText().equals("Nombre de la lista") && !nombreLista.getText().equals("")) {
				lblNombreError.setVisible(false);
				if (ControladorVistaModelo.getUnicaInstancia().comprobarNuevaLista(nombreLista.getText())) {
					Object[] options = { "Si", "No" };
					ImageIcon imageIcon = VentanaNuevaLista.getIcon("img/question.png", 4f);
					int n = JOptionPane.showOptionDialog(frmNewList, "Desea crear una nueva lista?",
							"Crear nueva lista", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
							imageIcon, options, options[1]);
					if (n == 0) {
						ControladorVistaModelo.getUnicaInstancia().crearNuevaLista(nombreLista.getText());
						btnEliminar.setVisible(true);
						panelNuevaLista.setVisible(true);
					}
				} else {
					btnEliminar.setVisible(true);
					panelNuevaLista.setVisible(true);
					ControladorVistaModelo.getUnicaInstancia().obtenerListaCanciones(nombreLista.getText());
				}

			} else {
				lblNombreError.setVisible(true);

			}

		});
	}

	private void crearManejadorBotonEliminar(JButton eliminar) {
		eliminar.addActionListener(ev -> {
			ControladorVistaModelo.getUnicaInstancia().eliminarLista(nombreLista.getText());
			frmNewList.dispose();
			VentanaNuevaLista nueva = new VentanaNuevaLista();
			nueva.mostrarVentana();
		});
	}

	private JPanel crearBotonesNuevaLista() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(crearCamposBusqueda(), BorderLayout.NORTH);

		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.X_AXIS));
		panelIzq = crearTablaIzquierda();
		panelCentral.add(panelIzq);
		panelCentral.add(crearBotonesCentro());
		panelCentral.add(crearTablaDerecha());

		panel.add(panelCentral, BorderLayout.CENTER);
		panel.add(crearBotonesAceptarCancelar());

		return panel;
	}

	private JPanel crearCamposBusqueda() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel campos = new JPanel();
		campos.setLayout(new BoxLayout(campos, BoxLayout.X_AXIS));
		txtInterprete = new JTextField();
		txtInterprete.setText("Interprete");
		fixedSize(txtInterprete, 80, 24);

		txtTitulo = new JTextField();
		txtTitulo.setText("Titulo");
		fixedSize(txtTitulo, 80, 24);

		comboEstilo = new JComboBox<String>();
		comboEstilo.setEditable(false);
		fixedSize(comboEstilo, 80, 24);
		comboEstilo.addItem("Estilo");
		String[] estilos = ControladorVistaModelo.getUnicaInstancia().getEstilos();
		if (estilos != null) {
			for (String est : estilos)
				comboEstilo.addItem(est);
		}

		txtEstilo = new JTextField();
		txtEstilo.setText("Estilo");

		crearManejadorElegirEstilo(comboEstilo);

		campos.add(txtInterprete);
		campos.add(txtTitulo);
		campos.add(comboEstilo);

		panel.add(campos);

		JPanel panelBuscar = new JPanel();
		panelBuscar.setLayout(new BoxLayout(panelBuscar, BoxLayout.X_AXIS));

		JButton buscar = new JButton("Buscar");
		fixedSize(buscar, 80, 24);
		panelBuscar.add(buscar, BorderLayout.CENTER);

		panel.add(panelBuscar);

		crearManejadorBotonBuscar(buscar);

		return panel;
	}

	private void crearManejadorElegirEstilo(JComboBox<String> comboEstilo) {
		comboEstilo.addActionListener(ev -> {
			txtEstilo.setText((String) comboEstilo.getSelectedItem());
		});
	}

	private void checkFields() {
		if (txtInterprete.getText().equals("Interprete")) {
			interprete = null;
		} else {
			interprete = txtInterprete.getText();
		}

		if (txtEstilo.getText().equals("Estilo")) {
			estilo = null;
		} else {
			estilo = txtEstilo.getText();
		}

		if (txtTitulo.getText().equals("Titulo")) {
			titulo = null;
		} else {
			titulo = txtTitulo.getText();
		}
	}

	private void crearManejadorBotonBuscar(JButton buscar) {
		buscar.addActionListener(ev -> {
			ControladorVistaModelo controlador = ControladorVistaModelo.getUnicaInstancia();

			checkFields();

			canciones = controlador.buscarCancion(interprete, titulo, estilo);

			rellenarTabla(tableIzq);
		});
	}

	private JPanel crearTablaIzquierda() {
		// TODO
		panelIzq = new JPanel();
		panelIzq.add(Box.createRigidArea(new Dimension(30, 30)));
		JPanel yeipanel = crearTablaCancionesIzq();
		yeipanel.setVisible(true);
		panelIzq.add(yeipanel);
		return panelIzq;
	}

	private JPanel crearTablaDerecha() {
		// TODO
		panelDer = new JPanel();
		panelDer.add(Box.createRigidArea(new Dimension(30, 30)));
		JPanel yeipanel = crearTablaCancionesDer();
		yeipanel.setVisible(true);
		panelDer.add(yeipanel);
		return panelDer;
	}

	private JPanel crearBotonesCentro() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(Box.createRigidArea(new Dimension(50, 30)));

		JButton right = new JButton(">>");
		fixedSize(right, 50, 30);
		JButton left = new JButton("<<");
		fixedSize(left, 50, 30);

		panel.add(right);
		panel.add(left);

		panel.add(Box.createRigidArea(new Dimension(50, 30)));

		crearManejadorBotonesCentrales(right, left);

		return panel;
	}

	private void crearManejadorBotonesCentrales(JButton right, JButton left) {
		right.addActionListener(ev -> {
			if (ControladorVistaModelo.getUnicaInstancia().getCancionActual() != null) {
				Cancion c = ControladorVistaModelo.getUnicaInstancia().getCancionActual();
				panelDer.setVisible(true);
				DefaultTableModel modelo = (DefaultTableModel) tableDer.getModel();
				modelo.setRowCount(modelo.getRowCount());
				String[] fila = { c.getTitulo(), c.interpretesToString() };
				modelo.addRow(fila);

				modelo.fireTableDataChanged();
				tableDer.setModel(modelo);
			}

			left.addActionListener(et -> {
				if (tableDer.getSelectedRow() != -1) {
					panelIzq.setVisible(true);
					DefaultTableModel modelo = (DefaultTableModel) tableDer.getModel();
					modelo.removeRow(tableDer.getSelectedRow());
					modelo.fireTableDataChanged();
					tableDer.setModel(modelo);
				}
			});

		});
	}

	private void rellenarTabla(JTable table) {
		panelIzq.setVisible(true);
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

	private JPanel crearTablaCancionesIzq() {
		JPanel paneltablaIzq = new JPanel();
		paneltablaIzq.setMaximumSize(new Dimension(100, 100));
		JScrollPane scrollPane = new JScrollPane();
		paneltablaIzq.add(scrollPane);

		DefaultTableModel modelo = new DefaultTableModel();
		modelo.addColumn("Titulo");
		modelo.addColumn("Interprete");

		modelo.fireTableDataChanged();
		tableIzq = new JTable(modelo);
		tableIzq.setPreferredScrollableViewportSize(new Dimension(100, 100));
		fixedSize(scrollPane, 150, 150);
		scrollPane.setViewportView(tableIzq);
		paneltablaIzq.add(scrollPane);

		crearManejadorTabla(tableIzq);

		paneltablaIzq.setVisible(false);
		return paneltablaIzq;
	}

	private JPanel crearTablaCancionesDer() {
		JPanel paneltablaIzq = new JPanel();
		paneltablaIzq.setMaximumSize(new Dimension(100, 100));
		JScrollPane scrollPane = new JScrollPane();
		paneltablaIzq.add(scrollPane);

		DefaultTableModel modelo = new DefaultTableModel();
		modelo.addColumn("Titulo");
		modelo.addColumn("Interprete");

		modelo.fireTableDataChanged();
		tableDer = new JTable(modelo);
		tableDer.setPreferredScrollableViewportSize(new Dimension(100, 100));
		fixedSize(scrollPane, 150, 150);
		scrollPane.setViewportView(tableDer);
		paneltablaIzq.add(scrollPane);

		crearManejadorTabla(tableDer);

		paneltablaIzq.setVisible(false);
		return paneltablaIzq;
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

	private JPanel crearBotonesAceptarCancelar() {

		JPanel botones = new JPanel();
		botones.setLayout(new BoxLayout(botones, BoxLayout.X_AXIS));

		JButton aceptar = new JButton("Aceptar");
		JButton cancelar = new JButton("Cancelar");

		fixedSize(aceptar, 100, 30);
		fixedSize(cancelar, 100, 30);

		botones.add(aceptar);
		botones.add(cancelar);

		crearManejadorBotonAceptar(aceptar);
		crearManejadorBotonCancelar(cancelar);
		return botones;
	}

	private void crearManejadorBotonAceptar(JButton aceptar) {
		aceptar.addActionListener(ev -> {
			// Acepta las canciones de la lista
			DefaultTableModel modelo = (DefaultTableModel) tableDer.getModel();
			for (int i = 0; i < modelo.getRowCount(); i++) {
				Cancion a = ControladorVistaModelo.getUnicaInstancia()
						.obtenerCancion((String) modelo.getValueAt(i, 0), (String) modelo.getValueAt(i, 1)).get();
				ControladorVistaModelo.getUnicaInstancia().anadirCancionLista(a, nombreLista.getText());
			}

		});
	}

	private void crearManejadorBotonCancelar(JButton cancelar) {
		cancelar.addActionListener(ev -> {
			// Ponemos los filtros con su valor inicial
			txtInterprete.setText("Interprete");
			txtTitulo.setText("Titulo");
			txtEstilo.setText("Estilo");
			comboEstilo.setSelectedIndex(0);

			// Dejar las tablas vacias TODO
		});
	}

	/**
	 * Reescala una imagen al tamano de letra del sistema por el parametro <factor>.
	 * 
	 * @param imageUrl URL de la imagen partiendo de la carpeta del proyecto.
	 * @param factor   Factor a multiplicar el tamano de letra del sistema
	 * @return Un ImageIcon para añadir al componente
	 */
	public static ImageIcon getIcon(String imageUrl, float factor) {
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
			Image newimg = img.getScaledInstance(Math.round(factor * tamanoLetra * proporcion), // Anchura: tamaño
																								// de la letra
																								// multiplicado por
																								// la proporcion
																								// original.
					Math.round(factor * tamanoLetra), // altura: tamaño de la letra
					java.awt.Image.SCALE_SMOOTH // Método para reescalar (Calidad:SCALE_SMOOTH o rapidez
												// SCALE_FAST)
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
