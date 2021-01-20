package tds.modelo;

import java.util.LinkedList;
import java.util.List;

public class Usuario {
	public static int MAXRECIENTES = 10;

	private int id;
	private String nombre;
	private String apellidos;
	private Boolean premium;
	private String fechaNacimiento;
	private String email;
	private String usuario;
	private String contrasena;
	private LinkedList<Cancion> recientes;
	private ContextoDescuento ctx;

	public Usuario(String nombre, String apellidos, String fechaNacimiento, String email, String usuario,
			String contrasena) {
		this.id = 0;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
		this.email = email;
		this.usuario = usuario;
		this.contrasena = contrasena;
		premium = false;
		recientes = new LinkedList<Cancion>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public Boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean p) {
		this.premium = p;
	}

	public boolean hacerPremium() {
		if (!premium) {
			ctx = new ContextoDescuento();
			Descuento descuento = new DescuentoFijo();
			ctx.setDescuento(descuento);

			double precio = ctx.calcularPrecio();
			System.out.println("Se ha aplicado un descuento del " + ctx.getPorcentajeDescuento() + "%");
			System.out.println("Total a pagar: " + precio + "€");

			// REALIZA PAGO
			// SI EL PAGO FALLARA premium=false
			// Pero como no se implementa el pago no falla nunca

			premium = true;
			System.out.println("El usuario " + usuario + " ahora es premium");
		}
		return premium;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public boolean comprobacion(String contrasena) {
		return contrasena.equals(this.contrasena);
	}

	@SuppressWarnings("unchecked")
	public List<Cancion> getRecientes() {
		return (List<Cancion>) recientes.clone();
	}

	// Cada vez que escucha una cancion la añade a la lista de recientes
	// Pero recientes solo puede tener 10 canciones como máximo
	public void addReciente(Cancion nueva) {
		if (recientes.contains(nueva))
			recientes.remove(nueva);
		else if (recientes.size() > 10)
			recientes.removeLast();
		recientes.addFirst(nueva);
	}

	public String getRecientesId() {
		String ids = "";
		for (Cancion cancion : recientes) {
			ids += cancion.getId() + " ";
		}
		return ids;
	}

}
