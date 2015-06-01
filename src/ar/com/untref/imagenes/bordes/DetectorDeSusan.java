package ar.com.untref.imagenes.bordes;

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
	
	public void aplicarMascaraDeSusan(int[][] mascaraSusan, Imagen imagenActual, int indiceI, int indiceJ){
		
		int longitudMascara = mascaraSusan.length;
		
		int[][] matrizRojo = imagenActual.getMatriz(Canal.ROJO);
				
			int coordenadaI = indiceI - (longitudMascara/2);
			int coordenadaJ = indiceJ - (longitudMascara/2);
				
			if (coordenadaI > 0 && coordenadaI < longitudMascara && coordenadaI > 0 && coordenadaJ < longitudMascara){

				mascaraSusan[coordenadaI][coordenadaJ] = (mascaraSusan[coordenadaI][coordenadaJ] * matrizRojo[indiceI][indiceJ]);
			}else{
						
				mascaraSusan[coordenadaI][coordenadaJ] = 0;
			}
	}
				
	
	public int[][] evaluarMascara(int[][] mascaraSusan){
		
		int longitudMascara = mascaraSusan.length;
		
		int[][] matrizResultado = new int[7][7];
		
		for (int i = 0; i < longitudMascara; i++) {
			for (int j = 0; j < longitudMascara; j++) {
				
				float centro = mascaraSusan[i - longitudMascara/2][j - longitudMascara/2];
				
				if (Math.abs(mascaraSusan[i][j] - centro) < 27){
					
					matrizResultado[i][j] = 1;
				}else{
					
					matrizResultado[i][j] = 0;
				}
			}
		
		}
		return matrizResultado;
	}
	
	public int aplicarDetectorDeSusan(Imagen imagenActual){
		
		int[][] mascaraSusan = calcularMascaraDeSusan();
		int sumatoria = 0;
				
		for (int i = 0; i < imagenActual.getBufferedImage().getWidth(); i++) {
			for (int j = 0; j < imagenActual.getBufferedImage().getHeight(); j++) {
			
				aplicarMascaraDeSusan(mascaraSusan, imagenActual, i, j);
				int[][] matrizResultado = evaluarMascara(mascaraSusan);
				sumatoria += matrizResultado[i][j];
			}
		}
		return sumatoria;
	}

}
