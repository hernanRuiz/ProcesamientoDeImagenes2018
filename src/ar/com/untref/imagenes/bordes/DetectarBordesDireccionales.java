package ar.com.untref.imagenes.bordes;

import java.awt.image.BufferedImage;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.filtros.FiltroNuevo;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.MatricesManager;

public class DetectarBordesDireccionales {

	public static BufferedImage aplicarDetectorDeBordesDireccional(Imagen imagenOriginal, String nombreMascara){
		
		float[][] mascaraEnX = calcularMascaraEnDireccion(nombreMascara);
		float[][] mascaraEnY = calcularMascaraEnDireccion(mascaraEnX, nombreMascara,  "y");
		float[][] mascaraEn45 = calcularMascaraEnDireccion(mascaraEnX, nombreMascara, "45");
		float[][] mascaraEn135 = calcularMascaraEnDireccion(mascaraEnX, nombreMascara, "135");
		
		int[][] matrizMejoresBordesRojos = new int[imagenOriginal.getBufferedImage().getWidth()][imagenOriginal.getBufferedImage().getHeight()];
		int[][] matrizMejoresBordesVerdes = new int[imagenOriginal.getBufferedImage().getWidth()][imagenOriginal.getBufferedImage().getHeight()];
		int[][] matrizMejoresBordesAzules = new int[imagenOriginal.getBufferedImage().getWidth()][imagenOriginal.getBufferedImage().getHeight()];
		
        Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenFiltradaEnY = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenFiltradaEn45 = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenFiltradaEn135 = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
		Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraEnX);
        FiltroNuevo filtroEnY = new FiltroNuevo(mascaraEnY);
        FiltroNuevo filtroEn45 = new FiltroNuevo(mascaraEn45);
        FiltroNuevo filtroEn135 = new FiltroNuevo(mascaraEn135);
        
        //Aplicamos filtros en X, en Y, en 45 y en 135
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.AZUL);
        		
        int[][] matrizRojoEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.AZUL);
        
        int[][] matrizRojoEn45 = filtroEn45.filtrar(imagenFiltradaEn45, Canal.ROJO);
        int[][] matrizVerdeEn45 = filtroEn45.filtrar(imagenFiltradaEn45, Canal.VERDE);
        int[][] matrizAzulEn45 = filtroEn45.filtrar(imagenFiltradaEn45, Canal.AZUL);
        		
        int[][] matrizRojoEn135 = filtroEn135.filtrar(imagenFiltradaEn135, Canal.ROJO);
        int[][] matrizVerdeEn135 = filtroEn135.filtrar(imagenFiltradaEn135, Canal.VERDE);
        int[][] matrizAzulEn135 = filtroEn135.filtrar(imagenFiltradaEn135, Canal.AZUL);
        		
        //Hallamos los mejores bordes: el máximo para cada punto entre las 4 matrices.
        if(imagenOriginal.getBufferedImage().getWidth() == imagenOriginal.getBufferedImage().getHeight()){
        for (int i = 0; i < matrizRojoEnX[0].length; i++) {
			for (int j = 0; j < matrizRojoEnX.length; j++) {
				
				int valorRojoEnX = matrizRojoEnX[i][j];
				int valorVerdeEnX = matrizVerdeEnX[i][j]; 
				int valorAzulEnX = matrizAzulEnX[i][j];
				
				int valorRojoEnY = matrizRojoEnY[i][j];
				int valorVerdeEnY = matrizVerdeEnY[i][j]; 
				int valorAzulEnY = matrizAzulEnY[i][j];
				
				int valorRojoEn45 = matrizRojoEn45[i][j];
				int valorVerdeEn45 = matrizVerdeEn45[i][j]; 
				int valorAzulEn45 = matrizAzulEn45[i][j];
				
				int valorRojoEn135 = matrizRojoEn135[i][j];
				int valorVerdeEn135 = matrizVerdeEn135[i][j]; 
				int valorAzulEn135 = matrizAzulEn135[i][j]; 
				
				int rojoMax = Math.max(Math.max(valorRojoEnX, valorRojoEnY), Math.max(valorRojoEn45, valorRojoEn135));
				int verdeMax = Math.max(Math.max(valorVerdeEnX, valorVerdeEnY), Math.max(valorVerdeEn45, valorVerdeEn135));
				int azulMax = Math.max(Math.max(valorAzulEnX, valorAzulEnY), Math.max(valorAzulEn45, valorAzulEn135));
				
				matrizMejoresBordesRojos[i][j] = rojoMax;
				matrizMejoresBordesVerdes[i][j] = verdeMax;
				matrizMejoresBordesAzules[i][j] = azulMax;
			}
        }
        }else{
        	  for (int i = 0; i < matrizRojoEnX.length; i++) {
      			for (int j = 0; j < matrizRojoEnX[0].length; j++) {
      				
      				int valorRojoEnX = matrizRojoEnX[i][j];
      				int valorVerdeEnX = matrizVerdeEnX[i][j]; 
      				int valorAzulEnX = matrizAzulEnX[i][j];
      				
      				int valorRojoEnY = matrizRojoEnY[i][j];
      				int valorVerdeEnY = matrizVerdeEnY[i][j]; 
      				int valorAzulEnY = matrizAzulEnY[i][j];
      				
      				int valorRojoEn45 = matrizRojoEn45[i][j];
      				int valorVerdeEn45 = matrizVerdeEn45[i][j]; 
      				int valorAzulEn45 = matrizAzulEn45[i][j];
      				
      				int valorRojoEn135 = matrizRojoEn135[i][j];
      				int valorVerdeEn135 = matrizVerdeEn135[i][j]; 
      				int valorAzulEn135 = matrizAzulEn135[i][j]; 
      				
      				int rojoMax = Math.max(Math.max(valorRojoEnX, valorRojoEnY), Math.max(valorRojoEn45, valorRojoEn135));
      				int verdeMax = Math.max(Math.max(valorVerdeEnX, valorVerdeEnY), Math.max(valorVerdeEn45, valorVerdeEn135));
      				int azulMax = Math.max(Math.max(valorAzulEnX, valorAzulEnY), Math.max(valorAzulEn45, valorAzulEn135));
      				
      				matrizMejoresBordesRojos[i][j] = rojoMax;
      				matrizMejoresBordesVerdes[i][j] = verdeMax;
      				matrizMejoresBordesAzules[i][j] = azulMax;
      			}
              }
        }
        int [][] matrizMejoresBordesRojosTransofrmada = MatricesManager.aplicarTransformacionLineal(matrizMejoresBordesRojos);
        int [][] matrizMejoresBordesVerdesTransofrmada = MatricesManager.aplicarTransformacionLineal(matrizMejoresBordesVerdes);
        int [][] matrizMejoresBordesAzulesTransofrmada = MatricesManager.aplicarTransformacionLineal(matrizMejoresBordesAzules);
        
        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizMejoresBordesRojosTransofrmada, matrizMejoresBordesVerdesTransofrmada, matrizMejoresBordesAzulesTransofrmada));
        
        return imagenResultante.getBufferedImage();
	}
	
	
	private static float[][] calcularMascaraEnDireccion(String nombre) {
		
		float [][] mascara = new float[3][3];
		
		switch (nombre) {

			case "Prewitt":
				if(nombre == "Prewitt"){mascara = calcularMascaraDePrewittEnX();}
				break;

			case "Sobel":
				if(nombre == "Sobel"){mascara = calcularMascaraDeSobelEnX();}
				break;

			case "Kirsh":
				if(nombre == "Kirsh"){mascara = calcularMascaraDeKirshEnX();}
				break;
		
			case "Nueva":
				if(nombre == "Nueva"){mascara = calcularMascaraNuevaEnX();}
				break;
		}
		
		return mascara;
	}
	
	private static float[][] calcularMascaraEnDireccion(float[][] mascaraOriginal, String nombre, String direccion) {
		
		float [][] mascara = new float[3][3];
		
		switch (direccion) {

			case "y":
				if(nombre == "Prewitt"){mascara = calcularMascaraDePrewittEnY();}
				if(nombre == "Sobel"){mascara = calcularMascaraDeSobelEnY();}
				if(nombre == "Kirsh"){mascara = calcularMascaraDeKirshEnY();}
				if(nombre == "Nueva"){mascara = calcularMascaraNuevaEnY();}
				break;

			case "45":
				if(nombre == "Prewitt"){mascara = calcularMascaraDePrewittEn45();}
				if(nombre == "Sobel"){mascara = calcularMascaraDeSobelEn45();}
				if(nombre == "Kirsh"){mascara = calcularMascaraDeKirshEn45();}
				if(nombre == "Nueva"){mascara = calcularMascaraNuevaEn45();}
				break;

			case "135":
				if(nombre == "Prewitt"){mascara = calcularMascaraDePrewittEn135();}
				if(nombre == "Sobel"){mascara = calcularMascaraDeSobelEn135();}
				if(nombre == "Kirsh"){mascara = calcularMascaraDeKirshEn135();}
				if(nombre == "Nueva"){mascara = calcularMascaraNuevaEn135();}
				break;
		}
		
		return mascara;
	}

	
	private static float[][] calcularMascaraDePrewittEnX() {
		
		float[][] mascaraDePrewittEnX = new float [3][3];
		
		mascaraDePrewittEnX[0][0]= -1;
		mascaraDePrewittEnX[0][1]= -1;
		mascaraDePrewittEnX[0][2]= -1;
		mascaraDePrewittEnX[1][0]= 0;
		mascaraDePrewittEnX[1][1]= 0;
		mascaraDePrewittEnX[1][2]= 0;
		mascaraDePrewittEnX[2][0]= 1;
		mascaraDePrewittEnX[2][1]= 1;
		mascaraDePrewittEnX[2][2]= 1;
		
		return mascaraDePrewittEnX;
	}
	
	private static float[][] calcularMascaraDePrewittEnY() {
		
		float[][] mascaraDePrewittEnY = new float [3][3];
		
		mascaraDePrewittEnY[0][0]= -1;
		mascaraDePrewittEnY[0][1]= 0;
		mascaraDePrewittEnY[0][2]= 1;
		mascaraDePrewittEnY[1][0]= -1;
		mascaraDePrewittEnY[1][1]= 0;
		mascaraDePrewittEnY[1][2]= 1;
		mascaraDePrewittEnY[2][0]= -1;
		mascaraDePrewittEnY[2][1]= 0;
		mascaraDePrewittEnY[2][2]= 1;
		
		return mascaraDePrewittEnY;
	}
	
	private static float[][] calcularMascaraDePrewittEn45() {
		
		float[][] mascaraDePrewittEnX = new float [3][3];
		
		mascaraDePrewittEnX[0][0]= 0;
		mascaraDePrewittEnX[0][1]= -1;
		mascaraDePrewittEnX[0][2]= -1;
		mascaraDePrewittEnX[1][0]= 1;
		mascaraDePrewittEnX[1][1]= 0;
		mascaraDePrewittEnX[1][2]= -1;
		mascaraDePrewittEnX[2][0]= 1;
		mascaraDePrewittEnX[2][1]= 1;
		mascaraDePrewittEnX[2][2]= 0;
		
		return mascaraDePrewittEnX;
	}
	
	private static float[][] calcularMascaraDePrewittEn135() {
		
		float[][] mascaraDePrewittEnX = new float [3][3];
		
		mascaraDePrewittEnX[0][0]= -1;
		mascaraDePrewittEnX[0][1]= -1;
		mascaraDePrewittEnX[0][2]= 0;
		mascaraDePrewittEnX[1][0]= -1;
		mascaraDePrewittEnX[1][1]= 0;
		mascaraDePrewittEnX[1][2]= 1;
		mascaraDePrewittEnX[2][0]= 0;
		mascaraDePrewittEnX[2][1]= 1;
		mascaraDePrewittEnX[2][2]= 1;
		
		return mascaraDePrewittEnX;
	}
	
	private static float[][] calcularMascaraDeSobelEnX() {
		
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
	
	private static float[][] calcularMascaraDeSobelEnY() {
		
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
	
	private static float[][] calcularMascaraDeSobelEn45() {
		
		float[][] mascaraDeSobelEn45 = new float [3][3];
		
		mascaraDeSobelEn45[0][0]= 0;
		mascaraDeSobelEn45[0][1]= -1;
		mascaraDeSobelEn45[0][2]= -2;
		mascaraDeSobelEn45[1][0]= 1;
		mascaraDeSobelEn45[1][1]= 0;
		mascaraDeSobelEn45[1][2]= -1;
		mascaraDeSobelEn45[2][0]= 2;
		mascaraDeSobelEn45[2][1]= 1;
		mascaraDeSobelEn45[2][2]= 0;
		
		return mascaraDeSobelEn45;
	}
	
	private static float[][] calcularMascaraDeSobelEn135() {
		
		float[][] mascaraDeSobelEn135 = new float [3][3];
		
		mascaraDeSobelEn135[0][0]= 0;
		mascaraDeSobelEn135[0][1]= 1;
		mascaraDeSobelEn135[0][2]= 2;
		mascaraDeSobelEn135[1][0]= -1;
		mascaraDeSobelEn135[1][1]= 0;
		mascaraDeSobelEn135[1][2]= 1;
		mascaraDeSobelEn135[2][0]= -2;
		mascaraDeSobelEn135[2][1]= -1;
		mascaraDeSobelEn135[2][2]= 0;
		
		return mascaraDeSobelEn135;
	}
	
	private static float[][] calcularMascaraDeKirshEnX() {
		
		float[][] mascaraDeKirshEnX = new float [3][3];
		
		mascaraDeKirshEnX[0][0]= 5;
		mascaraDeKirshEnX[0][1]= 5;
		mascaraDeKirshEnX[0][2]= 5;
		mascaraDeKirshEnX[1][0]= -3;
		mascaraDeKirshEnX[1][1]= 0;
		mascaraDeKirshEnX[1][2]= -3;
		mascaraDeKirshEnX[2][0]= -3;
		mascaraDeKirshEnX[2][1]= -3;
		mascaraDeKirshEnX[2][2]= -3;
		
		return mascaraDeKirshEnX;
	}
	
	private static float[][] calcularMascaraDeKirshEnY() {
		
		float[][] mascaraDeKirshEnY = new float [3][3];
		
		mascaraDeKirshEnY[0][0]= 5;
		mascaraDeKirshEnY[0][1]= -3;
		mascaraDeKirshEnY[0][2]= -3;
		mascaraDeKirshEnY[1][0]= 5;
		mascaraDeKirshEnY[1][1]= 0;
		mascaraDeKirshEnY[1][2]= -3;
		mascaraDeKirshEnY[2][0]= 5;
		mascaraDeKirshEnY[2][1]= -3;
		mascaraDeKirshEnY[2][2]= -3;
		
		return mascaraDeKirshEnY;
	}
	
	private static float[][] calcularMascaraDeKirshEn45() {
		
		float[][] mascaraDeKirshEn45 = new float [3][3];
		
		mascaraDeKirshEn45[0][0]=-3;
		mascaraDeKirshEn45[0][1]= -3;
		mascaraDeKirshEn45[0][2]= -3;
		mascaraDeKirshEn45[1][0]= 5;
		mascaraDeKirshEn45[1][1]= 0;
		mascaraDeKirshEn45[1][2]= -3;
		mascaraDeKirshEn45[2][0]= 5;
		mascaraDeKirshEn45[2][1]= 5;
		mascaraDeKirshEn45[2][2]= -3;
		
		return mascaraDeKirshEn45;
	}
	
	private static float[][] calcularMascaraDeKirshEn135() {
		
		float[][] mascaraDeKirshEn135 = new float [3][3];
		
		mascaraDeKirshEn135[0][0]=-3;
		mascaraDeKirshEn135[0][1]= -3;
		mascaraDeKirshEn135[0][2]= -3;
		mascaraDeKirshEn135[1][0]= -3;
		mascaraDeKirshEn135[1][1]= 0;
		mascaraDeKirshEn135[1][2]= 5;
		mascaraDeKirshEn135[2][0]= -3;
		mascaraDeKirshEn135[2][1]= 5;
		mascaraDeKirshEn135[2][2]= 5;
		
		return mascaraDeKirshEn135;
	}
	
	private static float[][] calcularMascaraNuevaEnX() {
		
		float[][] mascaraDeNuevaEnX = new float [3][3];
		
		mascaraDeNuevaEnX[0][0]= 1;
		mascaraDeNuevaEnX[0][1]= 1;
		mascaraDeNuevaEnX[0][2]= 1;
		mascaraDeNuevaEnX[1][0]= 1;
		mascaraDeNuevaEnX[1][1]= -2;
		mascaraDeNuevaEnX[1][2]= 1;
		mascaraDeNuevaEnX[2][0]= -1;
		mascaraDeNuevaEnX[2][1]= -1;
		mascaraDeNuevaEnX[2][2]= -1;
		
		return mascaraDeNuevaEnX;
	}
	
	private static float[][] calcularMascaraNuevaEnY() {
		
		float[][] mascaraDeNuevaEnY = new float [3][3];
		
		mascaraDeNuevaEnY[0][0]= 1;
		mascaraDeNuevaEnY[0][1]= 1;
		mascaraDeNuevaEnY[0][2]= -1;
		mascaraDeNuevaEnY[1][0]= 1;
		mascaraDeNuevaEnY[1][1]= -2;
		mascaraDeNuevaEnY[1][2]= -1;
		mascaraDeNuevaEnY[2][0]= 1;
		mascaraDeNuevaEnY[2][1]= 1;
		mascaraDeNuevaEnY[2][2]= -1;
		
		return mascaraDeNuevaEnY;
	}
	
	private static float[][] calcularMascaraNuevaEn45() {
		
		float[][] mascaraDeNuevaEn45 = new float [3][3];
		
		mascaraDeNuevaEn45[0][0]= 1;
		mascaraDeNuevaEn45[0][1]= -1;
		mascaraDeNuevaEn45[0][2]= -1;
		mascaraDeNuevaEn45[1][0]= 1;
		mascaraDeNuevaEn45[1][1]= -2;
		mascaraDeNuevaEn45[1][2]= -1;
		mascaraDeNuevaEn45[2][0]= 1;
		mascaraDeNuevaEn45[2][1]= 1;
		mascaraDeNuevaEn45[2][2]= 1;
		
		return mascaraDeNuevaEn45;
	}
	
	private static float[][] calcularMascaraNuevaEn135() {
		
		float[][] mascaraDeNuevaEn135 = new float [3][3];
		
		mascaraDeNuevaEn135[0][0]= -1;
		mascaraDeNuevaEn135[0][1]= -1;
		mascaraDeNuevaEn135[0][2]= 1;
		mascaraDeNuevaEn135[1][0]= -1;
		mascaraDeNuevaEn135[1][1]= -2;
		mascaraDeNuevaEn135[1][2]= 1;
		mascaraDeNuevaEn135[2][0]= 1;
		mascaraDeNuevaEn135[2][1]= 1;
		mascaraDeNuevaEn135[2][2]= 1;
		
		return mascaraDeNuevaEn135;
	}
	
}
