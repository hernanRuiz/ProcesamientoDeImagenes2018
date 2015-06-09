package ar.com.untref.imagenes.procesamiento;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import ar.com.untref.imagenes.modelo.Histograma;
import ar.com.untref.imagenes.modelo.Imagen;

public class Umbralizador {

	/**
	 * @param imagen - imagen a umbralizar
	 * @param umbral - valor que hará de separador entre valores 0 y 255
	 */
	public static Imagen umbralizarImagen(Imagen imagenOriginal, int umbral){
		
		BufferedImage buffered = new BufferedImage(imagenOriginal.getBufferedImage().getWidth(), imagenOriginal.getBufferedImage().getHeight(), imagenOriginal.getBufferedImage().getType());
		
		for (int x = 0; x < buffered.getWidth(); x++) {
			for (int y = 0; y < buffered.getHeight(); y++) {

				int rgba = imagenOriginal.getBufferedImage().getRGB(x, y);
				Color col = new Color(rgba, true);
				
				if ( col.getRed()<= umbral){
					
					col = new Color(0,0,0);
				} else {
					
					col = new Color(255,255,255);
				}
				buffered.setRGB(x, y, col.getRGB());
			}
		}
		
		return new Imagen(buffered, imagenOriginal.getFormato(), imagenOriginal.getNombre());
	}
	
	public static int encontrarNuevoUmbralGlobal(Imagen imagenOriginal, int umbral){
		
		int[] mediasDelUmbral = Umbralizador.calcularMediasDelUmbral(imagenOriginal, umbral); 
		
		int primeraMedia = mediasDelUmbral[0];
		int segundaMedia = mediasDelUmbral[1];
		
		int nuevoUmbral = (int) (0.5 * (primeraMedia + segundaMedia));
		
		if (nuevoUmbral > 255){
			
			nuevoUmbral = 255;
		} else if( nuevoUmbral < 0){
			
			nuevoUmbral = 0;
		}
		
		return nuevoUmbral;
	}

	public static int[] calcularMediasDelUmbral(Imagen imagenOriginal, int umbral) {
		
		List<Integer> valorOriginalPixelesGrupo1 = new ArrayList<Integer>();
		List<Integer> valorOriginalPixelesGrupo2 = new ArrayList<Integer>();
		
		int[] medias = new int[2];
		
		
		BufferedImage buffered = new BufferedImage(imagenOriginal.getBufferedImage().getWidth(), imagenOriginal.getBufferedImage().getHeight(), imagenOriginal.getBufferedImage().getType());
		
		for (int x = 0; x < buffered.getWidth(); x++) {
			for (int y = 0; y < buffered.getHeight(); y++) {

				int rgba = imagenOriginal.getBufferedImage().getRGB(x, y);
				Color col = new Color(rgba, true);
				
				if ( col.getRed()<= umbral){
					
					valorOriginalPixelesGrupo1.add(col.getRed());
				} else {
					
					valorOriginalPixelesGrupo2.add(col.getRed());
				}
			}
		}
		
		int sumatoria1 = obtenerSumatoria(valorOriginalPixelesGrupo1);
		int sumatoria2 = obtenerSumatoria(valorOriginalPixelesGrupo2);

		
		medias[0] = (int) sumatoria1 / valorOriginalPixelesGrupo1.size();
		medias[1] = (int) sumatoria2 / valorOriginalPixelesGrupo2.size();
		
		return medias;
	}

	private static int obtenerSumatoria(List<Integer> valorOriginalPixelesGrupo) {

		int sumatoria = 0;
		
		for (Integer colorActual: valorOriginalPixelesGrupo){
			
			sumatoria += colorActual;
		}
		
		return sumatoria;
	}
	
	public static Imagen generarUmbralizacionOtsu(Imagen imagen) {
				
		float[] probabilidadesDeOcurrencia = Histograma.calcularHistogramaRojo(imagen.getBufferedImage());
		int sigmaMaximo = 0;
		int umbralMaximo = 0;

		for (int t=0; t<probabilidadesDeOcurrencia.length; t++){

			float w1 = 0;
			float w2 = 0;
			float u1 = 0;
			float u2 = 0;

			for (int i = 0 ; i < probabilidadesDeOcurrencia.length ; i++) {
				
				if (i < t) {
					w1 += probabilidadesDeOcurrencia[i];
				} else {
					w2 += probabilidadesDeOcurrencia[i];
				}
			}
			
			for (int i = 1 ; i <= probabilidadesDeOcurrencia.length ; i++) {
				
				if (i < t) {
					
					u1 += i * probabilidadesDeOcurrencia[i-1] / w1;
				} else {
					
					u2 += i * probabilidadesDeOcurrencia[i-1] / w2;
				}
			}
			
			int ut = (int) (w1*u1 + w2*u2);
			
			int varianzaCuadrado = (int) ( (w1 * Math.pow( (u1-ut), 2) ) + (w2 * Math.pow( (u2-ut), 2) ));
			
			if (varianzaCuadrado > sigmaMaximo){
				
				sigmaMaximo = varianzaCuadrado;
				umbralMaximo = t;
			}
		}
		
		JOptionPane.showMessageDialog(null, "Umbral Calculado: " + String.valueOf(umbralMaximo));
		
		return umbralizarImagen(imagen, umbralMaximo);
	}
	
}
