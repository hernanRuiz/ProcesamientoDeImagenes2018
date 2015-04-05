package ar.com.untref.imagenes.ruido;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneradorDeRuido {
	
	public static BufferedImage imagenATrasnsformar;
	static float[][]matrizRojos;
	static float[][]matrizVerdes;
	static float[][]matrizAzules;
	
	//Para la transformada lineal, no estoy seguro si se usan en la logarítmica
	private static float[] obtenerMaximosYMinimos(BufferedImage bufferedImage, int sigma, int mu){
		
		
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
		
		Color color = new Color(bufferedImage.getRGB(0, 0));
		rojoMin = color.getRed(); 
		rojoMax = color.getRed();
		verdeMin = color.getRed(); 
		verdeMax = color.getGreen();
		azulMin = color.getRed(); 
		azulMax = color.getBlue();

		
		for (int f = 0; f < nrows; f++) {
			for (int g = 0; g < ncols; g++) {
	
				double[] funcionesAleatorias = generadorFuncionesAleatoriasDeGauss();
				Color colorActual = new Color(bufferedImage.getRGB(f, g));
				
				
				boolean elegirFormula1= random.nextBoolean();
				rojo = (colorActual.getRed() + (int) (((elegirFormula1 ? funcionesAleatorias[0]: funcionesAleatorias[1]) * sigma) + mu));
				verde =(colorActual.getGreen() + (int) (((elegirFormula1 ? funcionesAleatorias[0]: funcionesAleatorias[1]) * sigma) + mu));
				azul = (colorActual.getBlue() + (int) (((elegirFormula1 ? funcionesAleatorias[0]: funcionesAleatorias[1]) * sigma) + mu));
				
				matrizRojos[f][g] = rojo;
				matrizVerdes[f][g] = verde;
				matrizAzules[f][g] = azul;
				
				if(rojoMin > rojo){
					rojoMin = rojo;
				}
				
				if(rojoMax<rojo){
					rojoMax = rojo;						
				}
				
				if(verdeMin > verde){
					verdeMin = verde;
				}
				
				if(verdeMax<verde){
					verdeMax = verde;						
				}
				
				if(azulMin > azul){
					azulMin = azul;
				}
				
				if(azulMax<azul){
					azulMax = azul;						
				}

			}
		
		}
		
	    float[] maximosYMinimos = new float[6];
		maximosYMinimos[0]= rojoMin;
		maximosYMinimos[1]= rojoMax;
		maximosYMinimos[2]= verdeMin;
		maximosYMinimos[3]= verdeMax;
		maximosYMinimos[4]= azulMin;
		maximosYMinimos[5]= azulMax;
		
		return maximosYMinimos;
	}
	
	
	//8 - A
	private static double[] generadorFuncionesAleatoriasDeGauss(){
		double x1, x2, y1, y2;
		Random numero1 = new Random();
		Random numero2 = new Random();
		
		x1 = 0;
		x2 = 0;
		
		do
			x1 = numero1.nextGaussian();
		while (x1 <= 0 | x1>1); // x1 no puede ser cero
		
		//x2 = numero2.nextGaussian();

		do
			x2 = numero2.nextGaussian();
		while (x2 <= 0 | x2>1); // x1 no puede ser cero
		
		
		y1 = Math.sqrt(-2 * Math.log(x1)) * Math.cos(2 * Math.PI * x2);
		y2 = Math.sqrt(-2 * Math.log(x1)) * Math.sin(2 * Math.PI * x2);
		
		double[] resultados = new double[2];
		resultados[0] = y1;
		resultados[1] = y2;
		
		return resultados;
	}

	//10 - A
	public static BufferedImage generarRuidoGauss(BufferedImage bufferedImage, int sigma, int mu) {
		
		int nrows, ncols;
		
		float rojoMin;
		float rojoMax;
		float verdeMin;
		float verdeMax;
		float azulMin;
		float azulMax;

		BufferedImage imagenConRuido;
		//int[][] matriz = ProcesadorDeImagenes.obtenerInstancia().calcularMatrizDeLaImagen(bufferedImage);
				
		nrows = bufferedImage.getWidth();
		ncols = bufferedImage.getHeight();
		imagenConRuido = new BufferedImage(nrows, ncols, BufferedImage.TYPE_INT_RGB);
		
		float[] maximosYMinimos = obtenerMaximosYMinimos(bufferedImage, sigma, mu);
		rojoMin = maximosYMinimos[0];
		rojoMax = maximosYMinimos[1];
		verdeMin = maximosYMinimos[2];
		verdeMax = maximosYMinimos[3];
		azulMin = maximosYMinimos[4];
		azulMax = maximosYMinimos[5];
		
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
	
				int rojoTransformado = (int) ((((255f)/(rojoMax-rojoMin))*matrizRojos[i][j])-((rojoMin*255f)/(rojoMax-rojoMin))); 
				int verdeTransformado = (int)(((255f/(verdeMax-verdeMin))*matrizVerdes[i][j])-((verdeMin*255f)/(verdeMax-verdeMin)));
				int azulTransformado = (int)(((255f/(azulMax-azulMin))*matrizAzules[i][j])-((azulMin*255f)/(azulMax-azulMin)));				

				Color colorModificado = new Color (rojoTransformado, verdeTransformado, azulTransformado);
				imagenConRuido.setRGB(i, j, colorModificado.getRGB());				
			}
		}
		return imagenConRuido;
	}
		

	//8 - C
	private static double generadorAleatoriosExponencial(int lambda){
		
		double x, y;
		Random numero = new Random();
		x = 0;
		do
			x = numero.nextGaussian();
		while (x == 0); // x no puede ser cero
	
		//TODO: revisar division, floats
		y = (Math.log(x-1))/((-1)*lambda);
		
		return y;
	}
	
	//10 - C
	public static BufferedImage generarRuidoExponencialMultiplicativo(BufferedImage bufferedImage, int lambda) {
		
		int nrows, ncols;
		BufferedImage imagenConRuido;
		nrows = bufferedImage.getWidth();
		ncols = bufferedImage.getHeight();
		imagenConRuido = new BufferedImage(nrows, ncols, BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {

				double y = generadorAleatoriosExponencial(lambda);
				
				Color color = new Color(bufferedImage.getRGB(i, j));
				double rojo = (color.getRed() * y);
				double verde = (color.getGreen() * y);
				double azul = (color.getBlue() * y);

				if (rojo < 0) rojo = 0;
				else if (rojo > 255) rojo = 255;

				if (verde < 0) verde = 0;
				else if (verde > 255) verde = 255;

				if (azul < 0) azul = 0;
				else if (azul > 255) azul = 255;

				Color colorModificado = new Color((int)rojo, (int)verde, (int)azul);
				imagenConRuido.setRGB(i, j, colorModificado.getRGB()); // Add noise to pixel						
			}
		}
		return imagenConRuido;
	}

	
	//8 - B
		private static double generadorAleatoriosRayleigh(int phi){
			
			double x, y;
			Random numero = new Random();
			x = 0;
			do
				x = numero.nextGaussian();
			while (x == 0); // x no puede ser cero
		
			y = (-1)*phi*(Math.sqrt((2)*Math.log(x-1)));
			return y;
		}
		
		
		private static int[] obtenerMaximosRayleigh(BufferedImage bufferedImage, int phi){
			
			int rojoMax;
			int verdeMax;
			int azulMax;
			
			int rojo = 0;
			int verde = 0;
			int azul = 0;
			
			int nrows = bufferedImage.getWidth();
			int ncols = bufferedImage.getHeight();
			
			Color color = new Color(bufferedImage.getRGB(0, 0));
			rojoMax = color.getRed();
			verdeMax = color.getGreen();
			azulMax = color.getBlue();

			for (int f = 0; f < nrows; f++) {
				for (int g = 0; g < ncols; g++) {
		
					double y = generadorAleatoriosRayleigh(phi);
					
					rojo = (int)(Math.round(color.getRed() * y));
					verde = (int)(Math.round(color.getGreen() * y));
					azul = (int)(Math.round(color.getBlue() * y));
					
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
			
		    int[] maximosYMinimos = new int[6];
			maximosYMinimos[0]= rojoMax;
			maximosYMinimos[1]= verdeMax;
			maximosYMinimos[2]= azulMax;
			
			return maximosYMinimos;
		}

		
		
		//10 - B
		public static BufferedImage generarRuidoRayleighMultiplicativo(BufferedImage bufferedImage, int phi) {
			
			int nrows, ncols;
			BufferedImage imagenConRuido;
			//int[] maximosYMinimos = obtenerMaximosYMinimos(bufferedImage);
					
			nrows = bufferedImage.getWidth();
			ncols = bufferedImage.getHeight();
			imagenConRuido = new BufferedImage(nrows, ncols, BufferedImage.TYPE_INT_RGB);
			
			int[] maximos = obtenerMaximosRayleigh(bufferedImage, phi);
			int rojoMax = maximos[0];
			int verdeMax = maximos[1];
			int azulMax = maximos[2];
			
			for (int i = 0; i < nrows; i++) {
				for (int j = 0; j < ncols; j++) {

					double y = generadorAleatoriosRayleigh(phi);
					
					Color color = new Color(bufferedImage.getRGB(i, j));
					
					int rojo = color.getRed();
					int verde = color.getGreen();
					int azul = color.getBlue();

					int transformadaRojo = (int) (255/(Math.rint(Math.log10(1+rojoMax)))*(Math.round(Math.log10(1+rojo))));
					int transformadaVerde = (int) (255/(Math.rint(Math.log10(1+verdeMax)))*(Math.round(Math.log10(1+verde))));
					int transformadaAzul = (int) (255/(Math.rint(Math.log10(1+azulMax)))*(Math.round(Math.log10(1+azul))));
					
					
					/*if (rojo < 0) rojo = 0;
					else if (rojo > 255) rojo = 255;

					if (verde < 0) verde = 0;
					else if (verde > 255) verde = 255;

					if (azul < 0) azul = 0;
					else if (azul > 255) azul = 255;*/

					Color colorModificado = new Color(transformadaRojo, transformadaVerde, transformadaAzul);
					imagenConRuido.setRGB(i, j, colorModificado.getRGB()); // Add noise to pixel						
				}
			}
			return imagenConRuido;
		}

	
	
	public static BufferedImage generarRuidoSaltAndPepper(BufferedImage imagen, int porcentajeDePixelesAContaminar){
		
		int nrows, ncols; 
		
		BufferedImage imagenConRuido;
		nrows = imagen.getWidth();
		ncols = imagen.getHeight();
		imagenConRuido = new BufferedImage(nrows, ncols, BufferedImage.TYPE_INT_RGB);
		//imagenConRuido = imagen.getBufferedImage();
		//imagenConRuido = new Imagen(imagen.getBufferedImage(), imagen.getFormato(), "Imagen Con Ruido");
		//imagenConRuido.setBufferedImage(imagen.getBufferedImage());
		double densidad = ((nrows*ncols)*porcentajeDePixelesAContaminar)/100;
		
		double p0 = 0.2;
		double p1 = 0.8;
		
		ArrayList<int[]> listaDeCoordenadas = new ArrayList<int[]>();
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				int[] coordenada = new int[2];
				coordenada[0]= i;
				coordenada[1]= j;
				listaDeCoordenadas.add(coordenada);
				imagenConRuido.setRGB(i, j, imagen.getRGB(i, j));
			}
		}
	
		Collections.shuffle(listaDeCoordenadas);
		
		for (int h=0; h<=densidad; h++){
		
			Random numero = new Random();
			double x = numero.nextGaussian();
			int[] vector1 = listaDeCoordenadas.get(0);

			//tomo el color de la coordenada obtenida
			Color color = new Color(imagen.getRGB(vector1[0], vector1[1]));
			listaDeCoordenadas.remove(0);
			
			//Color color = new Color(bufferedImage.getRGB(coordenadaFila, coordenadaColumna));			
			int rojo = color.getRed();
			int verde = color.getGreen();
			int azul = color.getBlue();
			
			//aplico ruido 
			if (p1 > p0){
				if ( x <= p0){
					rojo = 0;
			  	  	verde = 0;
			  	  	azul = 0;
				}else if (x > p1){
					rojo = 255;
					verde = 255;
					azul = 255;
				}
			}
		
			Color colorModificado = new Color(rojo, verde, azul);
			imagenConRuido.setRGB(vector1[0], vector1[1], colorModificado.getRGB());
		}
		
		return imagenConRuido;
	}
}
