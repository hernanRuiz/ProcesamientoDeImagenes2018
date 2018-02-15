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
 	
 	//Se cuentan sólo los píxeles de la imagen circular
 	private static final int CANTIDAD_PIXELES_MASCARA = 37;
 	
 	private static double umbralT = 27.0;
	private static java.awt.Color pixelRojo = new java.awt.Color(255,0,0);	
	
 	/**
 	 * Si el resultado es aprox 0.75 no corresponde a borde ni esquina.
 	 * Si el resultado es aprox 0.5, es un borde.
 	 * Si el resultado es aprox 0.25 es una esquina.
 	 * 
 	 * Por lo tanto, se tom� como criterio que cualquier resultado mayor a 0.4, ser� considerado borde/esquina.
 	 */
	private static double criterioDeSierra = 0.75;
 	private static double criterioDeBorde = 0.5;
 	private static double criterioDeEsquina = 0.25;
 	
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
		
 		//En ejecución indivdual seteamos archivo de salida
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
				
				/*Se compara la intensidad de cada pixel de la máscara con la intensidad
				del pixel central de la misma*/
				for (int i = 0; i < TAMANIO_MASCARA; i++) {
					for (int j = 0; j < TAMANIO_MASCARA; j++) {
						int currentMaskColor = oldImage.getGray(x + i - 3, y + j - 3);
						if (susanMask[i][j] != 0 && Math.abs(currentMaskColor - currentColor) < umbralT) {
							pixelsWithinColorRange++;
						}
					}
				}
				
				Sr0 = 1 - (pixelsWithinColorRange / CANTIDAD_PIXELES_MASCARA);
				
				switch (flagDetector) {

 				case "Esquinas":
 					
 					if((Math.abs( Sr0 - criterioDeEsquina) <= 0.01)){
 	 					
 						oldImage.setRGB(x, y, pixelRojo.getRGB());
 						if (flagResultados){
 							System.out.println(x + "," + y);
 						}
 						
 						resultadosX.add(x);
 						resultadosY.add(y);
 	 				}

 				case "Bordes":
 					if(Math.abs( Sr0 - criterioDeBorde) < 0.1){
 	 					
 						oldImage.setRGB(x, y, pixelRojo.getRGB());
 						if (flagResultados){
 							System.out.println(x + "," + y);
 						}
 						
 						resultadosX.add(x);
 						resultadosY.add(y);
 	 				}
 					break;

 				case "Sierras":
 					if(Math.abs( Sr0 - criterioDeSierra) < 0.3){
 						
 						oldImage.setRGB(x, y, pixelRojo.getRGB());
 						if (flagResultados){
 							System.out.println(x + "," + y);
 						}
 						
 						resultadosX.add(x);
 						resultadosY.add(y); 						
 					}
 					break;
 					
 				case "EsquinasYBordes":
 					if((Math.abs( Sr0 - criterioDeBorde) < 0.1) 
 							|| (Math.abs( Sr0 - criterioDeEsquina) <= 0.01)){
 	 					
 						oldImage.setRGB(x, y, pixelRojo.getRGB());
 						if (flagResultados){
 							System.out.println(x + "," + y);
 						}
 						
 						resultadosX.add(x);
 						resultadosY.add(y);
 	 				}
 					break;
 					
 				case "EsquinasYSierras":
 					if((Math.abs( Sr0 - criterioDeSierra) < 0.3) 
 							|| (Math.abs( Sr0 - criterioDeEsquina) <= 0.01)){
 	 					
 						oldImage.setRGB(x, y, pixelRojo.getRGB());
 						if (flagResultados){
 							System.out.println(x + "," + y);
 						}
 						
 						resultadosX.add(x);
 						resultadosY.add(y);
 	 				}
 					break;
 				
 				case "BordesYSierras":
 					if((Math.abs( Sr0 - criterioDeBorde) < 0.1) 
 							|| (Math.abs( Sr0 - criterioDeSierra) < 0.3)){
 	 					
 						oldImage.setRGB(x, y, pixelRojo.getRGB());
 						if (flagResultados){
 							System.out.println(x + "," + y);
 						}
 						
 						resultadosX.add(x);
 						resultadosY.add(y);
 	 				}
 					break;
 				
 				case "BordesSierrasYEsquinas":
 					if((Math.abs( Sr0 - criterioDeBorde) < 0.1) 
 							|| (Math.abs( Sr0 - criterioDeEsquina) <= 0.01)
 							|| (Math.abs( Sr0 - criterioDeSierra) < 0.3)){
 	 					
 						oldImage.setRGB(x, y, pixelRojo.getRGB());
 						if (flagResultados){
 							System.out.println(x + "," + y);
 						}
 						
 						resultadosX.add(x);
 						resultadosY.add(y);
 	 				}
					break;
				}
				
			}
		}
		
		superponerAImagenOriginal(newImage, oldImage);
		
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
