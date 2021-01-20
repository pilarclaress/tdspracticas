package tds.modelo;

// Creamos esta clase para seguir el patrón estrategia al implementar los descuentos

public class ContextoDescuento {
	private Descuento estrategiaDescuento;

	// Precio de una cuenta premium
	private static final double PRECIOPREMIUM = 20;

	public ContextoDescuento() {
		estrategiaDescuento = null;
	}

	public void setDescuento(Descuento descuento) {
		estrategiaDescuento = descuento;
	}

	public double getPorcentajeDescuento() {
		if (estrategiaDescuento != null)
			return estrategiaDescuento.getPorcentaje();
		return 0;
	}

	public double calcularPrecio() {
		if (estrategiaDescuento != null)
			return estrategiaDescuento.calcDescuento(PRECIOPREMIUM);
		return PRECIOPREMIUM;
	}
}
