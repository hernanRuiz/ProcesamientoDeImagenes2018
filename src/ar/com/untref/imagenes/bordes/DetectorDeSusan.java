package ar.com.untref.imagenes.bordes;

import java.awt.Color;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.modelo.Imagen;

public class DetectorDeSusan {

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
	
	private static void aplicarMascaraDeSusan(int[][] mascaraSusan, int[][] matriz, int indiceK, int indiceL){
		
		int longitudMascara = mascaraSusan.length;
				
			int coordenadaI = (longitudMascara/2);
			int coordenadaJ = (longitudMascara/2);
			
			//ubico el pixel en cuestión en el centro de la mascara
			mascaraSusan[coordenadaI][coordenadaJ] = (mascaraSusan[coordenadaI][coordenadaJ] * matriz[indiceK][indiceL]);
				
			
			//Con la mascara de Susan centrada en el pixel en cuestion
			//calculo los valores en la mascara para todos sus vecinos, si
			//es que existen
			for(int i = 0 ; (coordenadaI + i)< longitudMascara; i++){
				for(int j = 0 ; (coordenadaJ + j)< longitudMascara; j++){
					
					//Valores arriba y abajo del centro de la mascara
					if(indiceL+j>0 && indiceL+j < matriz.length){
						
						mascaraSusan[coordenadaI][coordenadaJ+j] = mascaraSusan[coordenadaI][coordenadaJ+j]; 
					}else{
						
						mascaraSusan[coordenadaI][coordenadaJ+j] = 0; 
					}
					if(indiceL-j>0 && indiceL-j < matriz.length){
						
						mascaraSusan[coordenadaI][coordenadaJ-j] = mascaraSusan[coordenadaI][coordenadaJ-j]; 
					}else{
						
						mascaraSusan[coordenadaI][coordenadaJ-j] = 0;
					}
					
					//Valores a la izquierda
					if (indiceK+i>0 && indiceK+i < matriz[0].length){
						
						mascaraSusan[coordenadaI+i][coordenadaJ] = mascaraSusan[coordenadaI+i][coordenadaJ]; 

						if(indiceL+j>0 && indiceL+j < matriz.length){
							
							mascaraSusan[coordenadaI+i][coordenadaJ+j] = mascaraSusan[coordenadaI+i][coordenadaJ+j]; 
						}else{
							
							mascaraSusan[coordenadaI+i][coordenadaJ+j] = 0; 
						}
						
						if(indiceL-j>0 && indiceL-j < matriz.length){
							
							mascaraSusan[coordenadaI+i][coordenadaJ-j] = mascaraSusan[coordenadaI+i][coordenadaJ-j]; 
						}else{
							mascaraSusan[coordenadaI+i][coordenadaJ-j] = 0; 

						}
					}else{
						
						mascaraSusan[coordenadaI+i][coordenadaJ] = 0; 
					}
					
					//valores a la derecha
					if (indiceK-i>0 && indiceK-i < matriz[0].length){
						
						mascaraSusan[coordenadaI-i][coordenadaJ] = mascaraSusan[coordenadaI-i][coordenadaJ]; 
						
						if(indiceL+j>0 && indiceL+j < matriz.length){
							
							mascaraSusan[coordenadaI-i][coordenadaJ+j] = mascaraSusan[coordenadaI-i][coordenadaJ+j]; 
						}else{
							
							mascaraSusan[coordenadaI-i][coordenadaJ+j] = 0; 
						}
						
						if(indiceL-j>0 && indiceL-j < matriz.length){
							
							mascaraSusan[coordenadaI-i][coordenadaJ-j] = mascaraSusan[coordenadaI-i][coordenadaJ-j]; 
						}else{
							
							mascaraSusan[coordenadaI-i][coordenadaJ-j] = 0; 
						}
					}else{
						
						mascaraSusan[coordenadaI-i][coordenadaJ] = 0; 
					}	
				}
			}	
	}
		
	private static float evaluarMascaraEnIJ(int[][] mascaraSusan){
		
		int longitudMascara = mascaraSusan.length;
		int contador = 0;
		
		for (int i = 0; i < longitudMascara; i++) {
			for (int j = 0; j < longitudMascara; j++) {
				
				float centro = mascaraSusan[longitudMascara/2][longitudMascara/2];
				
				if (Math.abs(mascaraSusan[i][j] - centro) < 27){
					
					contador += 1;
				}
			}
		}
		
		float valorSr0 = 1 - (contador/37);
		
		return valorSr0;
	}
	
	public static void aplicarDetectorDeSusan(Imagen imagen){
		
		int[][] matriz = imagen.getMatriz(Canal.ROJO);
		int[][] mascaraSusan = calcularMascaraDeSusan();
		float valorSr0 = 0;
		
		@SuppressWarnings("unused")
		boolean pintarPixel = false;
				
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
			
				aplicarMascaraDeSusan(mascaraSusan, matriz, i, j);
				valorSr0 = evaluarMascaraEnIJ(mascaraSusan);
				
				if(esEsquina(valorSr0) || esBorde(valorSr0)){
					
					Color color = new Color(0,255,0);
					imagen.getBufferedImage().setRGB(i, j, color.getRGB());
				}
			}
		}
	}

	private static boolean esBorde(float s_ro) {
		float limiteInferior = (float) (0.5 - ((0.75 - 0.5) / 2));
		float limiteSuperior = (float) (0.5 + ((0.75 - 0.5) / 2));

		return s_ro > limiteInferior && s_ro <= limiteSuperior;
	}
	
	private static boolean esEsquina(float s_ro) {
		float limiteInferior = (float) (0.75 - (0.75 - 0.5) / 2);
		float limiteSuperior = (float) (0.75 + (0.75 - 0.5) / 2);

		return s_ro > limiteInferior && s_ro <= limiteSuperior;
	}
}
