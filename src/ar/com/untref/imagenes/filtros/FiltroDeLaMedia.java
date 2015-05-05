package ar.com.untref.imagenes.filtros;

import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

import ar.com.untref.imagenes.modelo.Imagen;

public class FiltroDeLaMedia {

	public static Imagen aplicarFiltroDeLaMedia(Imagen imagenOriginal, int longitudMascara) {

		float[][] mascara = generarMascaraDeLaMedia(longitudMascara);

		BufferedImage im = new BufferedImage(imagenOriginal.getBufferedImage()
				.getWidth(), imagenOriginal.getBufferedImage().getHeight(),
				imagenOriginal.getBufferedImage().getType());
		
		Imagen imagenFiltrada = new Imagen(im, imagenOriginal.getFormato(),
				imagenOriginal.getNombre());

		int width = mascara.length;
		int height = mascara[0].length;
		int tam = width * height;
		float filtroK[] = new float[tam];

		// Creamos el filtro - Se pasa de una matriz cuadrada (vector de 2
		// dimensiones) a un vector lineal
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				filtroK[i * width + j] = mascara[i][j];
			}
		}

		// Creamos la operación de convolución.
		Kernel kernel = new Kernel(width, height, filtroK);
		Filtro filtro = new Filtro(kernel);

		// Aplicamos el filtro
		filtro.filter(imagenOriginal, imagenFiltrada);

		return imagenFiltrada;
	}

	private static float[][] generarMascaraDeLaMedia(int longMascara) {

		float[][] mascara = new float[longMascara][longMascara];

		for (int j = 0; j < longMascara; ++j) {
			for (int i = 0; i < longMascara; ++i) {
				mascara[i][j] = 1f/Float.valueOf(longMascara*longMascara);
			}
		}

		return mascara;
	}

}
