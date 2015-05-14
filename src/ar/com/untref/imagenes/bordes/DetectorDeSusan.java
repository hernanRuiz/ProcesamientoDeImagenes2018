package ar.com.untref.imagenes.bordes;

public class DetectorDeSusan {

	private static int[][] calcularMascaraDeSusan() {
		int[][] mascaraDeSusan = new int [7][7];

		mascaraDeSusan[0][0]= 0;
		mascaraDeSusan[0][1]= 0;
		mascaraDeSusan[0][2]= 255;
		mascaraDeSusan[0][3]= 255;
		mascaraDeSusan[0][4]= 255;
		mascaraDeSusan[0][5]= 0;
		mascaraDeSusan[0][6]= 0;
		
		mascaraDeSusan[1][0]= 0;
		mascaraDeSusan[1][1]= 255;
		mascaraDeSusan[1][2]= 255;
		mascaraDeSusan[1][3]= 255;
		mascaraDeSusan[1][4]= 255;
		mascaraDeSusan[1][5]= 255;
		mascaraDeSusan[1][6]= 0;
		
		mascaraDeSusan[2][0]= 255;
		mascaraDeSusan[2][1]= 255;
		mascaraDeSusan[2][2]= 255;
		mascaraDeSusan[2][3]= 255;
		mascaraDeSusan[2][4]= 255;
		mascaraDeSusan[2][5]= 255;
		mascaraDeSusan[2][6]= 255;
		
		mascaraDeSusan[3][0]= 255;
		mascaraDeSusan[3][1]= 255;
		mascaraDeSusan[3][2]= 255;
		mascaraDeSusan[3][3]= 255;
		mascaraDeSusan[3][4]= 255;
		mascaraDeSusan[3][5]= 255;
		mascaraDeSusan[3][6]= 255;
		
		mascaraDeSusan[4][0]= 255;
		mascaraDeSusan[4][1]= 255;
		mascaraDeSusan[4][2]= 255;
		mascaraDeSusan[4][3]= 255;
		mascaraDeSusan[4][4]= 255;
		mascaraDeSusan[4][5]= 255;
		mascaraDeSusan[4][6]= 255;
		
		mascaraDeSusan[5][0]= 0;
		mascaraDeSusan[5][1]= 255;
		mascaraDeSusan[5][2]= 255;
		mascaraDeSusan[5][3]= 255;
		mascaraDeSusan[5][4]= 255;
		mascaraDeSusan[5][5]= 255;
		mascaraDeSusan[5][6]= 0;
		
		mascaraDeSusan[6][0]= 0;
		mascaraDeSusan[6][1]= 0;
		mascaraDeSusan[6][2]= 255;
		mascaraDeSusan[6][3]= 255;
		mascaraDeSusan[6][4]= 255;
		mascaraDeSusan[6][5]= 0;
		mascaraDeSusan[6][6]= 0;
		
		return mascaraDeSusan;
	}
	
	
	
}
