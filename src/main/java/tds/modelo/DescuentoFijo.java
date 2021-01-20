package tds.modelo;

import java.time.LocalDate;

// Descuento del 20% entre el 1/10/2020 y el 1/3/2021
public class DescuentoFijo extends Descuento {

	private LocalDate finicio = LocalDate.of(2020, 10, 1);
	private LocalDate ffin = LocalDate.of(2021, 3, 1);

	public DescuentoFijo() {
		super();
		porcentaje = 20;
	}

	public double calcDescuento(double precio) {
		return precio * (1 - porcentaje / 100);
	}

	public boolean isAplicableDescuento() {
		return LocalDate.now().isAfter(finicio) && LocalDate.now().isBefore(ffin);
	}

}
