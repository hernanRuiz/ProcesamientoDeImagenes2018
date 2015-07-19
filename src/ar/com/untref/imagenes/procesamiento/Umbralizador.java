package ar.com.untref.imagenes.procesamiento;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.modelo.ClaseOtsu;
import ar.com.untref.imagenes.modelo.Histograma;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.modelo.Pixel;
import ar.com.untref.imagenes.modelo.VarianzaInterclase;

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
	
	public static int generarUmbralizacionOtsu(Imagen imagen, Canal canal) {
				
		float[] probabilidadesDeOcurrencia;
		
		if (canal.equals(Canal.ROJO)){
			probabilidadesDeOcurrencia = Histograma.calcularHistogramaRojo(imagen.getBufferedImage());
		} else if (canal.equals(Canal.VERDE)){
			
			probabilidadesDeOcurrencia = Histograma.calcularHistogramaVerde(imagen.getBufferedImage());
		} else {
			
			probabilidadesDeOcurrencia = Histograma.calcularHistogramaAzul(imagen.getBufferedImage());
		}
		
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
			
			//Esperanzas
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
		
		return umbralMaximo;
	}
	
	
	public static Imagen generarUmbralizacionColor(Imagen imagen) {
		
		//Paso 1: Calcular los umbrales por banda. En este caso, usando Otsu.
		int umbralOtsuRojo = generarUmbralizacionOtsu(imagen, Canal.ROJO);
		int umbralOtsuVerde = generarUmbralizacionOtsu(imagen, Canal.ROJO);
		int umbralOtsuAzul = generarUmbralizacionOtsu(imagen, Canal.ROJO);
		
		//Paso 2: Generar las clases segun si el r,g,b de cada pixel de la imagen supera o no el umbral de esa banda
		//Las clases son:  C4- 1,0,0    C2- 0,1,0    C1- 0,0,1
		//				   C6- 1,1,0    C5- 1,0,1    C3- 0,1,1
		//				   C0- 0,0,0    C7- 1,1,1    
			
		Map<Integer, ClaseOtsu> mapaDeClases = new HashMap<Integer, ClaseOtsu>();
		
		cargarMapaDeClases(mapaDeClases);
		
		for (int x = 0; x < imagen.getBufferedImage().getWidth(); x++) {
			for (int y = 0; y < imagen.getBufferedImage().getHeight(); y++) {

				int rgba = imagen.getBufferedImage().getRGB(x, y);
				Color colorActual = new Color(rgba, true);
				
				int rojoBinario = 0;
				int verdeBinario = 0;
				int azulBinario = 0;
				
				if (colorActual.getRed() > umbralOtsuRojo){
					
					rojoBinario = 1;
				}
				
				if (colorActual.getGreen() > umbralOtsuVerde){
									
					verdeBinario = 1;
				}
				
				if (colorActual.getBlue() > umbralOtsuAzul){
					
					azulBinario = 1;
				}
				
				int indice = (rojoBinario*4) + (verdeBinario*2) + (azulBinario*1);
				
				Pixel pixel = new Pixel(x,y,colorActual);
				
				mapaDeClases.get(indice).agregarPixel(pixel);
			}
		}
		
		//Paso 3: Calcular el promedio de cada clase

		for ( ClaseOtsu claseActual : mapaDeClases.values() ){
			
			int rojoPromedio = 0;
			int verdePromedio = 0;
			int azulPromedio = 0;
			
			int rojoAcumulado = 0;
			int verdeAcumulado = 0;
			int azulAcumulado = 0;
			
			int contador = 0;
			
			for (Pixel pixelActual : claseActual.getPixeles()){
				
				rojoAcumulado += pixelActual.getColor().getRed();
				verdeAcumulado += pixelActual.getColor().getGreen();
				azulAcumulado += pixelActual.getColor().getBlue();
				
				contador ++;
			}
			
			rojoPromedio = (int) (rojoAcumulado / contador);
			verdePromedio = (int) (verdeAcumulado / contador);
			azulPromedio = (int) (azulAcumulado / contador);

			claseActual.setRojoPromedio(rojoPromedio);
			claseActual.setVerdePromedio(verdePromedio);
			claseActual.setAzulPromedio(azulPromedio);
		}
		
		//Paso 4: Calcular la varianza propia de cada clase (within-variance) y la varianza entre clases (between-variance)
		
		for ( ClaseOtsu claseActual : mapaDeClases.values() ){
			
			double varianza = 0;
			
			int acumulado = 0;
			
			for (Pixel pixelActual : claseActual.getPixeles()){
				
				acumulado += Math.pow((pixelActual.getColor().getRed() - claseActual.getRojoPromedio()), 2)
							+ Math.pow((pixelActual.getColor().getGreen() - claseActual.getVerdePromedio()), 2)
							+ Math.pow((pixelActual.getColor().getBlue() - claseActual.getAzulPromedio()), 2);
			}
			
			varianza = Math.sqrt(acumulado) / claseActual.getPixeles().size(); 
			claseActual.setVarianza(varianza);
		}
		
		List<VarianzaInterclase> listaDeVarianzasInterclase = new ArrayList<VarianzaInterclase>();
		
		for ( int i= 0; i< mapaDeClases.values().size(); i++ ){
			
			for ( int j = i+1; j < mapaDeClases.values().size(); j++ ){
				
				//Calculo el between class
				VarianzaInterclase varianzaEntreClases = new VarianzaInterclase();
				
				ClaseOtsu claseA = mapaDeClases.get(i);
				ClaseOtsu claseB = mapaDeClases.get(j);
				
				varianzaEntreClases.setClaseA(claseA);
				varianzaEntreClases.setClaseB(claseB);

				double varianza = Math.sqrt( Math.pow(claseA.getRojoPromedio()-claseB.getRojoPromedio(), 2)
									         + Math.pow(claseA.getVerdePromedio()-claseB.getVerdePromedio(), 2)
									         + Math.pow(claseA.getAzulPromedio()-claseB.getAzulPromedio(), 2));
				
				varianzaEntreClases.setVarianza(varianza);
				
				listaDeVarianzasInterclase.add(varianzaEntreClases);
			}
		}
		
		//Paso 5: Comparo y Paso 6: decido si hacer merge o no
		// del paso 3 hasta acá, se exportaría a un método que es autorecursivo
		

		
		//Paso 7: Asignar el color promedio de cada clase a todos los pixeles de cada una de las clases.
		
		for ( ClaseOtsu claseActual : mapaDeClases.values() ){
			
			int promedioRojo = claseActual.getRojoPromedio();
			int promedioVerde = claseActual.getVerdePromedio();
			int promedioAzul = claseActual.getAzulPromedio();
			
			for (Pixel pixelActual : claseActual.getPixeles()){
				
				pixelActual.setColor(new Color(promedioRojo, promedioVerde, promedioAzul));
			}
		}
		
		//Paso 8: Formar la imagen pintando los pixeles de cada clase con el color promedio calculado en el paso 7.
		
		BufferedImage buffer = new BufferedImage(imagen.getBufferedImage().getWidth(),imagen.getBufferedImage().getHeight(), imagen.getBufferedImage().getType());
		Imagen imagenFinal = new Imagen(buffer, imagen.getFormato(), imagen.getNombre()+"_otsuColor");
		
		for ( ClaseOtsu claseActual : mapaDeClases.values() ){
			
			for (Pixel pixelActual : claseActual.getPixeles()){
			
				imagenFinal.getBufferedImage().setRGB(pixelActual.getX(), pixelActual.getX(), pixelActual.getColor().getRGB());
			}
		}
		
		return imagenFinal;
	}

	private static void cargarMapaDeClases(
			Map<Integer, ClaseOtsu> mapaDeClases) {
		
		ClaseOtsu claseC0 = new ClaseOtsu();
		ClaseOtsu claseC1 = new ClaseOtsu();
		ClaseOtsu claseC2 = new ClaseOtsu();
		ClaseOtsu claseC3 = new ClaseOtsu();
		ClaseOtsu claseC4 = new ClaseOtsu();
		ClaseOtsu claseC5 = new ClaseOtsu();
		ClaseOtsu claseC6 = new ClaseOtsu();
		ClaseOtsu claseC7 = new ClaseOtsu();
		
		mapaDeClases.put(0, claseC0);
		mapaDeClases.put(1, claseC1);
		mapaDeClases.put(2, claseC2);
		mapaDeClases.put(3, claseC3);
		mapaDeClases.put(4, claseC4);
		mapaDeClases.put(5, claseC5);
		mapaDeClases.put(6, claseC6);
		mapaDeClases.put(7, claseC7);
	}
	
}
