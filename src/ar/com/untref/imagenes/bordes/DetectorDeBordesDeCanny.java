package ar.com.untref.imagenes.bordes;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.filtros.FiltroNuevo;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.modelo.MatrizDeColores;
import ar.com.untref.imagenes.procesamiento.MatricesManager;

public class DetectorDeBordesDeCanny {
	
	public static Imagen mostrarImagenNoMaximos(Imagen imagenOriginal){
		
		MatrizDeColores matrices = calcularSupresionNoMaximos(imagenOriginal);
		
		int[][] matrizR = MatricesManager.aplicarTransformacionLineal(matrices.getMatrizRojos());
		int[][] matrizV = MatricesManager.aplicarTransformacionLineal(matrices.getMatrizVerdes());
		int[][] matrizA = MatricesManager.aplicarTransformacionLineal(matrices.getMatrizAzules());
		
		Imagen imagenResultante = new Imagen(MatricesManager.obtenerImagenDeMatrices(matrizR, matrizV, matrizA), imagenOriginal.getFormato(), imagenOriginal.getNombre()+"_nomaximos");
		
		return imagenResultante;
	}
	
	public static MatrizDeColores calcularSupresionNoMaximos(Imagen imagenOriginal){
		
		float[][] mascaraDeSobelEnX = DetectorDeBordes.calcularMascaraDeSobelEnX();
		float[][] mascaraDeSobelEnY = DetectorDeBordes.calcularMascaraDeSobelEnY();
		
		Imagen imagenFiltradaEnX = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		Imagen imagenFiltradaEnY = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));
		
        FiltroNuevo filtroEnX = new FiltroNuevo(mascaraDeSobelEnX);
        FiltroNuevo filtroEnY = new FiltroNuevo(mascaraDeSobelEnY);
        
        //Aplicamos filtros en X y en Y
        int[][] matrizRojoEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.ROJO);
        int[][] matrizVerdeEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.VERDE);
        int[][] matrizAzulEnX = filtroEnX.filtrar(imagenFiltradaEnX, Canal.AZUL);
        		
        int[][] matrizRojoEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.ROJO);
        int[][] matrizVerdeEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.VERDE);
        int[][] matrizAzulEnY = filtroEnY.filtrar(imagenFiltradaEnY, Canal.AZUL);
        		
        //Sintetizamos usando la raiz de los cuadrados
        int[][] matrizRojosSintetizados = DetectorDeBordes.sintetizar(matrizRojoEnX, matrizRojoEnY);
        int[][] matrizVerdesSintetizados = DetectorDeBordes.sintetizar(matrizVerdeEnX, matrizVerdeEnY);
        int[][] matrizAzulesSintetizados = DetectorDeBordes.sintetizar(matrizAzulEnX, matrizAzulEnY);

		int[][] matrizDeAngulosCanalRojo = calcularAnguloDelGradiente(matrizRojoEnX, matrizRojoEnY);
		int[][] matrizDeAngulosCanalVerde = calcularAnguloDelGradiente(matrizVerdeEnX, matrizVerdeEnY);
		int[][] matrizDeAngulosCanalAzul = calcularAnguloDelGradiente(matrizAzulEnX, matrizAzulEnY);
        
		
		int[][] matrizBordesRojos = buscarBordesImportantes(matrizRojosSintetizados, matrizDeAngulosCanalRojo);
		int[][] matrizBordesVerdes = buscarBordesImportantes(matrizVerdesSintetizados, matrizDeAngulosCanalVerde);
		int[][] matrizBordesAzules = buscarBordesImportantes(matrizAzulesSintetizados, matrizDeAngulosCanalAzul);
        
		MatrizDeColores matrizFinal = new MatrizDeColores(matrizBordesRojos, matrizBordesVerdes, matrizBordesAzules);		
		
		return matrizFinal;
	}
	
	private static int[][] buscarBordesImportantes(int[][] matriz, int[][] matrizAngulos) {

		for (int x = 0; x < matriz.length; x++) {
			for (int y = 0; y < matriz[0].length; y++) {

				int[] pixelesAMirar = obtenerPixelesAMirar(matriz, matrizAngulos, x, y);
				int pixelAMirar1 = pixelesAMirar[0];
				int pixelAMirar2 = pixelesAMirar[1];
				
				if (pixelAMirar1 > matriz[x][y] || pixelAMirar2 > matriz[x][y]){
					
					matriz[x][y] = 0;
				}
			}
		}
		
		return matriz;
	}

	private static int[] obtenerPixelesAMirar(int[][] matrizDeMagnitud, int[][] matrizAngulos, int x, int y) {
		
		int[] pixeles = new int[]{0,0};
		
		if (matrizAngulos[x][y]==0 && x>0 && x < matrizDeMagnitud[0].length -1){
			
			pixeles[0] = matrizDeMagnitud[x-1][y];
			pixeles[1] = matrizDeMagnitud[x+1][y];
		} else if (matrizAngulos[x][y] == 45 && x>0 && x<matrizDeMagnitud[0].length -1 && y>0 && y<matrizDeMagnitud.length-1){
			
			pixeles[0] = matrizDeMagnitud[x+1][y-1];
			pixeles[1] = matrizDeMagnitud[x-1][y+1];
		} else if (matrizAngulos[x][y] == 90 && y>0 && y<matrizDeMagnitud.length-1){
			
			pixeles[0] = matrizDeMagnitud[x][y-1];
			pixeles[1] = matrizDeMagnitud[x][y+1];
		} else if (matrizAngulos[x][y] == 45 && x>0 && x<matrizDeMagnitud[0].length-1 && y>0 && y<matrizDeMagnitud.length-1){
			
			pixeles[0] = matrizDeMagnitud[x-1][y+1];
			pixeles[1] = matrizDeMagnitud[x+1][y-1];
		}
		
		return pixeles;
	}

	public static int[][] calcularAnguloDelGradiente(int[][] matrizX, int[][] matrizY){

		int[][] matrizDeAngulos = new int[matrizX.length][matrizX.length];
		
		for (int i=0; i<matrizX.length ;i++){
			for (int j=0; j<matrizX[0].length ;j++){
				
				float arcTan = (float) Math.atan((float) matrizX[i][j] / matrizY[i][j]);
                double grados = Math.toDegrees(arcTan);
                
                if ( grados < 0){
                	
                	grados = 180 + grados;
                }

                matrizDeAngulos[i][j] = discretizarAngulo(grados);
			}
		}
		
		return matrizDeAngulos;
	}

	private static int discretizarAngulo(double angulo) {

		int anguloResultante = 0;
		
		if ( angulo >= 22.5 && angulo <=67.5 ){
			
			anguloResultante = 45;
		} else if ( angulo >= 67.5 && angulo <=112.5 ){
			
			anguloResultante = 90;
		} else if ( angulo >= 112.5 && angulo <=157.5 ){
			
			anguloResultante = 135;
		}
		
		return anguloResultante;
	}

}
