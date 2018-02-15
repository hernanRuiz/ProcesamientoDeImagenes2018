package ar.com.untref.imagenes.bordes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.filtros.FiltroGaussiano;
import ar.com.untref.imagenes.modelo.Imagen;

public class DoG {
	
	private static List<Integer> resultadosX = new LinkedList<Integer>();
	private static List<Integer> resultadosY = new LinkedList<Integer>();
	private static PrintStream fileStreamDoG;
	
	public static BufferedImage aplicar(Imagen imagenOriginal, int sigma1, int sigma2, int sigma3, int sigma4, boolean flagResultados) {
		
		//En ejecución individual seteamos un archivo de salida
		if(flagResultados){			
			try {
				fileStreamDoG = new PrintStream("Salida_algoritmo_DiferenciaDeGaussianas.txt");
				fileStreamDoG.flush();
				System.setOut(fileStreamDoG);
				System.out.println("Puntos detectados (sigmas: " + sigma1 + ", " + sigma2 + ", " + sigma3 + " y "
						+ sigma4 + "):");
				System.out.println();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		Imagen imagenResultante = new Imagen(new BufferedImage(imagenOriginal.getBufferedImage().getWidth(), imagenOriginal.getBufferedImage().getHeight(), imagenOriginal.getBufferedImage().getType()), imagenOriginal.getFormato(), imagenOriginal.getNombre()+"_DoG");
		Imagen imagenAGrises = pasarImagenAGGrises(imagenOriginal);

		//Aplicamos filtros gaussianos a la imagen según los parámetros sigma 
		Imagen imagenFiltradaSigma1 = FiltroGaussiano.aplicarFiltroGaussiano(imagenAGrises, sigma1);
		Imagen imagenFiltradaSigma2 = FiltroGaussiano.aplicarFiltroGaussiano(imagenAGrises, sigma2);
		Imagen imagenFiltradaSigma3 = FiltroGaussiano.aplicarFiltroGaussiano(imagenAGrises, sigma3);
		Imagen imagenFiltradaSigma4 = FiltroGaussiano.aplicarFiltroGaussiano(imagenAGrises, sigma4);
		
		//Restamos pixel a pixel las imágenes filtradas
		Imagen imagenRestaSigma1y2 = restarImagenesFiltradas(imagenFiltradaSigma1, imagenFiltradaSigma2);
		Imagen imagenRestaSigma2y3 = restarImagenesFiltradas(imagenFiltradaSigma2, imagenFiltradaSigma3);
		Imagen imagenRestaSigma3y4 = restarImagenesFiltradas(imagenFiltradaSigma3, imagenFiltradaSigma4);
	
		for (int y = 0; y < imagenRestaSigma1y2.getBufferedImage().getHeight(); y ++) {
			
			for (int x = 0; x < imagenRestaSigma1y2.getBufferedImage().getWidth(); x ++) { 
				
				List<Boolean> maximosYMinimosCaso0 = calcularMaximosYMinimos(imagenRestaSigma1y2, imagenRestaSigma2y3, imagenRestaSigma3y4, x, y, 0);
				List<Boolean> maximosYMinimosCaso1 = calcularMaximosYMinimos(imagenRestaSigma1y2, imagenRestaSigma2y3, imagenRestaSigma3y4, x, y, 1);
				List<Boolean> maximosYMinimosCaso2 = calcularMaximosYMinimos(imagenRestaSigma1y2, imagenRestaSigma2y3, imagenRestaSigma3y4, x, y, 2);
			
				boolean esMaximoCaso0 = maximosYMinimosCaso0.get(0);
				boolean esMinimoCaso0 = maximosYMinimosCaso0.get(1);
				
				boolean esMaximoCaso1 = maximosYMinimosCaso1.get(0);
				boolean esMinimoCaso1 = maximosYMinimosCaso1.get(1);
				
				boolean esMaximoCaso2 = maximosYMinimosCaso2.get(0);
				boolean esMinimoCaso2 = maximosYMinimosCaso2.get(1);
				
				if(esMaximoCaso0 == true){
					imagenResultante.getBufferedImage().setRGB(x, y, Color.YELLOW.getRGB());
					resultadosX.add(x);
					resultadosY.add(y);
					if(flagResultados){
						System.out.println("Máximo: " + x + ", " + y);
					}
				} 
				
				if(esMinimoCaso0 == true){
					imagenResultante.getBufferedImage().setRGB(x, y, Color.GREEN.getRGB());
					resultadosX.add(x);
					resultadosY.add(y);
					if(flagResultados){
						System.out.println("Mínimo: " + x + ", " + y);
					}
				} 
				
				if (esMaximoCaso0 == false && esMinimoCaso0 == false){
					imagenResultante.getBufferedImage().setRGB(x, y, imagenOriginal.getBufferedImage().getRGB(x, y));
				}
				
				//=================================================================
				if(esMaximoCaso1 == true){
					imagenResultante.getBufferedImage().setRGB(x, y, Color.YELLOW.getRGB());
					resultadosX.add(x);
					resultadosY.add(y);
					if(flagResultados){
						System.out.println("Máximo: " + x + ", " + y);
					}
				} 
				
				if(esMinimoCaso1 == true){
					imagenResultante.getBufferedImage().setRGB(x, y, Color.GREEN.getRGB());
					resultadosX.add(x);
					resultadosY.add(y);
					if(flagResultados){
						System.out.println("Mínimo: " + x + ", " + y);
					}
				} 
				
				if (esMaximoCaso1 == false && esMinimoCaso1 == false){
					imagenResultante.getBufferedImage().setRGB(x, y, imagenOriginal.getBufferedImage().getRGB(x, y));
				}
				
				//=================================================================
				if(esMaximoCaso2 == true){
					imagenResultante.getBufferedImage().setRGB(x, y, Color.YELLOW.getRGB());
					resultadosX.add(x);
					resultadosY.add(y);
					if(flagResultados){
						System.out.println("Máximo: " + x + ", " + y);
					}
				} 
				
				if(esMinimoCaso2 == true){
					imagenResultante.getBufferedImage().setRGB(x, y, Color.GREEN.getRGB());
					resultadosX.add(x);
					resultadosY.add(y);
					if(flagResultados){
						System.out.println("Mínimo: " + x + ", " + y);
					}
				} 
				
				if (esMaximoCaso2 == false && esMinimoCaso2 == false){
					imagenResultante.getBufferedImage().setRGB(x, y, imagenOriginal.getBufferedImage().getRGB(x, y));
				}
			}
		}
		
		if(flagResultados){
			resultadosX = new LinkedList<Integer>();
			resultadosY = new LinkedList<Integer>();
			fileStreamDoG.flush();
			fileStreamDoG.close();
		}
		
		return imagenResultante.getBufferedImage();
	}
	
	
	private static Imagen pasarImagenAGGrises(Imagen imagenOriginal) {
		
		Imagen imagenResultante = new Imagen(new BufferedImage(imagenOriginal.getBufferedImage().getWidth(), imagenOriginal.getBufferedImage().getHeight(), imagenOriginal.getBufferedImage().getType()), imagenOriginal.getFormato(), "DoGGrises");
		
		for (int y = 0; y < imagenOriginal.getBufferedImage().getHeight(); y ++) {
			
			for (int x = 0; x < imagenOriginal.getBufferedImage().getWidth(); x ++) {
			
				//Color colorImagen = new Color(imagenOriginal.getBufferedImage().getRGB(x, y));
				
				int rgb = imagenOriginal.getBufferedImage().getRGB(x, y);
		        int r = (rgb >> 16) & 0xFF;
		        int g = (rgb >> 8) & 0xFF;
		        int b = (rgb & 0xFF);

		        int grayLevel = (r + g + b) / 3;
		        int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel; 
				
				// int valorPromedio = (colorImagen.getRed() + colorImagen.getBlue() + colorImagen.getGreen()) / 3;
				imagenResultante.getBufferedImage().setRGB(x, y, gray);
			}
		}
		
		return imagenResultante;
	}


	private static Imagen restarImagenesFiltradas(Imagen imagenFiltradaSigma1,
			Imagen imagenFiltradaSigma2) {
		
		Imagen imagenResultante = new Imagen(new BufferedImage(imagenFiltradaSigma1.getBufferedImage().getWidth(), imagenFiltradaSigma1.getBufferedImage().getHeight(), imagenFiltradaSigma1.getBufferedImage().getType()), imagenFiltradaSigma1.getFormato(), "DoGResta");
		
		for (int y = 0; y < imagenFiltradaSigma1.getBufferedImage().getHeight(); y ++) {
			
			for (int x = 0; x < imagenFiltradaSigma1.getBufferedImage().getWidth(); x ++) {
				
				Color colorImagenSigma1 = new Color(imagenFiltradaSigma1.getBufferedImage().getRGB(x, y));
				Color colorImagenSigma2 = new Color(imagenFiltradaSigma2.getBufferedImage().getRGB(x, y));
				
				int rojo = Math.abs(colorImagenSigma1.getRed() - colorImagenSigma2.getRed());
				int verde = Math.abs(colorImagenSigma1.getGreen() - colorImagenSigma2.getGreen());
				int azul = Math.abs(colorImagenSigma1.getBlue() - colorImagenSigma2.getBlue());
				
				imagenResultante.getBufferedImage().setRGB(x, y, (new Color(rojo, verde, azul).getRGB()));
			}
		}
		
		return imagenResultante;
	}
	
	
	private static List<Boolean> calcularMaximosYMinimos(Imagen imagenRestaSigma1y2, Imagen imagenRestaSigma2y3, Imagen imagenRestaSigma3y4, int x, int y, int caso){
		
		boolean esMaximoCaso0 = false;
		boolean esMinimoCaso0 = false;
		boolean esMaximoCaso1 = false;
		boolean esMinimoCaso1 = false;
		boolean esMaximoCaso2 = false;
		boolean esMinimoCaso2 = false;
		
		List<Boolean> maximosYMinimos = new ArrayList<Boolean>();
		
		List<List<Color>> nodosVecinosTodosLosNivelesCaso1 = calcularNodosVecinosNiveles(imagenRestaSigma1y2, imagenRestaSigma2y3, imagenRestaSigma3y4, x, y, 1);
		List<Color> nodosVecinosImagenRestaSigma1y2 = nodosVecinosTodosLosNivelesCaso1.get(0);
		List<Color> nodosVecinosImagenRestaSigma2y3 = nodosVecinosTodosLosNivelesCaso1.get(1);
		List<Color> nodosVecinosImagenRestaSigma3y4 = nodosVecinosTodosLosNivelesCaso1.get(2);
		
		
		switch (caso) {
		case 0:
			
			Color puntoAEvaluarCaso0 = new Color(imagenRestaSigma1y2.getBufferedImage().getRGB(x, y));
			nodosVecinosImagenRestaSigma1y2.addAll(nodosVecinosImagenRestaSigma2y3);
			
			esMaximoCaso0 = calcularMaximo(puntoAEvaluarCaso0, nodosVecinosImagenRestaSigma1y2);
			if(esMaximoCaso0 == false){				
				esMinimoCaso0 = calcularMinimo(puntoAEvaluarCaso0, nodosVecinosImagenRestaSigma1y2);
			}
			
			maximosYMinimos.add(esMaximoCaso0);
			maximosYMinimos.add(esMinimoCaso0);
			esMaximoCaso0 = false;
			esMinimoCaso0 = false;
			break;

		case 1:
			
			Color puntoAEvaluarCaso1 = new Color(imagenRestaSigma1y2.getBufferedImage().getRGB(x, y));
			nodosVecinosImagenRestaSigma2y3.addAll(nodosVecinosImagenRestaSigma1y2);
			nodosVecinosImagenRestaSigma2y3.addAll(nodosVecinosImagenRestaSigma3y4);
			
			esMaximoCaso1 = calcularMaximo(puntoAEvaluarCaso1, nodosVecinosImagenRestaSigma2y3);
			if(esMaximoCaso1 == false){					
				esMinimoCaso1 = calcularMinimo(puntoAEvaluarCaso1, nodosVecinosImagenRestaSigma2y3);
			}
			
			maximosYMinimos.add(esMaximoCaso1);
			maximosYMinimos.add(esMinimoCaso1);
			esMaximoCaso1 = false;
			esMinimoCaso1 = false;
			break;
			
		default:
			
			Color puntoAEvaluarCaso2 = new Color(imagenRestaSigma1y2.getBufferedImage().getRGB(x, y));
			nodosVecinosImagenRestaSigma3y4.addAll(nodosVecinosImagenRestaSigma2y3);
			
			esMaximoCaso2 = calcularMaximo(puntoAEvaluarCaso2, nodosVecinosImagenRestaSigma3y4);
			if(esMaximoCaso2 == false){					
				esMinimoCaso2 = calcularMinimo(puntoAEvaluarCaso2, nodosVecinosImagenRestaSigma3y4);
			}
			
			maximosYMinimos.add(esMaximoCaso2);
			maximosYMinimos.add(esMinimoCaso2);
			esMaximoCaso2 = false;
			esMinimoCaso2 = false;
			break;
		}
		
		return maximosYMinimos;
	}


	private static boolean calcularMaximo(Color maximoAEvaluar, List<Color> nodosVecinosMultinivel) {
		
		int i;
		int maximoEncontrado = 0;
		boolean esMaximo = false;
		int contador = 0;
		
		List<Integer> valoresAComparar = new ArrayList<Integer>();
		
		for (i = 0; i < nodosVecinosMultinivel.size(); i++){			
			valoresAComparar.add(nodosVecinosMultinivel.get(i).getRed());
		}
		
		maximoEncontrado = Collections.max(valoresAComparar);
		
		/*Comparamos con los nodos vecinos en todos los niveles que participan de la comparación
		Verificamos si el máximoEncontrado es o no único*/
		for (int h = 0; h < valoresAComparar.size(); h++){
			int valorActual = valoresAComparar.get(h);
			if (maximoEncontrado == valorActual) {
				contador++;
			}
		}
		
		/*Si el maximo a evaluar es diferente al encontrado entonces no es máximo. Si coincide pero no es único, 
		no es un máximo a marcar.*/
		if ((maximoEncontrado == maximoAEvaluar.getRed()) && contador == 1){
			esMaximo = true;
		}
		
		return esMaximo;
	}
	
	
	private static boolean calcularMinimo(Color minimoAEvaluar, List<Color> nodosVecinos) {
		
		int i;
		int minimoEncontrado = 0;
		boolean esMinimo = false;
		int contador = 0;
		
		List<Integer> valoresAComparar = new ArrayList<Integer>();
		
		for (i = 0; i < nodosVecinos.size(); i++){	
			valoresAComparar.add(nodosVecinos.get(i).getRed());
		}
		
		minimoEncontrado = Collections.min(valoresAComparar);
				
		/*Comparamos con los nodos vecinos en todos los niveles que participan de la comparación
		Verificamos si el minimo encontrado es o no único*/
		for (int h = 0; h < valoresAComparar.size(); h++){
			int valorActual = valoresAComparar.get(h);
			if (minimoEncontrado == valorActual) {
				contador++;
			}
		}
		
		/*Si el mínimo a evaluar es diferente al encontrado entonces no es mínimo. Si coincide pero no es único, 
		no es un mínimo a marcar.*/
		if ((minimoEncontrado == minimoAEvaluar.getRed()) && contador == 1){
			esMinimo = true;
		}
				
		return esMinimo;
	}
	
	
	private static List<List<Color>> calcularNodosVecinosNiveles(Imagen imagenRestaSigma1y2, Imagen imagenRestaSigma2y3, Imagen imagenRestaSigma3y4, int x, int y, int caso) {
		                
		List<Color> nodosVecinosImagenRestaSigma1y2 = calcularNodosVecinos(imagenRestaSigma1y2, x, y);
		List<Color> nodosVecinosImagenRestaSigma2y3 = calcularNodosVecinos(imagenRestaSigma2y3, x, y);
		List<Color> nodosVecinosImagenRestaSigma3y4 = calcularNodosVecinos(imagenRestaSigma3y4, x, y);
		List<List<Color>> nodosVecinosTodosLosNiveles = new ArrayList<List<Color>>();
		
		nodosVecinosTodosLosNiveles.add(nodosVecinosImagenRestaSigma1y2);
		nodosVecinosTodosLosNiveles.add(nodosVecinosImagenRestaSigma2y3);
		nodosVecinosTodosLosNiveles.add(nodosVecinosImagenRestaSigma3y4);
				
		return nodosVecinosTodosLosNiveles;
	}
	
	
	private static List<Color> calcularNodosVecinos(Imagen imagenRestaSigma1y2, int x, int y){
		
		int ancho = imagenRestaSigma1y2.getBufferedImage().getWidth();
		int alto = imagenRestaSigma1y2.getBufferedImage().getHeight();
		List<Color> nodosVecinos = new ArrayList<Color>();
		
		Color nodoCentral = new Color(imagenRestaSigma1y2.getBufferedImage().getRGB(x, y));
		nodosVecinos.add(nodoCentral);
		
		if(x+1 >= 0 && x+1 < ancho){
			Color vecino1 = new Color(imagenRestaSigma1y2.getBufferedImage().getRGB(x+1, y));
			nodosVecinos.add(vecino1);
		}
			
		if((x+1 >= 0 && x+1 < ancho) && (y+1 >= 0 && y+1 < alto)){
			Color vecino2 = new Color(imagenRestaSigma1y2.getBufferedImage().getRGB(x+1, y+1));
			nodosVecinos.add(vecino2);
		}
		
		if((x+1 >= 0 && x+1 < ancho) && (y-1 >= 0 && y-1 < alto)){
			Color vecino3 = new Color(imagenRestaSigma1y2.getBufferedImage().getRGB(x+1, y-1));
			nodosVecinos.add(vecino3);
		}
			
		if(x-1 >= 0 && x-1 < ancho){
			Color vecino4 = new Color(imagenRestaSigma1y2.getBufferedImage().getRGB(x-1, y));
			nodosVecinos.add(vecino4);
		}
        
		if((x-1 >= 0 && x-1 < ancho) && (y+1 >= 0 && y+1 < alto)){
			Color vecino5 = new Color(imagenRestaSigma1y2.getBufferedImage().getRGB(x-1, y+1));
			nodosVecinos.add(vecino5);
		}
		
		if((x-1 >= 0 && x-1 < ancho) && (y-1 >= 0 && y-1 < alto)){
			Color vecino6 = new Color(imagenRestaSigma1y2.getBufferedImage().getRGB(x-1, y-1));
			nodosVecinos.add(vecino6);
		}
        
		if(y+1 >= 0 && y+1 < alto){
			Color vecino7 = new Color(imagenRestaSigma1y2.getBufferedImage().getRGB(x, y+1));
			nodosVecinos.add(vecino7);
		}
		
		if(y-1 >= 0 && y-1 < alto){
			Color vecino8 = new Color(imagenRestaSigma1y2.getBufferedImage().getRGB(x, y-1));
			nodosVecinos.add(vecino8);
		}
		
        return nodosVecinos;
	}
	
	
	 	public static List<Integer> getResultadosX() {
			return resultadosX;
		}

		public static void setResultadosX(List<Integer> resultadosX) {
			DoG.resultadosX = resultadosX;
		}

		public static List<Integer> getResultadosY() {
			return resultadosY;
		}

		public static void setResultadosY(List<Integer> resultadosY) {
			DoG.resultadosY = resultadosY;
		}
}