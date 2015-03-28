package ar.com.untref.imagenes.procesamiento;

public class MatricesManager {

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
			// la multiplicación no es posible si la cantidad de columnas de una
			// matriz
			// no coincide con la cantidad de filas de la otra
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
			// la suma no es posible si las matrices no coinciden en la cantidad
			// de filas y columnas
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
			// la resta no es posible si las matrices no coinciden en la
			// cantidad de filas y columnas
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

}
