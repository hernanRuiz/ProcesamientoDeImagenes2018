package ar.com.untref.imagenes.bordes;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.modelo.Imagen;

public class DetectorSusan {
	 
 	private static final int TAMANIO_MASCARA = 7;
 	
 	// Se cuentan sólo los píxeles de la imagen circular
 	private static final int CANTIDAD_PIXELES_MASCARA = 37;
 	
 	private static double umbralT = 27.0;
	//private static int pixelNegro = new Color(0, 0, 0).getRGB();
	private static java.awt.Color pixelRojo = new java.awt.Color(255,0,0);
	private static java.awt.Color pixelVerde = new java.awt.Color(0,255,0);
	private static java.awt.Color pixelAzul = new java.awt.Color(0,0,255);
	
	
 	/**
 	 * Si el resultado es aprox 0, no corresponde a borde ni esquina.
 	 * Si el resultado es aprox 0.5, es un borde.
 	 * Si el resultado es aprox 0.75 es una esquina.
 	 * 
 	 * Por lo tanto, se tom� como criterio que cualquier resultado mayor a 0.4, ser� considerado borde/esquina.
 	 */
	private static double criterioDeSierra = 0.25;
 	private static double criterioDeBorde = 0.5;
 	private static double criterioDeEsquina = 0.75;
 	
 	static Color result = new Color(0);
 	
 	public static float red;
	public float green;
	public float blue;
	
	private static List<Integer> resultadosX = new LinkedList<Integer>();
	private static List<Integer> resultadosY = new LinkedList<Integer>();
	private static PrintStream fileStreamSusan; 	
 	
 	/**
 	 * Aplica una m�scara circular de 7x7 con el m�todo de Susan.
 	 */
 	public DetectorSusan() {
 		
 		calcularMascaraDeSusan();
 	}
 
 	private static int[][] calcularMascaraDeSusan() {
		
 		int[][] mascaraDeSusan = new int [7][7];

		mascaraDeSusan[0][0]= 0;
		mascaraDeSusan[0][1]= 0;
		mascaraDeSusan[0][2]= 1;
		mascaraDeSusan[0][3]= 1;
		mascaraDeSusan[0][4]= 1;
		mascaraDeSusan[0][5]= 0;
		mascaraDeSusan[0][6]= 0;
		
		mascaraDeSusan[1][0]= 0;
		mascaraDeSusan[1][1]= 1;
		mascaraDeSusan[1][2]= 1;
		mascaraDeSusan[1][3]= 1;
		mascaraDeSusan[1][4]= 1;
		mascaraDeSusan[1][5]= 1;
		mascaraDeSusan[1][6]= 0;
		
		mascaraDeSusan[2][0]= 1;
		mascaraDeSusan[2][1]= 1;
		mascaraDeSusan[2][2]= 1;
		mascaraDeSusan[2][3]= 1;
		mascaraDeSusan[2][4]= 1;
		mascaraDeSusan[2][5]= 1;
		mascaraDeSusan[2][6]= 1;
		
		mascaraDeSusan[3][0]= 1;
		mascaraDeSusan[3][1]= 1;
		mascaraDeSusan[3][2]= 1;
		mascaraDeSusan[3][3]= 1;
		mascaraDeSusan[3][4]= 1;
		mascaraDeSusan[3][5]= 1;
		mascaraDeSusan[3][6]= 1;
		
		mascaraDeSusan[4][0]= 1;
		mascaraDeSusan[4][1]= 1;
		mascaraDeSusan[4][2]= 1;
		mascaraDeSusan[4][3]= 1;
		mascaraDeSusan[4][4]= 1;
		mascaraDeSusan[4][5]= 1;
		mascaraDeSusan[4][6]= 1;
		
		mascaraDeSusan[5][0]= 0;
		mascaraDeSusan[5][1]= 1;
		mascaraDeSusan[5][2]= 1;
		mascaraDeSusan[5][3]= 1;
		mascaraDeSusan[5][4]= 1;
		mascaraDeSusan[5][5]= 1;
		mascaraDeSusan[5][6]= 0;
		
		mascaraDeSusan[6][0]= 0;
		mascaraDeSusan[6][1]= 0;
		mascaraDeSusan[6][2]= 1;
		mascaraDeSusan[6][3]= 1;
		mascaraDeSusan[6][4]= 1;
		mascaraDeSusan[6][5]= 0;
		mascaraDeSusan[6][6]= 0;
		
		return mascaraDeSusan;
	}
 	
 	
 	public static BufferedImage aplicar(Imagen image, final String flagDetector, boolean flagResultados) {
		
 		if(flagResultados){			
 			try {
 				fileStreamSusan = new PrintStream("Salida_algoritmo_Susan.txt");
 				fileStreamSusan.flush();
 				System.setOut(fileStreamSusan);
 				System.out.println("Puntos detectados:");
 				System.out.println();
 			} catch (FileNotFoundException e) {
 				e.printStackTrace();
 			}
		}
 		
 		CustomBufferedImage oldImage = new CustomBufferedImage(image.getBufferedImage());
		int height = oldImage.getHeight();
		int width = oldImage.getWidth();
		CustomBufferedImage newImage = new CustomBufferedImage(width, height, oldImage.getType());
		int[][] susanMask = calcularMascaraDeSusan();
		double Sr0 = 0;
		
		for (int x = 3; x < width - 3; x++) {
			for (int y = 3; y < height - 3; y++) {
				
				int currentColor = oldImage.getGray(x, y);
				double pixelsWithinColorRange = 0;
				
				for (int i = 0; i < 7; i++) {
					for (int j = 0; j < 7; j++) {
						int currentMaskColor = oldImage.getGray(x + i - 3, y + j - 3);
						if (susanMask[i][j] != 0 && Math.abs(currentMaskColor - currentColor) < umbralT) {
							pixelsWithinColorRange++;
						}
					}
				}
				
				Sr0 = 1 - (pixelsWithinColorRange / CANTIDAD_PIXELES_MASCARA);
				
				switch (flagDetector) {

 				case "Esquinas":
 					
 					double lowLimit = 0.75 - (0.75 - 0.5) / 2;
 					double highLimit = 0.75 + (0.75 - 0.5) / 2;
 					
 					if((Math.abs( Sr0 - 0.25) <= 0.01)){
 	 					
 						oldImage.setRGB(x, y, pixelRojo.getRGB());
 						if (flagResultados){
 							System.out.println(x + "," + y);
 						}
 						
 						resultadosX.add(x);
 						resultadosY.add(y);
 	 				}
 					
 					/*if(Math.abs( Sr0 - criterioDeBorde) < 0.15){
 	 					
 						oldImage.setRGB(x, y, pixelVerde.getRGB());
 	 				}
 					
 					if(Math.abs( Sr0 - criterioDeSierra) < 0.15){
 	 					
 						oldImage.setRGB(x, y, pixelAzul.getRGB());
 	 				}
 					break;*/

 				/*case "Bordes":
 					if(Math.abs( Sr0 - criterioDeBorde) < 0.1){
 	 					
 	 					imagenResultante.getBufferedImage().setRGB(i, j, pixelRojo);
 	 				} else {
 	 					
 	 					//imagenResultante.getBufferedImage().setRGB(i, j, pixelNegro);
 	 				}
 					break;

 				case "Sierras":
 					if(Math.abs( Sr0 - criterioDeSierra) < 0.1){
 	 					
 	 					imagenResultante.getBufferedImage().setRGB(i, j, pixelRojo);
 	 				} else {
 	 					
 	 					//imagenResultante.getBufferedImage().setRGB(i, j, pixelNegro);
 	 				}
 					break;
 					
 				case "EsquinasYBordes":
 					if(Math.abs( Sr0 - criterioDeEsquina) < 0.1 || Math.abs( Sr0 - criterioDeBorde) < 0.1){
 	 					
 	 					imagenResultante.getBufferedImage().setRGB(i, j, pixelRojo);
 	 				} else {
 	 					
 	 					//imagenResultante.getBufferedImage().setRGB(i, j, pixelNegro);
 	 				}
 					break;
 					
 				case "EsquinasYSierras":
 					if(Math.abs( Sr0 - criterioDeEsquina) < 0.1 || Math.abs( Sr0 - criterioDeSierra) < 0.1){
 	 					
 	 					imagenResultante.getBufferedImage().setRGB(i, j, pixelRojo);
 	 				} else {
 	 					
 	 					//imagenResultante.getBufferedImage().setRGB(i, j, pixelNegro);
 	 				}
 					break;
 				
 				case "BordesYSierras":
 					if(Math.abs( Sr0 - criterioDeBorde) < 0.1 || Math.abs( Sr0 - criterioDeSierra) < 0.1){
 	 					
 	 					imagenResultante.getBufferedImage().setRGB(i, j, pixelRojo);
 	 				} else {
 	 					
 	 					//imagenResultante.getBufferedImage().setRGB(i, j, pixelNegro);
 	 				}
 					break;
 				
 				case "BordesSierrasYEsquinas":
					if(Math.abs( Sr0 - criterioDeEsquina) < 0.1 || Math.abs( Sr0 - criterioDeBorde) < 0.1 || Math.abs( Sr0 - criterioDeSierra) < 0.1){
	 					
	 					imagenResultante.getBufferedImage().setRGB(i, j, pixelRojo);
	 				} else {
	 					
	 					//imagenResultante.getBufferedImage().setRGB(i, j, pixelNegro);
	 				}
					break;*/
				}
				
				/*if (Math.abs(s - 0d) < 0.15d) {
					newImage.setGray(x, y, 0);
				} else if (Math.abs(s - 0.5d) <= 0.15d) {
					// newImage.setGray(x, y, 255);
					newImage.setRGB(x, y, 65280);
					oldImage.setRGB(x, y, 65280);
				} else if (Math.abs(s - 0.75d) <= 0.15d) {
					// newImage.setGray(x, y, 255);
					newImage.setRGB(x, y, 16711680);
					oldImage.setRGB(x, y, 16711680);
				}*/
			}
		}
		
		Imagen imagenFinal = superponerAImagenOriginal(newImage, oldImage);
		
		if(flagResultados){			
			fileStreamSusan.flush();
			fileStreamSusan.close();
		}
		
		return oldImage;
 	}
 	
 	
 	 	private static Imagen superponerAImagenOriginal(BufferedImage umbralizada, BufferedImage original) {

		Imagen imagenFinal = new Imagen(new BufferedImage(umbralizada.getWidth(), umbralizada.getHeight(), umbralizada.getType()), FormatoDeImagen.JPEG, "final");
		
		for (int i=0; i< umbralizada.getWidth(); i++){
			for (int j=0; j< umbralizada.getHeight(); j++){
				
				Color colorEnUmbralizada = new Color(umbralizada.getRGB(i, j));
				if (colorEnUmbralizada.getRed()==255){
					
					imagenFinal.getBufferedImage().setRGB(i, j, pixelRojo.getRGB());
				
				} else {
					
					imagenFinal.getBufferedImage().setRGB(i, j, original.getRGB(i, j));
				}
			}
		}
		
		return imagenFinal;
	}
 	 	
	public static List<Integer> getResultadosX() {
		return resultadosX;
	}

	public static void setResultadosX(List<Integer> resultadosX) {
		DetectorSusan.resultadosX = resultadosX;
	}

	public static List<Integer> getResultadosY() {
		return resultadosY;
	}

	public static void setResultadosY(List<Integer> resultadosY) {
		DetectorSusan.resultadosY = resultadosY;
	}
}
