package ar.com.untref.imagenes.ruido;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.ColorManager;

public class GeneradorDeRuido {
	
	//Para la transformada lineal, no estoy seguro si se usan en la logarítmica
	private static int[] obtenerMaximosYMinimos(BufferedImage bufferedImage){
		
		int nrows = bufferedImage.getWidth();
		int ncols = bufferedImage.getHeight();
		int rojoMin = 255;
		int rojoMax = 0;
		int verdeMin = 255;
		int verdeMax = 0;
		int azulMin = 255;
		int azulMax = 0;
		
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				Color color = new Color(bufferedImage.getRGB(i, j));
				
				if(rojoMin > color.getRed()){
					rojoMin = color.getRed();
				}
				
				if(rojoMax<color.getRed()){
					rojoMax = color.getRed();						
				}
				
				if(verdeMin > color.getGreen()){
					verdeMin = color.getGreen();
				}
				
				if(verdeMax<color.getGreen()){
					verdeMax = color.getGreen();						
				}
				
				if(azulMin > color.getBlue()){
					azulMin = color.getBlue();
				}
				
				if(azulMax<color.getBlue()){
					azulMax = color.getBlue();						
				}
				
			}
		}
		
		int[] maximosYMinimos = new int[6];
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
		
		x1 = 0;
		x2 = 0;
		
		do
			x1 = Math.random();
		while (x1 == 0); // x1 no puede ser cero
		
		x2 = Math.random();
		
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
		BufferedImage imagenConRuido;
		
		Random random = new Random();

		double[] funcionesAleatorias = new double [2];
		
		nrows = bufferedImage.getWidth();
		ncols = bufferedImage.getHeight();
		imagenConRuido = new BufferedImage(nrows, ncols, BufferedImage.TYPE_INT_RGB);
		int[] maximosYMinimos = obtenerMaximosYMinimos(bufferedImage);
		
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {

				funcionesAleatorias = generadorFuncionesAleatoriasDeGauss();
				Color color = new Color(bufferedImage.getRGB(i, j));
				
				boolean elegirFormula1= random.nextBoolean();
				int rojo = (color.getRed() + (int) (((elegirFormula1 ? funcionesAleatorias[0]: funcionesAleatorias[1]) * sigma) + mu));
				int verde =(color.getGreen() + (int) (((elegirFormula1 ? funcionesAleatorias[0]: funcionesAleatorias[1]) * sigma) + mu));
				int azul = (color.getBlue() + (int) (((elegirFormula1 ? funcionesAleatorias[0]: funcionesAleatorias[1]) * sigma) + mu));
				
				int rojoTransformado = (((255)/(maximosYMinimos[1]-maximosYMinimos[0]))*rojo)-((maximosYMinimos[0]*255)/(maximosYMinimos[1]-maximosYMinimos[0])); 
				int verdeTransformado = (((255)/(maximosYMinimos[3]-maximosYMinimos[2]))*verde)-((maximosYMinimos[2]*255)/(maximosYMinimos[3]-maximosYMinimos[2]));
				int azulTransformado = (((255)/(maximosYMinimos[5]-maximosYMinimos[4]))*azul)-((maximosYMinimos[4]*255)/(maximosYMinimos[5]-maximosYMinimos[4]));				
				
				//TODO: Truncar o transformar?
				/*if (rojo < 0) rojo = 0;
				else if (rojo > 255) rojo = 255;

				if (verde < 0) verde = 0;
				else if (verde > 255) verde = 255;

				if (azul < 0) azul = 0;
				else if (azul > 255) azul = 255;*/

				Color colorModificado = ColorManager.setColorRGB(rojoTransformado, verdeTransformado, azulTransformado);
				imagenConRuido.setRGB(i, j, colorModificado.getRGB());				
			}
		}
		return imagenConRuido;
	}
		

	//8 - C
	private static double generadorAleatoriosExponencial(int lambda){
		
		double x, y;

		x = 0;
		do
			x = Math.random();
		while (x == 0); // x no puede ser cero
	
		y = (-1/lambda)*(Math.log(x+1));
		
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

			x = 0;
			do
				x = Math.random();
			while (x == 0); // x no puede ser cero
		
			y = phi*(Math.sqrt((-2)*Math.log(1-x)));
			return y;
		}
		
		
		//10 - B
		public static BufferedImage generarRuidoRayleighMultiplicativo(BufferedImage bufferedImage, int phi) {
			
			int nrows, ncols;
			BufferedImage imagenConRuido;
			//int[] maximosYMinimos = obtenerMaximosYMinimos(bufferedImage);
					
			nrows = bufferedImage.getWidth();
			ncols = bufferedImage.getHeight();
			imagenConRuido = new BufferedImage(nrows, ncols, BufferedImage.TYPE_INT_RGB);
			
			for (int i = 0; i < nrows; i++) {
				for (int j = 0; j < ncols; j++) {

					double y = generadorAleatoriosRayleigh(phi);
					
					Color color = new Color(bufferedImage.getRGB(i, j));
					
					int rojo = (int)(Math.round(color.getRed() * y));
					int verde = (int)(Math.round(color.getGreen() * y));
					int azul = (int)(Math.round(color.getBlue() * y));

					int transformadaRojo = ((int)(Math.round(255/Math.log(256))* Math.round(Math.log(1+rojo))));
					int transformadaVerde = ((int)(Math.round(255/Math.log(256))* Math.round(Math.log(1+verde))));
					int transformadaAzul = ((int)(Math.round(255/Math.log(256))* Math.round(Math.log(1+azul))));
					
					
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

	
	
	public static Imagen generarRuidoSaltAndPepper(Imagen imagenOriginal, int porcentajeDePixelesAContaminar){
		
		int nrows, ncols; 
		Imagen imagenConRuido;
		nrows = imagenOriginal.getBufferedImage().getWidth();
		ncols = imagenOriginal.getBufferedImage().getHeight();
		
		imagenConRuido = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre());
		
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
			}
		}
	
		Collections.shuffle(listaDeCoordenadas);
		
		for (int h=0; h<=densidad; h++){
		
			double x = Math.random();
			int[] vector1 = listaDeCoordenadas.get(0);

			//tomo el color de la coordenada obtenida
			Color color = new Color(imagenOriginal.getBufferedImage().getRGB(vector1[0], vector1[1]));
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
			imagenConRuido.getBufferedImage().setRGB(vector1[0], vector1[1], colorModificado.getRGB());
		}
		
		return imagenConRuido;
	}
}
