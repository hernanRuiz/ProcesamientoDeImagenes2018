package ar.com.untref.imagenes.filtros;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.modelo.Imagen;

public class FiltroNuevo {

	private float[][] mascara;
	
	public FiltroNuevo(float[][] mascara){
		
		this.setMascara(mascara);
	}
	
	private void setMascara(float[][] mascara) {

		this.mascara = mascara;
	}

	/**
	 * Filtra por el canal adecuado usando la matriz de la imagen por parametro. Se altera la matriz de la imagen
	 * original, por eso no devuelve nada, porque ya queda modificada.
	 * @param imagenOriginal
	 * @param mascara
	 * @param canal
	 */
	public int[][] filtrar(Imagen imagenOriginal, Canal canal){
		
		int anchoMascara = mascara[0].length;
		int altoMascara = mascara.length;
		int sumarEnAncho = (-1) * (anchoMascara / 2);
		int sumarEnAlto = (-1) * (altoMascara / 2);
		
		Imagen imagenSinCambios = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(),imagenOriginal.getNombre(), imagenOriginal.getMatriz(Canal.ROJO), imagenOriginal.getMatriz(Canal.VERDE), imagenOriginal.getMatriz(Canal.AZUL));

		int[][] matriz = new int[imagenSinCambios.getBufferedImage().getWidth()][imagenSinCambios.getBufferedImage().getHeight()];
		
		int[][] matrizTranspuesta = new int[imagenSinCambios.getBufferedImage().getHeight()][imagenSinCambios.getBufferedImage().getWidth()];
				
		 for(int j = 0; j < imagenSinCambios.getMatriz(canal)[0].length; j++){
	            for(int i = 0; i < imagenSinCambios.getMatriz(canal).length; i++){
	                matrizTranspuesta[i][j] = imagenSinCambios.getMatriz(canal)[i][j]; 
	            }
	        }
		
		// Iterar la imagen, sacando los bordes.
		for (int i = anchoMascara / 2; i < imagenSinCambios.getBufferedImage().getWidth()
				- (anchoMascara / 2); i++) {
			for (int j = altoMascara / 2; j < imagenSinCambios.getBufferedImage().getHeight()
					- (altoMascara / 2); j++) {

				float sumatoria = 0f;
				// Iterar la máscara
				for (int iAnchoMascara = 0; iAnchoMascara < anchoMascara; iAnchoMascara++) {
					for (int iAltoMascara = 0; iAltoMascara < altoMascara; iAltoMascara++) {

						int indiceIDeLaImagen = i + sumarEnAncho+ iAnchoMascara;
						int indiceJDeLaImagen = j + sumarEnAlto + iAltoMascara;

						int nivelDeGris = matrizTranspuesta[indiceJDeLaImagen][indiceIDeLaImagen];
						sumatoria += nivelDeGris * mascara[iAnchoMascara][iAltoMascara];
					}
				}

				matriz[i][j] = (int) sumatoria;
			}
		}
		
		return matriz;
	}

	public float[][] getMascara() {
		return mascara;
	}

}
