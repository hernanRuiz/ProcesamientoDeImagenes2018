package ar.com.untref.imagenes.modelo;

public class MatrizDeColores {

	private int[][] matrizRojos;
	private int[][] matrizVerdes;
	private int[][] matrizAzules;
	
	public MatrizDeColores(int[][] matrizRojos, int[][] matrizVerdes, int[][] matrizAzules){
		
		this.matrizRojos = matrizRojos;
		this.matrizVerdes = matrizVerdes;
		this.matrizAzules = matrizAzules;
	}

	public int[][] getMatrizRojos() {
		return matrizRojos;
	}

	public int[][] getMatrizVerdes() {
		return matrizVerdes;
	}

	public int[][] getMatrizAzules() {
		return matrizAzules;
	}
	
}
