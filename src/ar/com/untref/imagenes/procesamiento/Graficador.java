package ar.com.untref.imagenes.procesamiento;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Graficador {

	public static BufferedImage crearImagenConCuadradoEnElCentro(int ancho, int alto, int longitudDelCuadrado) {
		
		BufferedImage bufferedImage = new BufferedImage(ancho, alto, BufferedImage.TYPE_BYTE_BINARY);

		int posicionInicial = (ancho/2 -1) - (longitudDelCuadrado/2);
		for (int incrementoEnX=0; incrementoEnX < longitudDelCuadrado; incrementoEnX++){
			for (int incrementoEnY=0; incrementoEnY < longitudDelCuadrado; incrementoEnY++){
				
				bufferedImage.setRGB(posicionInicial+incrementoEnX, posicionInicial + incrementoEnY, Color.WHITE.getRGB());
			}
		}

		return bufferedImage;
	}
	
	public static BufferedImage crearImagenConCirculoEnElMedio(int ancho, int alto, int radioDelCirculo) {
		
		BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_BINARY);

		for (int i = 0; i < ancho; i++) {
			for (int j = 0; j < alto; j++) {
				bufferedImage.setRGB(i, j, 255);
			}
		}
		
		Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(Color.WHITE);
        
        int posicionInicial = (ancho/2) - (radioDelCirculo);
        graphics.fillOval(posicionInicial, posicionInicial, radioDelCirculo*2, radioDelCirculo*2);

		return bufferedImage;
	}
	
	public static BufferedImage crearImagenConDegradeDeGrises(int ancho, int alto) {
		BufferedImage bufferedImage = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < ancho; i++) {
			for (int j = 0; j < alto; j++) {
				bufferedImage.setRGB(i, j, ColorManager.convertirRgbAEscalaDeGrises(j, j, j));
			}
		}

		return bufferedImage;
	}
	
public static BufferedImage crearImagenConDegradeColor(int ancho, int alto) {
		
		BufferedImage bufferedImage = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
		

		int red = 255;
		int green = 0;
		int blue = 0;
		Color color = ColorManager.setColorRGB(red, green, blue);
		
		for (int i = 0; i < ancho; i++) {
			for (int j = 0; j < alto; j++) {
				bufferedImage.setRGB(i, j, color.getRGB());
			}
		}
		
		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				Color colorModificado = ColorManager.setColorRGB(red, i, blue);
				bufferedImage.setRGB(j, i, colorModificado.getRGB());
			}
		}
		
		return bufferedImage;
	}
	
	public static int calcularPromedioNivelesDeGris(int alto, int ancho){
		BufferedImage bufferedImage = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
		
		int acumulador = 0;
		for (int i = 0; i < ancho; i++) {
			for (int j = 0; j < alto; j++) {
				Color color = new Color(bufferedImage.getRGB(i, j));
				acumulador += color.getRed();
			}
		}
		int cantidadPixeles = ColorManager.contarNivelesDeGris(alto, ancho);
		int promedio = (acumulador / cantidadPixeles);
		return promedio;
	}

	
}
