package ar.com.untref.imagenes.procesamiento;

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
}
