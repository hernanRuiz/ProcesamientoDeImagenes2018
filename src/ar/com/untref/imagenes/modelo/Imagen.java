package ar.com.untref.imagenes.modelo;

import java.awt.image.BufferedImage;

import ar.com.untref.imagenes.enums.FormatoDeImagen;

public class Imagen {

	private BufferedImage imagen;
	private FormatoDeImagen formato;
	private String nombre;
	
	public Imagen(){};
	
	public Imagen(BufferedImage imagen, FormatoDeImagen formato, String nombre){
		
		this.imagen = imagen;
		this.formato = formato;
		this.nombre = nombre;
	}

	public BufferedImage getBufferedImage() {
		return imagen;
	}

	public void setBufferedImage(BufferedImage imagen) {
		this.imagen = imagen;
	}

	public FormatoDeImagen getFormato() {
		return formato;
	}

	public void setFormato(FormatoDeImagen formato) {
		this.formato = formato;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
