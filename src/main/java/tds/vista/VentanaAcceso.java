package tds.vista;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import tds.controlador.ControladorVistaModelo;
import tds.modelo.Usuario;

public class VentanaAcceso {
	private JFrame frmRegistro;
	private JTextField textUsuario;
	private JPasswordField textPassword;

	/**
	 * Create the application.
	 */
	public VentanaAcceso() {
		initialize();
	}

	public void mostrarVentana() {
		frmRegistro.setLocationRelativeTo(null);
		frmRegistro.setVisible(true);
	}

	private void initialize() {
		frmRegistro = new JFrame();
		frmRegistro.setTitle("App Music");
		frmRegistro.setMinimumSize(new Dimension(500, 400));
		frmRegistro.setMaximumSize(new Dimension(500, 400));
		frmRegistro.setPreferredSize(new Dimension(500, 400));
		frmRegistro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRegistro.getContentPane().setLayout(new BorderLayout());

		crearPanelTitulo();
		crearPanelLogin();

		frmRegistro.setResizable(false);
		frmRegistro.pack();

	}

	private void crearPanelTitulo() {

		JPanel arriba = new JPanel();
		JLabel etiquetaArriba = new JLabel("App Music", SwingConstants.CENTER);
		arriba.add(etiquetaArriba);
		frmRegistro.getContentPane().add(arriba, BorderLayout.NORTH);
	}

	private void crearPanelLogin() {

		JPanel centro = new JPanel();
		centro.setLayout(new BorderLayout(0, 0));

		centro.add(Box.createRigidArea(new Dimension(100, 100)), BorderLayout.NORTH);

		centro.add(crearCampos(), BorderLayout.CENTER);
		centro.add(crearBotones(), BorderLayout.SOUTH);

		frmRegistro.getContentPane().add(centro, BorderLayout.CENTER);

		JPanel bloque = new JPanel();
		bloque.add(Box.createRigidArea(new Dimension(50, 0)));
		frmRegistro.add(bloque, BorderLayout.WEST);

		JPanel bloque2 = new JPanel();
		bloque2.add(Box.createRigidArea(new Dimension(50, 0)));
		frmRegistro.add(bloque2, BorderLayout.EAST);
	}

	private JPanel crearCampos() {

		JPanel contieneCampos = new JPanel();
		contieneCampos.setLayout(new BoxLayout(contieneCampos, BoxLayout.Y_AXIS));

		JPanel contieneUsuario = new JPanel();
		contieneUsuario.setLayout(new BoxLayout(contieneUsuario, BoxLayout.X_AXIS));

		JLabel usuario = new JLabel("Usuario : ");

		fixedSize(usuario, 100, 24);
		textUsuario = new JTextField();
		fixedSize(textUsuario, 200, 24);

		contieneUsuario.add(usuario);
		contieneUsuario.add(textUsuario);

		JPanel contieneContrasena = new JPanel();
		contieneContrasena.setLayout(new BoxLayout(contieneContrasena, BoxLayout.X_AXIS));

		JLabel contrasena = new JLabel("Contrase\u00F1a : ");
		fixedSize(contrasena, 100, 24);
		textPassword = new JPasswordField();
		fixedSize(textPassword, 200, 24);

		contieneContrasena.add(contrasena);
		contieneContrasena.add(textPassword);

		contieneCampos.add(contieneUsuario, BorderLayout.CENTER);
		contieneCampos.add(contieneContrasena);

		contieneCampos.add(Box.createRigidArea(new Dimension(50, 50)));
		return contieneCampos;
	}

	private JPanel crearBotones() {

		JPanel allButtons = new JPanel();
		allButtons.setLayout(new BoxLayout(allButtons, BoxLayout.Y_AXIS));

		JPanel contieneBotones = new JPanel();
		contieneBotones.setLayout(new BoxLayout(contieneBotones, BoxLayout.X_AXIS));

		JButton aceptar = new JButton("Aceptar");
		fixedSize(aceptar, 100, 30);

		contieneBotones.add(aceptar);
		contieneBotones.add(Box.createRigidArea(new Dimension(10, 20)));

		JButton cancelar = new JButton("Cancelar");
		fixedSize(cancelar, 100, 30);

		contieneBotones.add(cancelar);

		JPanel abajo = new JPanel();
		abajo.setLayout(new BoxLayout(abajo, BoxLayout.X_AXIS));

		JLabel regis = new JLabel("Si no estas registrado,", SwingConstants.CENTER);
		fixedSize(regis, 200, 24);
		abajo.add(regis);

		JButton registrate = new JButton("Registrate");
		fixedSize(registrate, 150, 30);
		abajo.add(registrate);
		allButtons.add(contieneBotones, BorderLayout.CENTER);
		allButtons.add(Box.createRigidArea(new Dimension(100, 20)));
		allButtons.add(abajo, BorderLayout.SOUTH);
		allButtons.add(Box.createRigidArea(new Dimension(50, 70)));

		addManejadorBotonLogin(aceptar);
		addManejadorBotonRegistro(registrate);
		addManejadorBotonCancelar(cancelar);

		return allButtons;
	}

	private void addManejadorBotonLogin(JButton btn) {
		btn.addActionListener(ev -> {

			ControladorVistaModelo alb = ControladorVistaModelo.getUnicaInstancia();

			String contra = String.valueOf(textPassword.getPassword());

			Usuario actual = alb.comprobacionUsuarioClave(textUsuario.getText(), contra);
			if (actual != null) {
				ControladorVistaModelo.getUnicaInstancia().setUsuarioActual(actual);
				frmRegistro.dispose();
				VentanaPrincipal hola = new VentanaPrincipal();
				hola.mostrarVentana();

			} else {
				JOptionPane.showMessageDialog(frmRegistro, "Nombre de usuario o contrase\u00F1a no valido", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

		});
	}

	private void addManejadorBotonRegistro(JButton btn) {
		btn.addActionListener(ev -> {
			VentanaNuevoUsuario nuevoUsuario = new VentanaNuevoUsuario();
			nuevoUsuario.mostrarVentana();

		});
	}

	private void addManejadorBotonCancelar(JButton btn) {
		btn.addActionListener(ev -> {
			textPassword.setText("");
			textUsuario.setText("");

		});
	}

	public static void main(String[] args) {

		VentanaAcceso nuevo = new VentanaAcceso();

		nuevo.mostrarVentana();
	}

	private void fixedSize(JComponent c, int x, int y) {
		c.setMinimumSize(new Dimension(x, y));
		c.setMaximumSize(new Dimension(x, y));
		c.setPreferredSize(new Dimension(x, y));
	}

}