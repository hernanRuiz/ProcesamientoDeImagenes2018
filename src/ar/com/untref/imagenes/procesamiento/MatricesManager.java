package ar.com.untref.imagenes.procesamiento;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import ar.com.untref.imagenes.enums.Canal;

public class MatricesManager {

	static float[][] matrizRojos;
	static float[][] matrizVerdes;
	static float[][] matrizAzules;
	
	
	public static void toString(int[][] m) {
		String barra = "|";
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				barra += m[i][j] + " ";
			}
			System.out.println(barra + "|");
			barra = "|";
		}
		System.out.println("\n");

	}
	
	
	public static BufferedImage obtenerImagenDeMatriz(int[][] matriz) {

		int width = matriz[0].length;
		int height = matriz.length;
		
		BufferedImage imagenResultante = null;
		imagenResultante = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {

				Color color = new Color(matriz[i][j]);
				imagenResultante.setRGB(j, i, color.getRGB());
			}
		}
		
		return imagenResultante;
	}
	
	public static BufferedImage obtenerImagenDeMatrizPGM(int[][] matriz) {

		int width = matriz[0].length;
		int height = matriz.length;

		BufferedImage imagenResultante = null;
		imagenResultante = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {

				int g = matriz[i][j];
				Color color = new Color(g);
				
				imagenResultante.setRGB(j, i, (255 << 24) | (color.getRGB() << 16) | (color.getRGB() << 8) | color.getRGB());
			}
		}
		
		return imagenResultante;
	}
	
	
	public static int[][] calcularMatrizDeLaImagen(BufferedImage image, Canal canal) {

		byte[] pixels;
		
		DataBuffer dataBuffer = image.getRaster().getDataBuffer();
		ByteBuffer byteBuffer;
		
		short[] pixelesS;
		byte[] pixelesB;
		
		if (dataBuffer instanceof DataBufferUShort) {
			pixelesS = ((DataBufferUShort) dataBuffer).getData();
			byteBuffer = ByteBuffer.allocate(pixelesS.length * 2);
			byteBuffer.asShortBuffer().put(ShortBuffer.wrap(pixelesS));
			pixels = byteBuffer.array();
		} else {
			pixelesB = ((DataBufferByte) dataBuffer).getData();
			byteBuffer = ByteBuffer.wrap(pixelesB);
			pixels = byteBuffer.array();
		}
		
		final int width = image.getWidth();
		final int height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;

		int[][] matriz = new int[height][width];
		if (hasAlphaChannel) {
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; (pixel + 3) < pixels.length; pixel += pixelLength) {
				
				int argb = 0;
				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
				argb += ((int) pixels[pixel + 1] & 0xff); // blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
				
				Color color = new Color(argb, true);
				switch (canal){
				
					case VERDE:
						matriz[row][col] = color.getGreen();
						break;
					case AZUL:
						matriz[row][col] = color.getBlue();
						break;
					default:
						matriz[row][col] = color.getRed();
						break;
				}
				
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		} else {
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; (pixel +3) < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				
				Color color = new Color(argb);
				switch (canal){
				
					case VERDE:
						matriz[row][col] = color.getGreen();
						break;
					case AZUL:
						matriz[row][col] = color.getBlue();
						break;
					default:
						matriz[row][col] = color.getRed();
						break;
				}
				
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		}

		return matriz;
	}
	

	public static BufferedImage generarImagenRGB(int[][] matrizRojos, int[][] matrizVerdes, int[][] matrizAzules) {

		int ancho = matrizRojos[0].length;
		int alto = matrizRojos.length;
		
		BufferedImage imagenResultante = new BufferedImage(ancho, alto, BufferedImage.TYPE_3BYTE_BGR);
		
			for (int i = 0; i < alto; i++) {
				for (int j = 0; j < ancho; j++) {

					Color color = new Color(matrizRojos[i][j], matrizVerdes[i][j], matrizAzules[i][j]);
					imagenResultante.setRGB(j, i, color.getRGB());
				}
			}
		
			
		return imagenResultante;
	}
	
	public static int[][] aplicarTransformacionLogaritmica(int[][] matrizDesfasada) {

		float maximo;

		int[][] matrizTransformada;
		
		int filas = matrizDesfasada.length;
		int columnas = matrizDesfasada[0].length;
		
		matrizTransformada = new int[filas][columnas];
		
		maximo = 255;

		for (int f = 0; f < filas; f++) {
			for (int g = 0; g < columnas; g++) {
		
				int valorActual = matrizDesfasada[f][g];
				
				if (maximo < valorActual) {
					maximo = valorActual;
				}
			}
		}

		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {

				int valorActual = matrizDesfasada[i][j];
				
				int valorTransformado = (int) ((255f / (Math.log(maximo))) * Math.log(1 + valorActual));;

				matrizTransformada[i][j] = valorTransformado;
			}
		}
		
		return matrizTransformada;
	}
	
	public static int[][] aplicarTransformacionLineal(int[][] matrizDesfasada) {

		float minimo;
		float maximo;

		int[][] matrizTransformada;
		
		int filas = matrizDesfasada.length;
		int columnas = matrizDesfasada[0].length;
		
		matrizTransformada = new int[filas][columnas];
		
		minimo = 0;
		maximo = 255;

		for (int f = 0; f < filas; f++) {
			for (int g = 0; g < columnas; g++) {
		
				int valorActual = matrizDesfasada[f][g];
				
				if (minimo > valorActual) {
					minimo = valorActual;
				}

				if (maximo < valorActual) {
					maximo = valorActual;
				}

			}

		}

		float[] maximosYMinimos = new float[2];
		maximosYMinimos[0] = minimo;
		maximosYMinimos[1] = maximo;
		
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {

				int valorActual = matrizDesfasada[i][j];
				int valorTransformado = (int) ((((255f) / (maximo - minimo)) * valorActual) - ((minimo * 255f) / (maximo - minimo)));

				matrizTransformada[i][j] = valorTransformado;
			}
		}
		
		return matrizTransformada;
	}
	
	public static BufferedImage obtenerImagenDeMatrices(int[][] matrizRojos, int[][] matrizVerdes, int[][] matrizAzules){
		
		int filas = matrizRojos.length;
		int columnas = matrizRojos[0].length;

		BufferedImage imagenFinal = new BufferedImage(filas, columnas, BufferedImage.TYPE_3BYTE_BGR);
			
		for (int f = 0; f < filas; f++) {
			for (int g = 0; g < columnas; g++) {

				Color color = new Color(matrizRojos[f][g], matrizVerdes[f][g], matrizAzules[f][g]);
				imagenFinal.setRGB(f, g, color.getRGB());
			}
		}
		
		return imagenFinal;
	}


	public static int[][] elevarAlCuadrado(int[][] matriz) {
		
		int filas = matriz.length;
		int columnas = matriz[0].length;

		for (int f = 0; f < filas; f++) {
			for (int g = 0; g < columnas; g++) {

				matriz[f][g] = (int) Math.pow(matriz[f][g],2);
			}
		}
		
		return matriz;
	}

	
	public static int[][] multiplicarValores(int[][] matrizRojoEnX,
			int[][] matrizRojoEnY) {

		int filas = matrizRojoEnX.length;
		int columnas = matrizRojoEnX[0].length;
		int[][] matrizResultado = new int[filas][columnas];

		for (int f = 0; f < filas; f++) {
			for (int g = 0; g < columnas; g++) {

				matrizResultado[f][g] = matrizRojoEnX[f][g]*matrizRojoEnY[f][g];
			}
		}
		
		return matrizResultado;
	}
}

