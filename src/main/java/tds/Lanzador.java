package tds;

import java.awt.EventQueue;

import tds.vista.VentanaAcceso;

public class Lanzador {
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaAcceso ventana = new VentanaAcceso();
					ventana.mostrarVentana();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}