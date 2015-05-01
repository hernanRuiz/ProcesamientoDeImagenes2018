package ar.com.untref.imagenes.modelo;

import java.awt.image.BufferedImage;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.enums.FormatoDeImagen;

public class Imagen {

	private BufferedImage imagen;
	private FormatoDeImagen formato;
	private String nombre;
	private int[][] matrizRojos;
	private int[][] matrizAzules;
	private int[][] matrizVerdes;

	
	public Imagen(){};
	
	public Imagen(BufferedImage imagen, FormatoDeImagen formato, String nombre){
		
		this.imagen = imagen;
		this.formato = formato;
		this.nombre = nombre;
	}
	
	public Imagen(BufferedImage imagen, FormatoDeImagen formato, String nombre, int[][] matrizRojos, int[][] matrizVerdes, int[][] matrizAzules){
		
		this.imagen = imagen;
		this.formato = formato;
		this.nombre = nombre;
		this.matrizRojos = matrizRojos;
		this.matrizAzules = matrizAzules;
		this.matrizVerdes = matrizVerdes;
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

	public int[][] getMatriz(Canal canal) {
		
		switch (canal) {
		case ROJO:
			
			return matrizRojos;
		case AZUL:
			
			return matrizAzules;
		default:
			
			return matrizVerdes;
		}	
	}

	public void setMatriz(int[][] matriz, Canal canal) {
		
		switch (canal) {
		case ROJO:
			
			this.matrizRojos = matriz;
			break;
		case AZUL:
			
			this.matrizAzules = matriz;
			break;
		default:
			
			this.matrizVerdes = matriz;
			break;
		}
	}
	
}
