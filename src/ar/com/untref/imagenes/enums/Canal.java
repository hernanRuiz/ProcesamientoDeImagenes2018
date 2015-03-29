package ar.com.untref.imagenes.enums;

import java.awt.Color;

public enum Canal {

	ROJO(Color.RED, "Rojo"), VERDE(Color.GREEN, "Verde"), AZUL(Color.BLUE,
			"Azul");

	private String nombre;
	private Color color;

	Canal(Color color, String nombre) {

		this.color = color;
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
