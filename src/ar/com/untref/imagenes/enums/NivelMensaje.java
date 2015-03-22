package ar.com.untref.imagenes.enums;

import javax.swing.JOptionPane;

public enum NivelMensaje {

	WARNING(JOptionPane.WARNING_MESSAGE, "Atención"), 
	ERROR(JOptionPane.ERROR_MESSAGE, "Ups!");

	private int nivelDeError;
	private String titulo;

	NivelMensaje(int nivelDeError, String mensaje) {

		this.nivelDeError = nivelDeError;
		this.titulo = mensaje;
	}

	public int getNivelDeError() {
		return nivelDeError;
	}

	public void setNivelDeError(int nivelDeError) {
		this.nivelDeError = nivelDeError;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String mensaje) {
		this.titulo = mensaje;
	}

}
