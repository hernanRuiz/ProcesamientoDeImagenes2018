package ar.com.untref.imagenes.ruido;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneradorDeRuido {

	static float[][] matrizRojos;
	static float[][] matrizVerdes;
	static float[][] matrizAzules;

	// 8 - A - Generador de Aleatorios para Ruido de Gauss
	private static double[] generadorFuncionesAleatoriasDeGauss() {
		double x1, x2, y1, y2;
		Random numero1 = new Random();
		Random numero2 = new Random();

		x1 = 0;
		x2 = 0;

		do
			x1 = numero1.nextGaussian();
		while (x1 <= 0 | x1 > 1); // x1 no puede ser cero ni mayor a 1

		do
			x2 = numero2.nextGaussian();
		while (x2 <= 0 | x2 > 1); // x2 no puede ser cero ni mayor a 1

		y1 = Math.sqrt(-2 * Math.log(x1)) * Math.cos(2 * Math.PI * x2);
		y2 = Math.sqrt(-2 * Math.log(x1)) * Math.sin(2 * Math.PI * x2);

		double[] resultados = new double[2];
		resultados[0] = y1;
		resultados[1] = y2;

		return resultados;
	}

	// Aplica alguna de las 2 funciones aleatorias y a su vez calcula los Máximos y mínimos para poder generar el ruido Gaussiano
	private static float[] aplicarFuncionAleatoriaYObtenerMaximosYMinimos(BufferedImage bufferedImage, int sigma, int mu) {

		float rojoMin;
		float rojoMax;
		float verdeMin;
		float verdeMax;
		float azulMin;
		float azulMax;

		int rojo = 0;
		int verde = 0;
		int azul = 0;

		Random random = new Random();
		int nrows = bufferedImage.getWidth();
		int ncols = bufferedImage.getHeight();
		matrizRojos = new float[nrows][ncols];
		matrizVerdes = new float[nrows][ncols];
		matrizAzules = new float[nrows][ncols];

		rojoMin = 0; 
		rojoMax = 255;
		verdeMin = 0; 
		verdeMax = 255;
		azulMin = 0;
		azulMax = 255;

		for (int f = 0; f < nrows; f++) {
			for (int g = 0; g < ncols; g++) {

				double[] funcionesAleatorias = generadorFuncionesAleatoriasDeGauss();
				Color colorActual = new Color(bufferedImage.getRGB(f, g));

				boolean elegirFormula1 = random.nextBoolean();
				rojo = (colorActual.getRed() + (int) (((elegirFormula1 ? funcionesAleatorias[0]: funcionesAleatorias[1]) * sigma) + mu));
				verde = (colorActual.getGreen() + (int) (((elegirFormula1 ? funcionesAleatorias[0]: funcionesAleatorias[1]) * sigma) + mu));
				azul = (colorActual.getBlue() + (int) (((elegirFormula1 ? funcionesAleatorias[0]: funcionesAleatorias[1]) * sigma) + mu));

				matrizRojos[f][g] = rojo;
				matrizVerdes[f][g] = verde;
				matrizAzules[f][g] = azul;

				if (rojoMin > rojo) {
					rojoMin = rojo;
				}

				if (rojoMax < rojo) {
					rojoMax = rojo;
				}

				if (verdeMin > verde) {
					verdeMin = verde;
				}

				if (verdeMax < verde) {
					verdeMax = verde;
				}

				if (azulMin > azul) {
					azulMin = azul;
				}

				if (azulMax < azul) {
					azulMax = azul;
				}

			}

		}

		float[] maximosYMinimos = new float[6];
		maximosYMinimos[0] = rojoMin;
		maximosYMinimos[1] = rojoMax;
		maximosYMinimos[2] = verdeMin;
		maximosYMinimos[3] = verdeMax;
		maximosYMinimos[4] = azulMin;
		maximosYMinimos[5] = azulMax;

		return maximosYMinimos;
	}

	// 10 - A - Generador de ruido de Gauss
	public static BufferedImage generarRuidoGauss(BufferedImage bufferedImage,
			int sigma, int mu) {

		int nrows, ncols;

		float rojoMin;
		float rojoMax;
		float verdeMin;
		float verdeMax;
		float azulMin;
		float azulMax;

		BufferedImage imagenConRuido;

		nrows = bufferedImage.getWidth();
		ncols = bufferedImage.getHeight();
		imagenConRuido = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);

		float[] maximosYMinimos = aplicarFuncionAleatoriaYObtenerMaximosYMinimos(bufferedImage, sigma, mu);
		rojoMin = maximosYMinimos[0];
		rojoMax = maximosYMinimos[1];
		verdeMin = maximosYMinimos[2];
		verdeMax = maximosYMinimos[3];
		azulMin = maximosYMinimos[4];
		azulMax = maximosYMinimos[5];

		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {


				int rojoTransformado = (int) ((((255f) / (rojoMax - rojoMin)) * matrizRojos[i][j]) - ((rojoMin * 255f) / (rojoMax - rojoMin)));
				int verdeTransformado = (int) (((255f / (verdeMax - verdeMin)) * matrizVerdes[i][j]) - ((verdeMin * 255f) / (verdeMax - verdeMin)));
				int azulTransformado = (int) (((255f / (azulMax - azulMin)) * matrizAzules[i][j]) - ((azulMin * 255f) / (azulMax - azulMin)));

				Color colorModificado = new Color(rojoTransformado, verdeTransformado, azulTransformado);
				imagenConRuido.setRGB(i, j, colorModificado.getRGB());
			}
		}
		
		return imagenConRuido;
	}

	// 8 - C - Aleatorios para ruido Exponencial
	private static float generadorAleatoriosExponencial(int lambda) {

		float y;
		float x;

		Random numero = new Random();
		x = 0;
		do
			x = (float) numero.nextGaussian();
		while (x <= 0 | x > 1); // x no puede ser cero ni mayor a 1

		
		y = (float) ((Math.log10(-x + 1)) / ((-1) * Float.valueOf(lambda)));

		return y;
	}

	// Máximos para ruido Exponencial
	private static float[] obtenerMaximosRuidoExponencial(BufferedImage bufferedImage, int lambda){
		
		float rojoMax;
		float verdeMax;
		float azulMax;
		
		int rojo = 0;
		int verde = 0;
		int azul = 0;
		
		int nrows = bufferedImage.getWidth();
		int ncols = bufferedImage.getHeight();
		matrizRojos = new float[nrows][ncols];
		matrizVerdes = new float[nrows][ncols];
		matrizAzules = new float[nrows][ncols];
		
		rojoMax = 255; 
		verdeMax = 255; 
		azulMax = 255;
		
		for (int f = 0; f < nrows; f++) {
			for (int g = 0; g < ncols; g++) {
	
				float funcionAleatoria = generadorAleatoriosExponencial(lambda);
				Color colorActual = new Color(bufferedImage.getRGB(f, g));
				rojo = (int)(colorActual.getRed() * funcionAleatoria);
				verde = (int)(colorActual.getGreen() * funcionAleatoria);
				azul = (int)(colorActual.getBlue() * funcionAleatoria);
				
				matrizRojos[f][g] = rojo;
				matrizVerdes[f][g] = verde;
				matrizAzules[f][g] = azul;
		
				if(rojoMax<rojo){
					rojoMax = rojo;						
				}
		
				if(verdeMax<verde){
					verdeMax = verde;						
				}
				
				if(azulMax<azul){
					azulMax = azul;						
				}
			}
		}
		float[] maximos = new float[6];
		maximos[0] = rojoMax;
		maximos[1] = verdeMax;
		maximos[2] = azulMax;
		
		return maximos;
	}

	// 10 - C - Generador de ruido Exponencial multiplicativo
	public static BufferedImage generarRuidoExponencialMultiplicativo(BufferedImage bufferedImage, int lambda) {

		int nrows, ncols;
		BufferedImage imagenConRuido;
		nrows = bufferedImage.getWidth();
		ncols = bufferedImage.getHeight();
		imagenConRuido = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);

		float rojoMax;
		float verdeMax;
		float azulMax;
		
		float[] maximos = obtenerMaximosRuidoExponencial(bufferedImage, lambda);
		rojoMax = maximos[0];
		verdeMax = maximos[1];
		azulMax = maximos[2];
		
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {

				int rojoTransformado = (int) ((255f / (Math.log10(rojoMax))) * Math.log10(1 + matrizRojos[i][j]));
				int verdeTransformado = (int) ((255f / (Math.log10(verdeMax))) * Math.log10(1 + matrizVerdes[i][j]));
				int azulTransformado = (int) ((255f / (Math.log10(azulMax))) * Math.log10(1 + matrizAzules[i][j]));

				Color colorModificado = new Color(rojoTransformado, verdeTransformado, azulTransformado);
				imagenConRuido.setRGB(i, j, colorModificado.getRGB()); // Add noise to pixel
			}
		}
		return imagenConRuido;
	}

	
	
	// 8 - B - Generador de aleatorios para ruido de Rayleigh
	private static float generadorAleatoriosRayleigh(int phi) {

		float x, y;
		Random numero = new Random();
		x = 0;
		do
			x = (float) numero.nextGaussian();
		while (x <= 0 | x >= 1); // x no puede ser cero ni mayor a uno

		y = (float) (Float.valueOf(phi) * (Math.sqrt((-2) * Math.log(1-x))));
		return y;
	}

	
	// Máximos para ruido Rayleigh multiplicativo
	private static float[] obtenerMaximosRuidoRayleigh(BufferedImage bufferedImage, int phi){
		
		float rojoMax;
		float verdeMax;
		float azulMax;
		
		int rojo = 0;
		int verde = 0;
		int azul = 0;
		
		int nrows = bufferedImage.getWidth();
		int ncols = bufferedImage.getHeight();
		matrizRojos = new float[nrows][ncols];
		matrizVerdes = new float[nrows][ncols];
		matrizAzules = new float[nrows][ncols];
		
		rojoMax = 255; 
		verdeMax = 255; 
		azulMax = 255;
		
		for (int f = 0; f < nrows; f++) {
			for (int g = 0; g < ncols; g++) {
	
				float funcionAleatoria = generadorAleatoriosRayleigh(phi);
				Color colorActual = new Color(bufferedImage.getRGB(f, g));
				rojo = (int)(colorActual.getRed() * funcionAleatoria);
				verde = (int)(colorActual.getGreen() * funcionAleatoria);
				azul = (int)(colorActual.getBlue() * funcionAleatoria);
				
				matrizRojos[f][g] = rojo;
				matrizVerdes[f][g] = verde;
				matrizAzules[f][g] = azul;
		
				if(rojoMax<rojo){
					rojoMax = rojo;						
				}
		
				if(verdeMax<verde){
					verdeMax = verde;						
				}
				
				if(azulMax<azul){
					azulMax = azul;						
				}
			}
		}
		float[] maximos = new float[6];
		maximos[0] = rojoMax;
		maximos[1] = verdeMax;
		maximos[2] = azulMax;
		
		return maximos;
	}	
	
	
	// 10 - B - Generador de ruido de Rayleigh
	public static BufferedImage generarRuidoRayleighMultiplicativo(BufferedImage bufferedImage, int phi) {

		int nrows, ncols;
		BufferedImage imagenConRuido;
		nrows = bufferedImage.getWidth();
		ncols = bufferedImage.getHeight();
		imagenConRuido = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);

		float rojoMax;
		float verdeMax;
		float azulMax;
		
		float[] maximos = obtenerMaximosRuidoRayleigh(bufferedImage, phi);
		rojoMax = maximos[0];
		verdeMax = maximos[1];
		azulMax = maximos[2];
		
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {

				int rojoTransformado = (int) ((255f / (Math.log10(rojoMax+1))) * Math.log10(1 + matrizRojos[i][j]));
				int verdeTransformado = (int) ((255f / (Math.log10(verdeMax+1))) * Math.log10(1 + matrizVerdes[i][j]));
				int azulTransformado = (int) ((255f / (Math.log10(azulMax+1))) * Math.log10(1 + matrizAzules[i][j]));

				Color colorModificado = new Color(rojoTransformado, verdeTransformado, azulTransformado);
				imagenConRuido.setRGB(i, j, colorModificado.getRGB()); // Add noise to pixel
			}
		}
		return imagenConRuido;
	}

	//Generador de ruido Salt and Pepper
	public static BufferedImage generarRuidoSaltAndPepper(BufferedImage imagen, int porcentajeDePixelesAContaminar) {

		int nrows, ncols;

		BufferedImage imagenConRuido;
		nrows = imagen.getWidth();
		ncols = imagen.getHeight();
		imagenConRuido = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);
		double densidad = ((nrows * ncols) * porcentajeDePixelesAContaminar) / 100;

		double p0 = 0.2;
		double p1 = 0.8;

		ArrayList<int[]> listaDeCoordenadas = new ArrayList<int[]>();
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				int[] coordenada = new int[2];
				coordenada[0] = i;
				coordenada[1] = j;
				listaDeCoordenadas.add(coordenada);
				imagenConRuido.setRGB(i, j, imagen.getRGB(i, j));
			}
		}

		Collections.shuffle(listaDeCoordenadas);

		for (int h = 0; h <= densidad; h++) {

			Random numero = new Random();
			double x = numero.nextGaussian();
			int[] vector1 = listaDeCoordenadas.get(0);

			// tomo el color de la coordenada obtenida
			Color color = new Color(imagen.getRGB(vector1[0], vector1[1]));
			listaDeCoordenadas.remove(0);

			int rojo = color.getRed();
			int verde = color.getGreen();
			int azul = color.getBlue();

			// aplico ruido
			if (p1 > p0) {
				if (x <= p0) {
					rojo = 0;
					verde = 0;
					azul = 0;
				} else if (x > p1) {
					rojo = 255;
					verde = 255;
					azul = 255;
				}
			}

			Color colorModificado = new Color(rojo, verde, azul);
			imagenConRuido.setRGB(vector1[0], vector1[1],
					colorModificado.getRGB());
		}

		return imagenConRuido;
	}
}
