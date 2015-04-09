package ar.com.untref.imagenes.bordes;

import java.awt.image.BufferedImage;

public class DetectorDeBordes {

	public  void aplicarDetectorDeRoberts(BufferedImage bufferedImage){
		
		int[][] mascaraDeRobertsEnX = new int [2][2];
		mascaraDeRobertsEnX[0][0]= 1;
		mascaraDeRobertsEnX[0][1]= 0;
		mascaraDeRobertsEnX[1][0]= 0;
		mascaraDeRobertsEnX[1][1]= -1;
		
		int[][] mascaraDeRobertsEnY = new int [2][2];
		mascaraDeRobertsEnY[0][0]= 0;
		mascaraDeRobertsEnY[0][1]= 1;
		mascaraDeRobertsEnY[1][0]= -1;
		mascaraDeRobertsEnY[1][1]= 0;
		
	}
	
	public  void aplicarDetectorDePrewitt(BufferedImage bufferedImage){
		
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
		
	}
	
	public  void aplicarDetectorDeSobel(BufferedImage bufferedImage){
		
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
		
	}
	
	public  void aplicarDetectorLaplaciano(BufferedImage bufferedImage){
		
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
		
	}
	
}
