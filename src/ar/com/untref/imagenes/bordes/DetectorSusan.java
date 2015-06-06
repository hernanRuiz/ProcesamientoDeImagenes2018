package ar.com.untref.imagenes.bordes;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class DetectorSusan {
	 
 	private static final int TAMANIO_MASCARA = 7;
 	
 	// Se cuentan sólo los píxeles de la imágen circular
 	private static final int CANTIDAD_PIXELES_MASCARA = 37;
 	
 	private static double umbralT = 27.0;
	private static int pixelNegro = new Color(0, 0, 0).getRGB();
	private static int pixelBlanco = new Color(255, 0, 0).getRGB();
 	
 	/**
 	 * Si el resultado es aprox 0, no corresponde a borde ni esquina.
 	 * Si el resultado es aprox 0.5, es un borde.
 	 * Si el resultado es aprox 0.75 es una esquina.
 	 * 
 	 * Por lo tanto, se tomó como criterio que cualquier resultado mayor a 0.4, será considerado borde/esquina.
 	 */
 	private static double criterioDeBordeBordeOEsquina = 0.4;
 	
 	/**
 	 * Aplica una máscara circular de 7x7 con el método de Susan.
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
 	
 	/**
 	 * @return imagen binaria que detecta los bordes y esquinas de la imagen original
 	 */
 	public static BufferedImage aplicar(BufferedImage imagenOriginal) {
 		
 		int[][] mascaraSusan = calcularMascaraDeSusan();
 		
 		BufferedImage resultado = new BufferedImage(imagenOriginal.getWidth(), imagenOriginal.getHeight(), imagenOriginal.getType());
 		
 		for (int i = 0; i < imagenOriginal.getWidth() ; i++) {
 			for (int j = 0; j < imagenOriginal.getHeight() ; j++) {
 				
 				resultado.setRGB(i, j, pixelNegro);
 			}
 		}
 		
 		int sumarEnAncho = (-1) * (TAMANIO_MASCARA / 2);
 		int sumarEnAlto = (-1) * (TAMANIO_MASCARA / 2);
 		
 		// Iterar la imagen, sacando los bordes.
 		for (int i = TAMANIO_MASCARA / 2; i < imagenOriginal.getWidth() - (TAMANIO_MASCARA / 2); i++) {
 			for (int j = TAMANIO_MASCARA / 2; j < imagenOriginal.getHeight() - (TAMANIO_MASCARA / 2); j++) {
 
 				// Tomo el valor del píxel central de la máscara (el (3,3) de la máscara)
 				int indiceICentralDeLaImagen = i + sumarEnAncho + (TAMANIO_MASCARA / 2);
 				int indiceJCentralDeLaImagen = j + sumarEnAlto + (TAMANIO_MASCARA / 2);
 				double valorCentral = new Color(imagenOriginal.getRGB(indiceICentralDeLaImagen, indiceJCentralDeLaImagen)).getRed();
 				
 				int cantidadDePixelesSimilaresAlCentral = 0;

 				// Iterar la máscara
 				for(int iAnchoMascara = 0; iAnchoMascara < TAMANIO_MASCARA; iAnchoMascara++) {
 					for(int iAltoMascara = 0; iAltoMascara < TAMANIO_MASCARA; iAltoMascara++) {
 						
 						int indiceIDeLaImagen = i + sumarEnAncho + iAnchoMascara;
 						int indiceJDeLaImagen = j + sumarEnAlto + iAltoMascara;
 
 						double valor = new Color(imagenOriginal.getRGB(indiceIDeLaImagen, indiceJDeLaImagen)).getRed();
 						
 						// Se multiplica el valor leído por la máscara, para sacar los que no pertenezcan a la parte circular.
 						valor = valor * mascaraSusan[iAnchoMascara][iAltoMascara];
 						
 						if (Math.abs(valor - valorCentral) < umbralT) {
 							
 							cantidadDePixelesSimilaresAlCentral++;
 						}
 					}
 				}
 				// Fin iteración máscara
 				
 				double s = 1.0 - ((double)cantidadDePixelesSimilaresAlCentral / (double)CANTIDAD_PIXELES_MASCARA);
 				
 				if (s > criterioDeBordeBordeOEsquina) {
 					
 					resultado.setRGB(i, j, pixelBlanco);
 				} else {
 					
 					resultado.setRGB(i, j, pixelNegro);
 				}				
 			}
 		}
 		return resultado;
 	}
}
