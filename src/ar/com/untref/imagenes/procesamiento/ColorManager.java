package ar.com.untref.imagenes.procesamiento;

import java.awt.Color;

public class ColorManager {

	
	public static String getHexaDeColorRGB(int rgb){
		
		return String.format("#%06X", (0xFFFFFF & rgb));
	}
	
	public static int convertirRgbAEscalaDeGrises(int red, int green, int blue) {
		
		int redConverter = (int) (red * 0.299);
		int greenConverter = (int) (green * 0.587);
		int blueConverter = (int) (blue * 0.114);
		
		//EL gris se dibuja con los valores r g y b iguales
		int gray = (int)(redConverter + greenConverter + blueConverter);
		
		return 0xff000000 + (gray<<16) + (gray<<8) + gray;
	}
	
	public static int contarNivelesDeGris(int alto, int ancho){
		int contador =0;
		for (int i = 0; i < ancho; i++) {
			for (int j = 0; j < alto; j++) {
				contador++;
			}
		}
		return contador;
	}
	
	public static Color setColorRGB(int red, int green, int blue) {
		
		Color color = new Color((int)((red<<16) | (green<<8) | blue));
	
		return color;
	}
	
	public static int colorToRGB(int red, int green, int blue) {
		int newPixel = 0;
		
		
		newPixel += newPixel << 8;
		newPixel += red;
		newPixel = newPixel << 8;
		newPixel += green;
		newPixel = newPixel << 8;
		newPixel += blue;
		
		return newPixel;
	}
}
