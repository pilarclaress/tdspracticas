package tds.modelo;

public abstract class Descuento {
	protected double porcentaje = 0;

	public Descuento() {
	}

	public abstract double calcDescuento(double precio);

	public double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public abstract boolean isAplicableDescuento();
}