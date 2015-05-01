package ar.com.untref.imagenes.bordes;

import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.filtros.Filtro;
import ar.com.untref.imagenes.filtros.FiltroNuevo;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.MatricesManager;

public class DetectorDeBordes {
	
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
	
	public static BufferedImage aplicarDetectorDePrewitt(Imagen imagenOriginal){
		
		int[][] mascaraDePrewittEnX = calcularMascaraDePrewittEnX();
		int[][] mascaraDePrewittEnY = calcularMascaraDePrewittEnY();
		
        Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenFiltradaEnY = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
		Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDePrewittEnX);
        FiltroNuevo filtroEnY = new FiltroNuevo(mascaraDePrewittEnY);
        
        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDePrewittEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDePrewittEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDePrewittEnX, Canal.AZUL);
        		
        int[][] matrizRojoEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraDePrewittEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraDePrewittEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraDePrewittEnY, Canal.AZUL);
        		
        //Sintetizamos usando la raiz de los cuadrados
        int[][] matrizRojosSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizRojoEnX, matrizRojoEnY));
        int[][] matrizVerdesSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizVerdeEnX, matrizVerdeEnY));
        int[][] matrizAzulesSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizAzulEnX, matrizAzulEnY));

        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojosSintetizadosYTransformados, matrizVerdesSintetizadosYTransformados, matrizAzulesSintetizadosYTransformados));
        
        return imagenResultante.getBufferedImage();
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
	
	public static BufferedImage aplicarDetectorDeSobel(Imagen imagenOriginal){
		
		int[][] mascaraDeSobelEnX = calcularMascaraDeSobelEnX();
		int[][] mascaraDeSobelEnY = calcularMascaraDeSobelEnY();
		
		Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenFiltradaEnY = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
		Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDeSobelEnX);
        FiltroNuevo filtroEnY = new FiltroNuevo(mascaraDeSobelEnY);
        
        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDeSobelEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDeSobelEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDeSobelEnX, Canal.AZUL);
        		
        int[][] matrizRojoEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraDeSobelEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraDeSobelEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraDeSobelEnY, Canal.AZUL);
        		
        //Sintetizamos usando la raiz de los cuadrados
        int[][] matrizRojosSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizRojoEnX, matrizRojoEnY));
        int[][] matrizVerdesSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizVerdeEnX, matrizVerdeEnY));
        int[][] matrizAzulesSintetizadosYTransformados = MatricesManager.aplicarTransformacionLineal(sintetizar(matrizAzulEnX, matrizAzulEnY));

        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojosSintetizadosYTransformados, matrizVerdesSintetizadosYTransformados, matrizAzulesSintetizadosYTransformados));
        
        return imagenResultante.getBufferedImage();
	}
	
	
	private static int[][] calcularMascaraDeLaplaciano() {
		int[][] mascaraDeLaplaciano = new int [3][3];
		mascaraDeLaplaciano[0][0]= 0;
		mascaraDeLaplaciano[0][1]= 1;
		mascaraDeLaplaciano[0][2]= 0;
		mascaraDeLaplaciano[1][0]= 1;
		mascaraDeLaplaciano[1][1]= -4;
		mascaraDeLaplaciano[1][2]= 1;
		mascaraDeLaplaciano[2][0]= 0;
		mascaraDeLaplaciano[2][1]= 1;
		mascaraDeLaplaciano[2][2]= 0;
		return mascaraDeLaplaciano;
	}

	
	public static BufferedImage aplicarDetectorLaplaciano(Imagen imagenOriginal){
		
		int[][] mascaraDeLaplaciano = calcularMascaraDeLaplaciano();
		
		int ancho = mascaraDeLaplaciano.length;
        int alto = mascaraDeLaplaciano[0].length;
        int tam = ancho * alto;
        float filtroK[] = new float[tam];
        
        BufferedImage im = new BufferedImage(imagenOriginal.getBufferedImage().getWidth(), imagenOriginal.getBufferedImage().getHeight(), imagenOriginal.getBufferedImage().getType());
		Imagen imagenFiltrada = new Imagen(im, imagenOriginal.getFormato(), imagenOriginal.getNombre());
		Imagen imagenResultante = new Imagen(im, imagenOriginal.getFormato(), imagenOriginal.getNombre());
        
        
      //Creamos el filtro - Se pasa de una matriz cuadrada (vector de 2 dimensiones) a un vector lineal
        for(int i=0; i < ancho; i++){
            for(int j=0; j < alto; j++){
                filtroK[i*ancho + j] = mascaraDeLaplaciano[i][j];
            }
        }
        
        Kernel kernel = new Kernel(ancho, alto, filtroK);
        Filtro filtro = new Filtro(kernel);
        
        //Aplicamos filtros
        filtro.filter(imagenOriginal.getBufferedImage(), imagenFiltrada.getBufferedImage());
        		
        		
        //Creamos la imagen resultante
        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                int pixel = imagenFiltrada.getBufferedImage().getRGB(i, j);
                imagenResultante.getBufferedImage().setRGB(i, j, pixel);
            }
        }
        
        return imagenResultante.getBufferedImage();
	}

	
	public static BufferedImage mostrarMascaraDeLaplaciano(Imagen imagenOriginal){
		
		int[][] mascaraDeLaplaciano = calcularMascaraDeLaplaciano();
		
		Imagen imagenFiltrada = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
        FiltroNuevo filtro = new FiltroNuevo(mascaraDeLaplaciano);
        
        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnX = filtro.filtrar(imagenFiltrada, mascaraDeLaplaciano, Canal.ROJO);
        int[][] matrizVerdeEnX = filtro.filtrar(imagenFiltrada, mascaraDeLaplaciano, Canal.VERDE);
        int[][] matrizAzulEnX = filtro.filtrar(imagenFiltrada, mascaraDeLaplaciano, Canal.AZUL);
        
        int[][] matrizRojoEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizRojoEnX);
        int[][] matrizVerdeEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizVerdeEnX);
        int[][] matrizAzulEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizAzulEnX);
        
        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnXTransformada, matrizVerdeEnXTransformada, matrizAzulEnXTransformada));

		return imagenResultante.getBufferedImage();
		
	}
	
	
	public static BufferedImage mostrarMascaraDePrewittEnX(Imagen imagenOriginal){
		
		int[][] mascaraDePrewittEnX = calcularMascaraDePrewittEnX();
		
		Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDePrewittEnX);
        
        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDePrewittEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDePrewittEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDePrewittEnX, Canal.AZUL);
        
        int[][] matrizRojoEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizRojoEnX);
        int[][] matrizVerdeEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizVerdeEnX);
        int[][] matrizAzulEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizAzulEnX);
        
        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnXTransformada, matrizVerdeEnXTransformada, matrizAzulEnXTransformada));

		return imagenResultante.getBufferedImage();
		
	}
	
	public static BufferedImage mostrarMascaraDePrewittEnY(Imagen imagenOriginal){
		
		int[][] mascaraDePrewittEnY = calcularMascaraDePrewittEnY();
		
		Imagen imagenFiltradaEnY = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
        FiltroNuevo filtroEnY = new FiltroNuevo(mascaraDePrewittEnY);
        
        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraDePrewittEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraDePrewittEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraDePrewittEnY, Canal.AZUL);
        
        int[][] matrizRojoEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizRojoEnY);
        int[][] matrizVerdeEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizVerdeEnY);
        int[][] matrizAzulEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizAzulEnY);
        
        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnYTransformada, matrizVerdeEnYTransformada, matrizAzulEnYTransformada));

		return imagenResultante.getBufferedImage();
		
	}
	
	
	public static BufferedImage mostrarMascaraDeSobelEnX(Imagen imagenOriginal){
		
		int[][] mascaraDeSobelEnX = calcularMascaraDeSobelEnX();
		
		Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDeSobelEnX);
        
        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDeSobelEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDeSobelEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, mascaraDeSobelEnX, Canal.AZUL);
        
        int[][] matrizRojoEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizRojoEnX);
        int[][] matrizVerdeEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizVerdeEnX);
        int[][] matrizAzulEnXTransformada = MatricesManager.aplicarTransformacionLineal(matrizAzulEnX);
        
        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnXTransformada, matrizVerdeEnXTransformada, matrizAzulEnXTransformada));

		return imagenResultante.getBufferedImage();
		
	}
	
	public static BufferedImage mostrarMascaraDeSobelEnY(Imagen imagenOriginal){
		
		int[][] mascaraDeSobelEnY = calcularMascaraDeSobelEnY();
		
		Imagen imagenFiltradaEnY = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
        FiltroNuevo filtroEnY = new FiltroNuevo(mascaraDeSobelEnY);
        
        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraDeSobelEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraDeSobelEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnY.filtrar(imagenFiltradaEnY, mascaraDeSobelEnY, Canal.AZUL);
        
        int[][] matrizRojoEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizRojoEnY);
        int[][] matrizVerdeEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizVerdeEnY);
        int[][] matrizAzulEnYTransformada = MatricesManager.aplicarTransformacionLineal(matrizAzulEnY);
        
        imagenResultante.setBufferedImage(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnYTransformada, matrizVerdeEnYTransformada, matrizAzulEnYTransformada));

		return imagenResultante.getBufferedImage();
		
	}
	
	
	private static int[][] sintetizar(int[][] matrizEnX, int[][] matrizEnY) {
		
		int[][] matrizFinal  = new int[matrizEnX.length][matrizEnX[0].length]; 
		
		for (int i=0; i<matrizEnX.length ;i++){
			for (int j=0; j<matrizEnX[0].length ;j++){
				
				matrizFinal[i][j] = (int) Math.hypot(matrizEnX[i][j], matrizEnY[i][j]);
			}
		}
		
		return matrizFinal;
	}
}
