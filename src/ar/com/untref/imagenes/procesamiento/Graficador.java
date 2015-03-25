package ar.com.untref.imagenes.procesamiento;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Graficador {

	public static BufferedImage crearImagenConCuadradoEnElCentro(int ancho, int alto, int longitudDelCuadrado) {
		
		BufferedImage bufferedImage = new BufferedImage(ancho, alto, BufferedImage.TYPE_BYTE_BINARY);

		for (int i = 0; i < ancho; i++) {
			for (int j = 0; j < alto; j++) {
				bufferedImage.setRGB(i, j, 255);
			}
		}
		
		int posicionInicial = (ancho/2 -1) - (longitudDelCuadrado/2);
		for (int incrementoEnX=0; incrementoEnX < longitudDelCuadrado; incrementoEnX++){

			for (int incrementoEnY=0; incrementoEnY < longitudDelCuadrado; incrementoEnY++){
				
				bufferedImage.setRGB(posicionInicial+incrementoEnX, posicionInicial + incrementoEnY, -1);
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
	
}
