package ar.com.untref.imagenes.procesamiento;

import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;

public class MatricesManager {

	private static JPanel contentPane;
	static float[][] matrizRojos;
	static float[][] matrizVerdes;
	static float[][] matrizAzules;
	
	public static int[][] multiplicarMatrices(int[][] matriz1, int[][] matriz2) {

		int matriz1CantidadColumnas = matriz1[0].length;

		int matriz2CantidadFilas = matriz2.length;

		int filasMatrizResultante = matriz1.length;
		int columnasMatrizResultante = matriz2[0].length;
		int[][] matrizResultante = new int[filasMatrizResultante][columnasMatrizResultante];

		if (matriz1CantidadColumnas == matriz2CantidadFilas) {
			for (int i = 0; i < filasMatrizResultante; i++) {
				for (int j = 0; j < columnasMatrizResultante; j++) {
					for (int k = 0; k < matriz1CantidadColumnas; k++) {
						matrizResultante[i][j] += matriz1[i][k] * matriz2[k][j];
					}
				}
			}
		} else {
			DialogsHelper.mostarMensaje(contentPane, "La multiplicación no es posible si la cantidad de columnas de una matriz no coincide con la cantidad de filas de la otra", NivelMensaje.ERROR);
		}
		return matrizResultante;
	}

	public static int[][] multiplicarMatrizPorEscalar(int[][] matriz,
			int escalar) {

		int filasMatrizResultante = matriz.length;
		int columnasMatrizResultante = matriz[0].length;
		int[][] matrizResultante = new int[filasMatrizResultante][columnasMatrizResultante];

		for (int i = 0; i < filasMatrizResultante; i++) {
			for (int j = 0; j < columnasMatrizResultante; j++) {
				matrizResultante[i][j] += matriz[i][j] * escalar;
			}
		}
		return matrizResultante;
	}
	
	
	public static int[][] sumarMatrizYEscalar(int[][] matriz,
			int escalar) {

		int filasMatrizResultante = matriz.length;
		int columnasMatrizResultante = matriz[0].length;
		int[][] matrizResultante = new int[filasMatrizResultante][columnasMatrizResultante];

		for (int i = 0; i < filasMatrizResultante; i++) {
			for (int j = 0; j < columnasMatrizResultante; j++) {
				matrizResultante[i][j] += matriz[i][j] + escalar;
			}
		}
		return matrizResultante;
	}
	
	public static int[][] restarMatrizYEscalar(int[][] matriz,
			int escalar) {

		int filasMatrizResultante = matriz.length;
		int columnasMatrizResultante = matriz[0].length;
		int[][] matrizResultante = new int[filasMatrizResultante][columnasMatrizResultante];

		for (int i = 0; i < filasMatrizResultante; i++) {
			for (int j = 0; j < columnasMatrizResultante; j++) {
				matrizResultante[i][j] += matriz[i][j] * escalar;
			}
		}
		return matrizResultante;
	}

	public static int[][] sumarMatrices(int[][] matriz1, int[][] matriz2) {
		int matriz1CantidadFilas = matriz1.length;
		int matriz1CantidadColumnas = matriz1[0].length;
		int matriz2CantidadFilas = matriz2.length;
		int matriz2CantidadColumnas = matriz2[0].length;
		int filasMatrizResultante = matriz1.length;
		int columnasMatrizResultante = matriz1[0].length;
		int[][] matrizResultante = new int[filasMatrizResultante][columnasMatrizResultante];

		if (matriz1CantidadFilas == matriz2CantidadFilas
				&& matriz1CantidadColumnas == matriz2CantidadColumnas) {
			for (int i = 0; i < filasMatrizResultante; i++) {
				for (int j = 0; j < columnasMatrizResultante; j++) {
					matrizResultante[i][j] += matriz1[i][j] + matriz2[i][j];
				}
			}
		} else {
			DialogsHelper.mostarMensaje(contentPane, "La suma no es posible si las matrices no coinciden en la cantidad de filas y columnas", NivelMensaje.ERROR);

		}
		return matrizResultante;
	}

	public static int[][] restarMatrices(int[][] matriz1, int[][] matriz2) {
		int matriz1CantidadFilas = matriz1.length;
		int matriz1CantidadColumnas = matriz1[0].length;
		int matriz2CantidadFilas = matriz2.length;
		int matriz2CantidadColumnas = matriz2[0].length;
		int filasMatrizResultante = matriz1.length;
		int columnasMatrizResultante = matriz1[0].length;
		int[][] matrizResultante = new int[filasMatrizResultante][columnasMatrizResultante];

		if (matriz1CantidadFilas == matriz2CantidadFilas
				&& matriz1CantidadColumnas == matriz2CantidadColumnas) {
			for (int i = 0; i < filasMatrizResultante; i++) {
				for (int j = 0; j < columnasMatrizResultante; j++) {
					matrizResultante[i][j] += matriz1[i][j] - matriz2[i][j];
				}
			}
		} else {
			DialogsHelper.mostarMensaje(contentPane, "La resta no es posible si las matrices no coinciden en la cantidad de filas y columnas", NivelMensaje.ERROR);
		}
		return matrizResultante;
	}

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
				BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int alpha = 0;
		        int red = matriz[i][j];
		        int green = matriz[i][j];
		        int blue = matriz[i][j];
		        int color = alpha + red + green + blue;
				imagenResultante.setRGB(j, i, color);
			}
		}
		
		return imagenResultante;
	}	
}

