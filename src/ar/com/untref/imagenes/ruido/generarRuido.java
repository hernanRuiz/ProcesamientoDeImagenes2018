package ar.com.untref.imagenes.ruido;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import ar.com.untref.imagenes.procesamiento.ColorManager;

public class generarRuido {
	
	//8 - A. Esto es tal cual a la carpeta, pero en el enunciado del tp nos pasa una distribución de 
	//y su función de densidad y nos pide hacer un generador de aleatorios en base a eso, ni idea.
	private static double[] generadorAleatoriosGauss(){
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

	//10 - A (pero dice ruido blanco, y este es Gauss a secas, el que vimos en clase).
	public static BufferedImage generarRuidoGauss(BufferedImage bufferedImage, int ro, int mu) {
		
		int nrows, ncols;
		BufferedImage imagenConRuido;
		Random random = new Random();

		double[] aleatorios = new double [2];
		
		nrows = bufferedImage.getWidth();
		ncols = bufferedImage.getHeight();
		imagenConRuido = new BufferedImage(nrows, ncols, BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {

				aleatorios = generadorAleatoriosGauss();
				Color color = new Color(bufferedImage.getRGB(i, j));
				
				int rojo = (color.getRed() + (int) (((random.nextBoolean() ? aleatorios[0]: aleatorios[1]) * ro) + mu));
				int verde = (color.getGreen() + (int) (((random.nextBoolean() ? aleatorios[0]: aleatorios[1]) * ro) + mu));
				int azul = (color.getBlue() + (int) (((random.nextBoolean() ? aleatorios[0]: aleatorios[1]) * ro) + mu));

				if (rojo < 0) rojo = 0;
				else if (rojo > 255) rojo = 255;

				if (verde < 0) verde = 0;
				else if (verde > 255) verde = 255;

				if (azul < 0) azul = 0;
				else if (azul > 255) azul = 255;

				Color colorModificado = ColorManager.setColorRGB(rojo, verde, azul);
				imagenConRuido.setRGB(i, j, colorModificado.getRGB()); // Add noise to pixel						
			}
		}
		return imagenConRuido;
	}
	
	
	//8 - A de otra forma, tomando la fórmula loca con ro y mu que viene en el enunciado, pero en
	//la carpeta no parte de ninguna fórmula. En este sentido, si la onda es despejar la formula que
	//viene por enunciado, el 8 - b nose como despejarla.
	private static double generadorAleatoriosGaussConRoYMu(int ro, int mu){
		double x, y;

		x = 0;
		do
			x = Math.random();
		while (x == 0); // x no puede ser cero
		
		//aca usé la fórmula que viene en el enunciado
		//y = (1 / (ro * Math.sqrt(2 * Math.PI))) * Math.exp(((-x - mu) * (-x - mu)) / (2 * (ro * ro))); 
		
		//aca supuse que habia que hacer lo que hizo en clase para otros ruidos, o sea, despejar esa 
		//formula
		y = (Math.sqrt(Math.log10(x*ro*Math.sqrt(2*Math.PI))*2*ro*ro)-mu);
		
		return y;
	}
	
	
	//10 - Respetando esta última forma del 8 - A, no anda
	public static BufferedImage generarRuidoGaussConRoYMu(BufferedImage bufferedImage, int ro, int mu) {
		
		int nrows, ncols;
		BufferedImage imagenConRuido;
			
		nrows = bufferedImage.getWidth();
		ncols = bufferedImage.getHeight();
		imagenConRuido = new BufferedImage(nrows, ncols, BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {

				double y = generadorAleatoriosGaussConRoYMu(mu, ro);
				
				Color color = new Color(bufferedImage.getRGB(i, j));
				int rojo = (color.getRed() + (int)y);
				int verde = (color.getGreen() + (int)y);
				int azul = (color.getBlue() + (int)y);

				if (rojo < 0) rojo = 0;
				else if (rojo > 255) rojo = 255;

				if (verde < 0) verde = 0;
				else if (verde > 255) verde = 255;

				if (azul < 0) azul = 0;
				else if (azul > 255) azul = 255;

				Color colorModificado = ColorManager.setColorRGB(rojo, verde, azul);
				imagenConRuido.setRGB(i, j, colorModificado.getRGB()); // Add noise to pixel						
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
	
		y = ((Math.log10(x/lambda))/-lambda);
		
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
				int rojo = (color.getRed() * (int)y);
				int verde = (color.getGreen() * (int)y);
				int azul = (color.getBlue() * (int)y);

				if (rojo < 0) rojo = 0;
				else if (rojo > 255) rojo = 255;

				if (verde < 0) verde = 0;
				else if (verde > 255) verde = 255;

				if (azul < 0) azul = 0;
				else if (azul > 255) azul = 255;

				Color colorModificado = ColorManager.setColorRGB(rojo, verde, azul);
				imagenConRuido.setRGB(i, j, colorModificado.getRGB()); // Add noise to pixel						
			}
		}
		return imagenConRuido;
	}

	
}
