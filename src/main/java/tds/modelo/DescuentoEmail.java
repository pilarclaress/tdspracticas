package tds.modelo;

//Descuento del 20% a los usuarios con un correo Gmail
public class DescuentoEmail extends Descuento {

	private String emailUsuario;
	private String emailDescuento = "@gmail.com";

	public DescuentoEmail(String email) {
		super();
		porcentaje = 40;
		this.emailUsuario = email;
	}

	public double calcDescuento(double precio) {
		return precio * (1 - porcentaje / 100);
	}

	public void setEmailUsuario(String email) {
		emailUsuario = email;
	}

	public boolean isAplicableDescuento() {
		return emailUsuario.contains(emailDescuento);
	}
}