package ar.com.untref.imagenes.procesamiento;

public class ColorManager {

	
	public static String getHexaDeColorRGB(int rgb){
		
		return String.format("#%06X", (0xFFFFFF & rgb));
	}
}
