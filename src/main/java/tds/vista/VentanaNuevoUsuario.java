package tds.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Date;

import javax.swing.BorderFactory;
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
import javax.swing.border.Border;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JSpinnerDateEditor;

import tds.controlador.ControladorVistaModelo;

public class VentanaNuevoUsuario {

	private JFrame frmRegistro;
	private JTextField txtNombre;
	private JTextField txtApellidos;
	private Date fechaNacimiento = null;
	private JTextField txtEmail;
	private JTextField txtUsuario;
	private JPasswordField txtPassword;
	private JPasswordField txtPasswordChk;
	private JDateChooser calendar;

	private JLabel lblRellenarCamposError;
	private JLabel lblUsuarioError;
	private JLabel lblEmailError;
	private JLabel lblPasswordError;
	private JLabel lblFechaError;

	public VentanaNuevoUsuario() {
		initialize();
	}

	public void mostrarVentana() {
		frmRegistro.setLocationRelativeTo(null);
		frmRegistro.setVisible(true);
	}

	private void initialize() {
		frmRegistro = new JFrame();
		frmRegistro.setResizable(false);
		frmRegistro.setTitle("Registro App Music");
		frmRegistro.setMinimumSize(new Dimension(500, 500));
		frmRegistro.setMaximumSize(new Dimension(500, 500));
		frmRegistro.setPreferredSize(new Dimension(500, 500));
		frmRegistro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRegistro.getContentPane().setLayout(new BorderLayout());

		JPanel centro = new JPanel();
		centro.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

		centro.add(Box.createRigidArea(new Dimension(20, 20)));
		centro.add(crearNombre());
		centro.add(Box.createRigidArea(new Dimension(20, 20)));

		centro.add(crearApellidos());
		centro.add(Box.createRigidArea(new Dimension(20, 20)));

		centro.add(crearFecha());
		centro.add(Box.createRigidArea(new Dimension(20, 20)));

		centro.add(crearEmail());
		centro.add(Box.createRigidArea(new Dimension(20, 20)));

		centro.add(crearUsuario());
		centro.add(Box.createRigidArea(new Dimension(20, 20)));

		centro.add(crearClave());
		centro.add(Box.createRigidArea(new Dimension(20, 20)));

		centro.add(crearClaveRepetida());

		centro.add(crearMensajesError(), BorderLayout.SOUTH);

		frmRegistro.getContentPane().add(centro, BorderLayout.CENTER);

		JPanel botones = new JPanel();
		fixedSize(botones, 300, 60);
		botones.add(crearBotones(), BorderLayout.CENTER);
		frmRegistro.getContentPane().add(botones, BorderLayout.AFTER_LAST_LINE);

		JPanel bloqueIzq = new JPanel();
		bloqueIzq.add(Box.createRigidArea(new Dimension(20, 20)));
		frmRegistro.getContentPane().add(bloqueIzq, BorderLayout.WEST);

		JPanel bloqueDos = new JPanel();
		bloqueDos.add(Box.createRigidArea(new Dimension(20, 20)));
		frmRegistro.getContentPane().add(bloqueDos, BorderLayout.EAST);

		ocultarErrores();

		frmRegistro.revalidate();
		frmRegistro.pack();

	}

	private JPanel crearNombre() {
		JPanel n = new JPanel();
		n.setLayout(new BoxLayout(n, BoxLayout.X_AXIS));
		n.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		JLabel nombre = new JLabel("Nombre :");
		fixedSize(nombre, 150, 30);
		txtNombre = new JTextField();
		fixedSize(txtNombre, 200, 30);
		n.add(nombre);
		n.add(txtNombre);
		return n;
	}

	private JPanel crearApellidos() {
		JPanel a = new JPanel();
		a.setLayout(new BoxLayout(a, BoxLayout.X_AXIS));
		a.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		JLabel apellidos = new JLabel("Apellidos :");
		fixedSize(apellidos, 150, 30);
		txtApellidos = new JTextField();
		fixedSize(txtApellidos, 200, 30);
		a.add(apellidos);
		a.add(txtApellidos);
		return a;
	}

	private JPanel crearFecha() {
		JPanel c = new JPanel();
		c.setLayout(new BoxLayout(c, BoxLayout.X_AXIS));
		c.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		JLabel fecha = new JLabel("Fecha Nacimiento :");
		fixedSize(fecha, 150, 30);
		c.add(fecha);

		calendar = new JDateChooser(null, null, null, new JSpinnerDateEditor());
		fixedSize(calendar, 200, 30);

		c.add(calendar);
		return c;
	}

	private JPanel crearEmail() {
		JPanel e = new JPanel();
		e.setLayout(new BoxLayout(e, BoxLayout.X_AXIS));
		e.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		JLabel email = new JLabel("Email :");
		fixedSize(email, 150, 30);
		txtEmail = new JTextField();
		fixedSize(txtEmail, 200, 30);
		e.add(email);
		e.add(txtEmail);
		return e;
	}

	private JPanel crearUsuario() {
		JPanel u = new JPanel();
		u.setLayout(new BoxLayout(u, BoxLayout.X_AXIS));
		u.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		JLabel usuario = new JLabel("Usuario :");
		fixedSize(usuario, 150, 30);
		txtUsuario = new JTextField();
		fixedSize(txtUsuario, 200, 30);
		u.add(usuario);
		u.add(txtUsuario);
		return u;
	}

	private JPanel crearClave() {
		JPanel cl = new JPanel();
		cl.setLayout(new BoxLayout(cl, BoxLayout.X_AXIS));
		cl.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		JLabel clave = new JLabel("Clave :");
		fixedSize(clave, 150, 30);
		txtPassword = new JPasswordField();
		fixedSize(txtPassword, 200, 30);
		cl.add(clave);
		cl.add(txtPassword);
		return cl;
	}

	private JPanel crearClaveRepetida() {
		JPanel cl = new JPanel();
		cl.setLayout(new BoxLayout(cl, BoxLayout.X_AXIS));
		cl.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		JLabel repite = new JLabel("Repite la clave :");
		fixedSize(repite, 150, 30);
		txtPasswordChk = new JPasswordField();
		fixedSize(txtPasswordChk, 200, 30);
		cl.add(repite);
		cl.add(txtPasswordChk);
		return cl;
	}

	// Panel en el que se mostrarán los mensajes de error si los hay
	private JPanel crearMensajesError() {
		JPanel u = new JPanel();
		u.setLayout(new BoxLayout(u, BoxLayout.Y_AXIS));
		u.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		lblRellenarCamposError = new JLabel("# Campo obligatorio", SwingConstants.LEFT);
		fixedSize(lblRellenarCamposError, 200, 15);
		lblRellenarCamposError.setForeground(Color.RED);
		u.add(lblRellenarCamposError);

		lblUsuarioError = new JLabel("# El usuario ya existe", SwingConstants.LEFT);
		fixedSize(lblUsuarioError, 200, 15);
		lblUsuarioError.setForeground(Color.RED);
		u.add(lblUsuarioError);

		lblPasswordError = new JLabel("# Error al introducir las contrase\u00F1as", JLabel.LEFT);
		u.add(lblPasswordError);
		lblPasswordError.setForeground(Color.RED);

		lblEmailError = new JLabel("# Este correo está en uso", JLabel.LEFT);
		u.add(lblEmailError);
		lblEmailError.setForeground(Color.RED);

		lblFechaError = new JLabel("# La fecha introducida no es correcta", JLabel.LEFT);
		u.add(lblFechaError);
		lblFechaError.setForeground(Color.RED);

		return u;
	}

	private JPanel crearBotones() {
		JPanel abajo = new JPanel();
		fixedSize(abajo, 400, 30);
		abajo.setLayout(new BoxLayout(abajo, BoxLayout.X_AXIS));
		JButton registrar = new JButton("Registrar");
		fixedSize(registrar, 100, 30);
		abajo.add(registrar, BorderLayout.CENTER);

		JButton cancelar = new JButton("Cancelar");
		fixedSize(cancelar, 100, 30);
		abajo.add(cancelar);

		addManejadorBotonRegistrar(registrar);
		addManejadorBotonCancelar(cancelar);
		return abajo;
	}

	private void addManejadorBotonRegistrar(JButton registrar) {
		registrar.addActionListener(ev -> {
			boolean OK = false;
			fechaNacimiento = calendar.getDate();
			OK = checkFields();
			if (OK) {
				boolean registrado = false;
				String contra = String.valueOf(txtPassword.getPassword());
				registrado = ControladorVistaModelo.getUnicaInstancia().generarUsuario(txtNombre.getText(),
						txtApellidos.getText(), fechaNacimiento.toString(), txtEmail.getText(), txtUsuario.getText(),
						contra);

				frmRegistro.dispose();
				if (registrado) {
					JOptionPane.showMessageDialog(frmRegistro, "Asistente registrado correctamente.", "Registro",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(frmRegistro, "No se ha podido llevar a cabo el registro.\n",
							"Registro", JOptionPane.ERROR_MESSAGE);
					frmRegistro.setTitle("Login Gestor Eventos");
				}

			}
		});
	}

	private void addManejadorBotonCancelar(JButton cancelar) {
		cancelar.addActionListener(ev -> {
			frmRegistro.dispose();
		});

	}

	/**
	 * Comprueba que los campos de registro est�n bien
	 */
	private boolean checkFields() {
		boolean salida = true;
		/* borrar todos los errores en pantalla */
		ocultarErrores();

		if (txtNombre.getText().trim().isEmpty()) {
			lblRellenarCamposError.setVisible(true);
			txtNombre.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}
		if (txtApellidos.getText().trim().isEmpty()) {
			lblRellenarCamposError.setVisible(true);
			txtApellidos.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}
		if (txtEmail.getText().trim().isEmpty()) {
			lblRellenarCamposError.setVisible(true);
			txtEmail.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}
		if (txtUsuario.getText().trim().isEmpty()) {
			lblRellenarCamposError.setVisible(true);
			txtUsuario.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}
		String password = new String(txtPassword.getPassword());
		String password2 = new String(txtPasswordChk.getPassword());
		if (password.isEmpty()) {
			lblRellenarCamposError.setVisible(true);
			txtPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}
		if (password2.isEmpty()) {
			lblRellenarCamposError.setVisible(true);
			txtPasswordChk.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}
		if (!password.equals(password2)) {
			lblPasswordError.setVisible(true);
			txtPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
			txtPasswordChk.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}
		/* Comprobar que no exista otro usuario con igual login */
		if (!lblUsuarioError.getText().isEmpty()
				&& ControladorVistaModelo.getUnicaInstancia().esUsuarioRegistrado(txtUsuario.getText())) {
			lblUsuarioError.setText("Ya existe ese usuario");
			lblUsuarioError.setVisible(true);
			txtUsuario.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}

		if (ControladorVistaModelo.getUnicaInstancia().esEmailRegistrado(txtEmail.getText())) {
			lblEmailError.setVisible(true);
			txtEmail.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}

		if (fechaNacimiento == null) {
			lblRellenarCamposError.setVisible(true);
			calendar.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		} else if (fechaNacimiento.after(new Date())) {
			lblFechaError.setVisible(true);
			calendar.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}

		frmRegistro.revalidate();
		frmRegistro.pack();

		return salida;
	}

	/**
	 * Oculta todos los errores que pueda haber en la pantalla
	 */
	private void ocultarErrores() {
		lblUsuarioError.setVisible(false);
		lblPasswordError.setVisible(false);
		lblEmailError.setVisible(false);
		lblRellenarCamposError.setVisible(false);
		lblFechaError.setVisible(false);

		Border border = new JTextField().getBorder();
		txtNombre.setBorder(border);
		txtApellidos.setBorder(border);
		txtEmail.setBorder(border);
		txtUsuario.setBorder(border);
		txtPassword.setBorder(border);
		txtPasswordChk.setBorder(border);
		txtPassword.setBorder(border);
		txtPasswordChk.setBorder(border);
		txtUsuario.setBorder(border);
		calendar.setBorder(border);
	}

	private void fixedSize(JComponent c, int x, int y) {
		c.setMinimumSize(new Dimension(x, y));
		c.setMaximumSize(new Dimension(x, y));
		c.setPreferredSize(new Dimension(x, y));
	}

}