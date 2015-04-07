package ar.com.untref.imagenes.filtros;

import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

import ar.com.untref.imagenes.modelo.Imagen;

public class FiltroPasaBajos {

	public static Imagen aplicarFiltroPasaBajos(Imagen imagenOriginal,
			int longitudMascara) {

		float[][] mascara = generarMascaraPasaBajos(longitudMascara);

		BufferedImage im = new BufferedImage(imagenOriginal.getBufferedImage()
				.getWidth(), imagenOriginal.getBufferedImage().getHeight(),
				imagenOriginal.getBufferedImage().getType());

		Imagen imagenFiltrada = new Imagen(im, imagenOriginal.getFormato(),
				imagenOriginal.getNombre());

		int width = mascara.length;
		int height = mascara[0].length;
		int tam = width * height;
		float filtroK[] = new float[tam];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				filtroK[i * width + j] = mascara[i][j];
			}
		}

		Kernel kernel = new Kernel(width, height, filtroK);
		Filtro filtro = new Filtro(kernel);

		// Aplicamos el filtro
		filtro.filter(imagenOriginal.getBufferedImage(),
				imagenFiltrada.getBufferedImage());

		return imagenFiltrada;
	}

	private static float[][] generarMascaraPasaBajos(int longMascara) {

		float[][] mascara = new float[longMascara][longMascara];

		for (int j = 0; j < longMascara; ++j) {
			for (int i = 0; i < longMascara; ++i) {
				
				if (j == longMascara/2 && i == longMascara/2){
					
					mascara[i][j] = (longMascara * longMascara)-1;
				} else {
					
					mascara[i][j] = -1;
				}
			}
		}

		return mascara;
	}
}