package ar.com.untref.imagenes.bordes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.filtros.FiltroGaussiano;
import ar.com.untref.imagenes.filtros.FiltroNuevo;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.MatricesManager;
import ar.com.untref.imagenes.procesamiento.Umbralizador;

public class DetectorDeHarris {

	private static CustomBufferedImage oldImage;
	private static List<Integer> resultadosX = new LinkedList<Integer>();
	private static List<Integer>resultadosY = new LinkedList<Integer>();
	private static PrintStream fileStreamHarris;
	
	public static List<Integer> getResultadosX() {
		return resultadosX;
	}

	public static void setResultadosX(List<Integer> resultadosX) {
		DetectorDeHarris.resultadosX = resultadosX;
	}

	public static List<Integer> getResultadosY() {
		return resultadosY;
	}

	public static void setResultadosY(List<Integer> resultadosY) {
		DetectorDeHarris.resultadosY = resultadosY;
	}

	public static Imagen detectarEsquinas(Imagen imagenOriginal, boolean flagResultados){
		
		if (flagResultados){			
			try {
				fileStreamHarris  = new PrintStream("Salida_algoritmo_Harris.txt");
				fileStreamHarris.flush();
				System.setOut(fileStreamHarris);
				System.out.println("Resultados encontrados:");
				System.out.println();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		oldImage = new CustomBufferedImage(imagenOriginal.getBufferedImage());
		//Imagen imagenADevolver = imagenOriginal;
		
		
		//Paso 1: Calcular lx e Iy usando las m�caras de prewit o sobel para toda la imagen.
		float[][] mascaraDeSobelEnX = DetectorDeBordes.calcularMascaraDeSobelEnX();
		float[][] mascaraDeSobelEnY = DetectorDeBordes.calcularMascaraDeSobelEnY();
		
		Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenFiltradaEnY = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDeSobelEnX);
        FiltroNuevo filtroEnY = new FiltroNuevo(mascaraDeSobelEnY);
        
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.AZUL);
        		
        int[][] matrizRojoEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.AZUL);
        
        //Paso2: Calcular Ix2 elevando Ix^2 elemento a elemento y aplicar un filtro gaussiano 
        //para suavizar, por ejemplo, de 7x7 con sigma=2. Lo mismo Iy^2.
        int[][] matrizRojoEnXCuadrado = MatricesManager.aplicarTransformacionLineal(MatricesManager.elevarAlCuadrado(matrizRojoEnX));
        int[][] matrizVerdeEnXCuadrado = MatricesManager.aplicarTransformacionLineal(MatricesManager.elevarAlCuadrado(matrizVerdeEnX));
        int[][] matrizAzulEnXCuadrado = MatricesManager.aplicarTransformacionLineal(MatricesManager.elevarAlCuadrado(matrizAzulEnX));

        int[][] matrizRojoEnYCuadrado = MatricesManager.aplicarTransformacionLineal(MatricesManager.elevarAlCuadrado(matrizRojoEnY));
        int[][] matrizVerdeEnYCuadrado = MatricesManager.aplicarTransformacionLineal(MatricesManager.elevarAlCuadrado(matrizVerdeEnY));
        int[][] matrizAzulEnYCuadrado = MatricesManager.aplicarTransformacionLineal(MatricesManager.elevarAlCuadrado(matrizAzulEnY));
        
        Imagen imagenX = new Imagen(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnXCuadrado, matrizVerdeEnXCuadrado, matrizAzulEnXCuadrado), FormatoDeImagen.JPEG, "gaussX");
        Imagen imagenY = new Imagen(MatricesManager.obtenerImagenDeMatrices(matrizRojoEnYCuadrado, matrizVerdeEnYCuadrado, matrizAzulEnYCuadrado), FormatoDeImagen.JPEG, "gaussY");
        
        Imagen imagenConFiltroGaussEnX = FiltroGaussiano.aplicarFiltroGaussiano(imagenX, 2);
        Imagen imagenConFiltroGaussEnY = FiltroGaussiano.aplicarFiltroGaussiano(imagenY, 2);
        
        //Paso3: Calcular Ixy multiplicando elemento a elemento tambi�n suavizar con el mismo filtro gaussiano.
        int[][] matrizRojoXY = MatricesManager.aplicarTransformacionLineal(MatricesManager.multiplicarValores(matrizRojoEnX, matrizRojoEnY));
        int[][] matrizVerdeXY = MatricesManager.aplicarTransformacionLineal(MatricesManager.multiplicarValores(matrizVerdeEnX, matrizVerdeEnY));
        int[][] matrizAzulXY = MatricesManager.aplicarTransformacionLineal(MatricesManager.multiplicarValores(matrizAzulEnX, matrizAzulEnY));

        Imagen imagenXY = new Imagen(MatricesManager.obtenerImagenDeMatrices(matrizRojoXY, matrizVerdeXY, matrizAzulXY), FormatoDeImagen.JPEG, "gaussXY");
        Imagen imagenXYConFiltroGauss = FiltroGaussiano.aplicarFiltroGaussiano(imagenXY, 2);
        
        //Paso 4: con k=0.04 Calcular: cim1 = (Ix2*Iy2 - Ixy^2) - k*(Ix2 + Iy2)^2  
        int[][] cimRojos = MatricesManager.aplicarTransformacionLineal(calcularCim(imagenConFiltroGaussEnX.getMatriz(Canal.ROJO), imagenConFiltroGaussEnY.getMatriz(Canal.ROJO), imagenXYConFiltroGauss.getMatriz(Canal.ROJO)));
        int[][] cimVerdes = MatricesManager.aplicarTransformacionLineal(calcularCim(imagenConFiltroGaussEnX.getMatriz(Canal.VERDE), imagenConFiltroGaussEnY.getMatriz(Canal.VERDE), imagenXYConFiltroGauss.getMatriz(Canal.VERDE)));
        int[][] cimAzules = MatricesManager.aplicarTransformacionLineal(calcularCim(imagenConFiltroGaussEnX.getMatriz(Canal.AZUL), imagenConFiltroGaussEnY.getMatriz(Canal.AZUL), imagenXYConFiltroGauss.getMatriz(Canal.AZUL)));
        
        //Aplicamos filtros en X y en Y
        int[][] transpuestaRojo = new int[cimRojos[0].length][cimRojos.length];
        int[][] transpuestaVerde = new int[cimVerdes[0].length][cimVerdes.length];
        int[][] transpuestaAzul = new int[cimAzules[0].length][cimAzules.length];
    	
    	for(int j = 0; j < cimRojos.length; j++){
           for(int i = 0; i < cimRojos[0].length; i++){
        	   transpuestaRojo[i][j] = cimRojos[j][i];
        	   transpuestaVerde[i][j] = cimVerdes[j][i];
        	   transpuestaAzul[i][j] = cimAzules[j][i];
           }
        }
        
        Imagen imagenFinal = new Imagen(MatricesManager.obtenerImagenDeMatrices(transpuestaRojo, transpuestaVerde, transpuestaAzul), FormatoDeImagen.JPEG, "imagenFinal");
        
        int umbral = (int) (Umbralizador.generarUmbralizacionOtsu(imagenFinal, Canal.ROJO, false) * 2.6);
        
        Imagen imagenUmbralizada = Umbralizador.umbralizarImagen(imagenFinal, umbral);
        
		return superponerAImagenOriginal(imagenUmbralizada, imagenOriginal, flagResultados);
	}

	private static Imagen superponerAImagenOriginal(Imagen umbralizada, Imagen original, boolean flagResultados) {

		Imagen imagenFinal = new Imagen(new BufferedImage(umbralizada.getBufferedImage().getWidth(), umbralizada.getBufferedImage().getHeight(), umbralizada.getBufferedImage().getType()), FormatoDeImagen.JPEG, "final");
		
		for (int i=0; i< umbralizada.getBufferedImage().getWidth(); i++){
			for (int j=0; j< umbralizada.getBufferedImage().getHeight(); j++){
				
				Color colorEnUmbralizada = new Color(umbralizada.getBufferedImage().getRGB(i, j));
				if (colorEnUmbralizada.getRed()==255){
					
					imagenFinal.getBufferedImage().setRGB(i, j, Color.RED.getRGB());
					oldImage.setRGB(i, j, Color.RED.getRGB());
					if (flagResultados){
						System.out.println(i + "," + j);
					}
					resultadosX.add(i);
					resultadosY.add(j);
				} else {
					
					oldImage.setRGB(i, j, original.getBufferedImage().getRGB(i, j));
					imagenFinal.getBufferedImage().setRGB(i, j, original.getBufferedImage().getRGB(i, j));
				}
			}
		}
		
		if(flagResultados){			
			fileStreamHarris.flush();
			fileStreamHarris.close();
		}
		
		return imagenFinal;
	}

	/**
	 * cim1 = (Ix2*Iy2 - Ixy^2) - k*(Ix2 + Iy2)^2  con k=0.04
	 * @param imagenConFiltroGaussEnX
	 * @param imagenConFiltroGaussEnY
	 * @param imagenXYConFiltroGauss
	 * @return
	 */
	private static int[][] calcularCim(int[][] Ix2,
			int[][] Iy2, int[][] Ixy2) {
		
		int filas = Ix2.length;
		int columnas = Ix2[0].length;
		int[][] matrizCim = new int[filas][columnas];
		
		for (int f = 0; f < filas; f++) {
			for (int g = 0; g < columnas; g++) {

				matrizCim[f][g] = (int) (((Ix2[f][g]*Iy2[f][g]) - Ixy2[f][g]) - (0.04 * Math.pow((Ix2[f][g] + Iy2[f][g]),2)));
			}
		}
		
		return matrizCim;
	}
	
}
