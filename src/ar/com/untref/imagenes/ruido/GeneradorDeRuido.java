package ar.com.untref.imagenes.ruido;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GeneradorDeRuido {

	private static float[][] matrizRojos;
	private static float[][] matrizVerdes;
	private static float[][] matrizAzules;

	//Generador de Aleatorios para Ruido de Gauss
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

	//Aplica alguna de las 2 funciones aleatorias y a su vez calcula los Máximos y mínimos para poder generar el ruido Gaussiano
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

	//Generador de ruido de Gauss
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

	
	public static float[][] getMatrizRojos(){
		return matrizRojos;
	}
	
	public static float[][] getMatrizVerdes(){
		return matrizVerdes;
	}
	
	public static float[][] getMatrizAzules(){
		return matrizAzules;
	}
	
}
