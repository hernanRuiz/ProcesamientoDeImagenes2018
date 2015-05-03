package ar.com.untref.imagenes.bordes;

import java.awt.image.BufferedImage;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.filtros.FiltroNuevo;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.MatricesManager;

public class DetectarBordesDireccionales {

	public static BufferedImage aplicarDetectorDeBordesDireccional(Imagen imagenOriginal, String nombreMascara){
		
		int[][] mascaraEnX = calcularMascaraEnDireccion(nombreMascara);
		int[][] mascaraEnY = calcularMascaraEnDireccion(mascaraEnX, nombreMascara,  "y");
		int[][] mascaraEn45 = calcularMascaraEnDireccion(mascaraEnX, nombreMascara, "45");
		int[][] mascaraEn135 = calcularMascaraEnDireccion(mascaraEnX, nombreMascara, "135");
		
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
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraEnX, Canal.AZUL);
        		
        int[][] matrizRojoEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraEnY, Canal.AZUL);
        
        int[][] matrizRojoEn45 = filtroEn45.filtrar(imagenFiltradaEn45, mascaraEn45, Canal.ROJO);
        int[][] matrizVerdeEn45 = filtroEn45.filtrar(imagenFiltradaEn45, mascaraEn45, Canal.VERDE);
        int[][] matrizAzulEn45 = filtroEn45.filtrar(imagenFiltradaEn45, mascaraEn45, Canal.AZUL);
        		
        int[][] matrizRojoEn135 = filtroEn135.filtrar(imagenFiltradaEn135, mascaraEn135, Canal.ROJO);
        int[][] matrizVerdeEn135 = filtroEn135.filtrar(imagenFiltradaEn135, mascaraEn135, Canal.VERDE);
        int[][] matrizAzulEn135 = filtroEn135.filtrar(imagenFiltradaEn135, mascaraEn135, Canal.AZUL);
        		
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
	
	
	private static int[][] calcularMascaraEnDireccion(String nombre) {
		
		int [][] mascara = new int[3][3];
		
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
	
	private static int[][] calcularMascaraEnDireccion(int[][] mascaraOriginal, String nombre, String direccion) {
		
		int [][] mascara = new int[3][3];
		
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

	
	private static int[][] calcularMascaraDePrewittEnX() {
		
		int[][] mascaraDePrewittEnX = new int [3][3];
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
	
	private static int[][] calcularMascaraDePrewittEnY() {
		int[][] mascaraDePrewittEnY = new int [3][3];
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
	
	private static int[][] calcularMascaraDePrewittEn45() {
		
		int[][] mascaraDePrewittEnX = new int [3][3];
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
	
	private static int[][] calcularMascaraDePrewittEn135() {
		
		int[][] mascaraDePrewittEnX = new int [3][3];
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
	
	private static int[][] calcularMascaraDeSobelEnX() {
		int[][] mascaraDeSobelEnX = new int [3][3];
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
	
	private static int[][] calcularMascaraDeSobelEnY() {
		int[][] mascaraDeSobelEnY = new int [3][3];
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
	
	private static int[][] calcularMascaraDeSobelEn45() {
		int[][] mascaraDeSobelEn45 = new int [3][3];
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
	
	private static int[][] calcularMascaraDeSobelEn135() {
		int[][] mascaraDeSobelEn135 = new int [3][3];
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
	
	private static int[][] calcularMascaraDeKirshEnX() {
		int[][] mascaraDeKirshEnX = new int [3][3];
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
	
	private static int[][] calcularMascaraDeKirshEnY() {
		int[][] mascaraDeKirshEnY = new int [3][3];
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
	
	private static int[][] calcularMascaraDeKirshEn45() {
		int[][] mascaraDeKirshEn45 = new int [3][3];
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
	
	private static int[][] calcularMascaraDeKirshEn135() {
		int[][] mascaraDeKirshEn135 = new int [3][3];
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
	
	private static int[][] calcularMascaraNuevaEnX() {
		int[][] mascaraDeNuevaEnX = new int [3][3];
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
	
	private static int[][] calcularMascaraNuevaEnY() {
		int[][] mascaraDeNuevaEnY = new int [3][3];
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
	
	private static int[][] calcularMascaraNuevaEn45() {
		int[][] mascaraDeNuevaEn45 = new int [3][3];
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
	
	private static int[][] calcularMascaraNuevaEn135() {
		int[][] mascaraDeNuevaEn135 = new int [3][3];
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
