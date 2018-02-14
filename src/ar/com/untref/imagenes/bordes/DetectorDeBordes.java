package ar.com.untref.imagenes.bordes;

import java.awt.image.BufferedImage;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.filtros.FiltroNuevo;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.MatricesManager;

//Define máscaras de Sobel para el algoritmo de Harris
public class DetectorDeBordes {

	public static float[][] calcularMascaraDeSobelEnY() {
		
		float[][] mascaraDeSobelEnY = new float [3][3];
		
		mascaraDeSobelEnY[0][0]= -1;
		mascaraDeSobelEnY[0][1]= 0;
		mascaraDeSobelEnY[0][2]= 1;
		mascaraDeSobelEnY[1][0]= -2;
		mascaraDeSobelEnY[1][1]= 0;
		mascaraDeSobelEnY[1][2]= 2;
		mascaraDeSobelEnY[2][0]= -1;
		mascaraDeSobelEnY[2][1]= 0;
		mascaraDeSobelEnY[2][2]= 1;
		
		return mascaraDeSobelEnY;
	}
	
	public static float[][] calcularMascaraDeSobelEnX() {
		
		float[][] mascaraDeSobelEnX = new float [3][3];
		
		mascaraDeSobelEnX[0][0]= -1;
		mascaraDeSobelEnX[0][1]= -2;
		mascaraDeSobelEnX[0][2]= -1;
		mascaraDeSobelEnX[1][0]= 0;
		mascaraDeSobelEnX[1][1]= 0;
		mascaraDeSobelEnX[1][2]= 0;
		mascaraDeSobelEnX[2][0]= 1;
		mascaraDeSobelEnX[2][1]= 2;
		mascaraDeSobelEnX[2][2]= 1;
		
		return mascaraDeSobelEnX;
	}
	
	public static BufferedImage mostrarMascaraDeSobelEnX(Imagen imagenOriginal){
		
		float[][] mascaraDeSobelEnX = calcularMascaraDeSobelEnX();
		
		Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDeSobelEnX);
        
        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.AZUL);
        
        int[][] matrizRojoEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizRojoEnX);
        int[][] matrizVerdeEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizVerdeEnX);
        int[][] matrizAzulEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizAzulEnX);
        
        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnXTransformada, matrizVerdeEnXTransformada, matrizAzulEnXTransformada));

		return imagenResultante.getBufferedImage();
		
	}
	
	public static BufferedImage mostrarMascaraDeSobelEnY(Imagen imagenOriginal){
		
		float[][] mascaraDeSobelEnY = calcularMascaraDeSobelEnY();
		
		Imagen imagenFiltradaEnY = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
        FiltroNuevo filtroEnY = new FiltroNuevo(mascaraDeSobelEnY);
        
        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.AZUL);
        
        int[][] matrizRojoEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizRojoEnY);
        int[][] matrizVerdeEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizVerdeEnY);
        int[][] matrizAzulEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizAzulEnY);
        
        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnYTransformada, matrizVerdeEnYTransformada, matrizAzulEnYTransformada));

		return imagenResultante.getBufferedImage();
		
	}

}
