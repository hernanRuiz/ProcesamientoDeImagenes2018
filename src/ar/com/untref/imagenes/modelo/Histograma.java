package ar.com.untref.imagenes.modelo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

public class Histograma {

	public static float[] calcularHistogramaRojo(BufferedImage imagen) {

		Color color;

		float histograma[] = new float[256];
		BigDecimal b = new BigDecimal("1");
		BigDecimal totalPixeles = new BigDecimal(imagen.getHeight()*imagen.getWidth());
		BigDecimal porcentual = b.divide(totalPixeles, 8, BigDecimal.ROUND_HALF_UP);

		for (int i = 0; i < imagen.getWidth(); i++) {

			for (int j = 0; j < imagen.getHeight(); j++) {

				color = new Color(imagen.getRGB(i, j));
				histograma[color.getRed()] += porcentual.floatValue();
			}
		}
		return histograma;
	}
	
	public static float[] calcularHistogramaDelCanal(float[][] matriz) {

		int ancho = matriz[0].length;
		int alto = matriz.length;
		
		int maximo = 0;
		
		for (int i = 0; i < ancho; i++) {

			for (int j = 0; j < alto; j++) {
				if(matriz[j][i] > maximo){
					maximo = (int)matriz[j][i];
				}
			}	
		}
		
		float histograma[] = new float[maximo+1];
		BigDecimal b = new BigDecimal("1");
		BigDecimal totalPixeles = new BigDecimal(ancho*alto);
		BigDecimal porcentual = b.divide(totalPixeles, 8, BigDecimal.ROUND_HALF_UP);

		for (int i = 0; i < ancho; i++) {

			for (int j = 0; j < alto; j++) {

				histograma[(int)matriz[j][i]] += porcentual.floatValue();
			}
		}
		return histograma;
	}
	
	public static float[] calcularHistogramaAzul(BufferedImage imagen) {

		Color color;
		float histograma[] = new float[256];
		BigDecimal b = new BigDecimal("1");
		BigDecimal totalPixeles = new BigDecimal(imagen.getHeight()*imagen.getWidth());
		BigDecimal porcentual = b.divide(totalPixeles, 8, BigDecimal.ROUND_HALF_UP);
		
		for (int i = 0; i < imagen.getWidth(); i++) {

			for (int j = 0; j < imagen.getHeight(); j++) {

				color = new Color(imagen.getRGB(i, j));
				histograma[color.getBlue()] += porcentual.floatValue();
			}
		}
		return histograma;
	}
	
	public static float[] calcularHistogramaVerde(BufferedImage imagen) {

		Color color;
		float histograma[] = new float[256];
		BigDecimal b = new BigDecimal("1");
		BigDecimal totalPixeles = new BigDecimal(imagen.getHeight()*imagen.getWidth());
		BigDecimal porcentual = b.divide(totalPixeles, 8, BigDecimal.ROUND_HALF_UP);
		
		for (int i = 0; i < imagen.getWidth(); i++) {

			for (int j = 0; j < imagen.getHeight(); j++) {

				color = new Color(imagen.getRGB(i, j));
				histograma[color.getGreen()] += porcentual.floatValue();
			}
		}
		return histograma;
	}
}
