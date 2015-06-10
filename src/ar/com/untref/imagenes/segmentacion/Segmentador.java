package ar.com.untref.imagenes.segmentacion;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ar.com.untref.imagenes.modelo.Curva;
import ar.com.untref.imagenes.modelo.Imagen;

public class Segmentador {
	
private static int diferenciaColor = 20;	

public static BufferedImage segmentarImagen(Imagen imagenOriginal, Point punto1, Point punto2, int repeticiones, int difColor) {
		
		diferenciaColor = difColor;
		Imagen imagenMuleto = new Imagen(imagenOriginal.getBufferedImage(), imagenOriginal.getFormato(), imagenOriginal.getNombre()+"_m");
		BufferedImage imagen = imagenMuleto.getBufferedImage();
		
		Curva curvaSeleccionada = obtenerCurvaDesdePuntosSeleccionados(punto1.x, punto1.y, punto2.x, punto2.y);

		int desdeX = curvaSeleccionada.getDesde().x;
		int hastaX = curvaSeleccionada.getHasta().x;
		int desdeY = curvaSeleccionada.getDesde().y;
		int hastaY = curvaSeleccionada.getHasta().y;

		int[][] matrizSigmas = new int[imagen.getWidth()][imagen.getHeight()];
		
		List<Point> lIn = new CopyOnWriteArrayList<Point>();
		List<Point> lOut = new CopyOnWriteArrayList<Point>();

		int contPixelFueraLout = 0;
		int contPixelDentroLin = 0;
		
		for(int i = 0; i < imagen.getWidth() ; i++){
			for(int j = 0; j < imagen.getHeight() ; j++){
				
				if(estaEnBorde(i, j, desdeX, hastaX, desdeY, hastaY)){
					
					matrizSigmas[i][j] = -1;
					lIn.add(new Point(i,j));
					
				}else if(estaAlRededorDelBorde(i, j, desdeX, hastaX, desdeY, hastaY)){
					
					matrizSigmas[i][j] = 1;
					lOut.add(new Point(i,j));
					
				}else if(estaAdentro(i, j, desdeX, hastaX, desdeY, hastaY)){
					
					matrizSigmas[i][j] = -3;
					contPixelDentroLin++;
					
				}else{
					
					matrizSigmas[i][j] = 3;
					contPixelFueraLout++;
					
				}
			}
		}
		
		int[] promedioDeColores = obtenerPromedioRGB(imagen, punto1, punto2);
		
		for(int i = 0; i < repeticiones; i++){
			
			Iterator<Point> iteradorPuntos = lOut.iterator();
			while(iteradorPuntos.hasNext()){
				
				expandir(imagen, matrizSigmas, lIn, lOut, promedioDeColores,iteradorPuntos, contPixelFueraLout, contPixelDentroLin);
			}

			Iterator<Point> iteradorPuntosLin = lIn.iterator();
			while(iteradorPuntosLin.hasNext()){
				
				sacarLinNoCorrespondientes(matrizSigmas, lIn, iteradorPuntosLin);
			}
			
			Iterator<Point> iteradorPuntosLin2 = lIn.iterator();
			while(iteradorPuntosLin2.hasNext()){
				
				contraer(imagen, matrizSigmas, lIn, lOut, promedioDeColores, iteradorPuntosLin2, contPixelFueraLout, contPixelDentroLin);
			}
			
			Iterator<Point> iteradorPuntosLout2 = lOut.iterator();
			while(iteradorPuntosLout2.hasNext()){
				
				sacarLoutNoCorrespondientes(matrizSigmas, lOut, iteradorPuntosLout2);
			}
		}
		
		for(Point unPoint : lIn){
			imagen.setRGB(unPoint.x, unPoint.y, Color.PINK.getRGB());
		}
		
		return imagen;
	}

	private static Curva obtenerCurvaDesdePuntosSeleccionados(int primerPuntoX,
			int primerPuntoY, int segundoPuntoX, int segundoPuntoY) {

		int desdeX = 0;
		int hastaX = 0;
		int desdeY = 0;
		int hastaY = 0;

		if (primerPuntoX <= segundoPuntoX) {
			desdeX = primerPuntoX;
			hastaX = segundoPuntoX;
		} else {
			desdeX = segundoPuntoX;
			hastaX = primerPuntoX;
		}

		if (primerPuntoY <= segundoPuntoY) {
			desdeY = primerPuntoY;
			hastaY = segundoPuntoY;
		} else {
			desdeY = segundoPuntoY;
			hastaY = primerPuntoY;
		}

		Point desde = new Point(desdeX, desdeY);
		Point hasta = new Point(hastaX, hastaY);

		return new Curva(desde, hasta);
	}

	private static void sacarLoutNoCorrespondientes(int[][] matrizSigmas,
			List<Point> lOut, Iterator<Point> iteradorPuntosLout2) {
		Point unPoint = iteradorPuntosLout2.next();
		
		int valorMatrizIzquierda = matrizSigmas[unPoint.x - 1][unPoint.y];
		int valorMatrizDerecha = matrizSigmas[unPoint.x + 1][unPoint.y];
		int valorMatrizArriba = matrizSigmas[unPoint.x][unPoint.y - 1];
		int valorMatrizAbajo = matrizSigmas[unPoint.x][unPoint.y + 1];
		
		if(valorMatrizIzquierda > 0 && valorMatrizDerecha > 0 && valorMatrizArriba > 0 && valorMatrizAbajo > 0){
			matrizSigmas[unPoint.x][unPoint.y] = 3;
			lOut.remove(unPoint);
		}
	}

	private static void contraer(BufferedImage imagen, int[][] matrizSigmas,
			List<Point> lIn, List<Point> lOut, int[] promedio,
			Iterator<Point> iteradorPuntosLin2, int contadorPixelFuera, int contadorPixelDentro) {
		Point unPoint = iteradorPuntosLin2.next();
		
		if(calcularFd(promedio, imagen, unPoint) < 0){
			
			int valorMatrizIzquierda = matrizSigmas[unPoint.x - 1][unPoint.y];
			int valorMatrizDerecha = matrizSigmas[unPoint.x + 1][unPoint.y];
			int valorMatrizArriba = matrizSigmas[unPoint.x][unPoint.y - 1];
			int valorMatrizAbajo = matrizSigmas[unPoint.x][unPoint.y + 1];
			
			if(valorMatrizIzquierda == -3){
				matrizSigmas[unPoint.x - 1][unPoint.y] = -1;
				lIn.add(new Point(unPoint.x - 1, unPoint.y));
			}
			
			if(valorMatrizDerecha == -3){
				matrizSigmas[unPoint.x + 1][unPoint.y] = -1;
				lIn.add(new Point(unPoint.x + 1, unPoint.y));
			}
			
			if(valorMatrizArriba == -3){
				matrizSigmas[unPoint.x][unPoint.y - 1] = -1;
				lIn.add(new Point(unPoint.x, unPoint.y - 1));
			}
			
			if(valorMatrizAbajo == -3){
				matrizSigmas[unPoint.x][unPoint.y + 1] = -1;
				lIn.add(new Point(unPoint.x, unPoint.y + 1));
			}
			
			matrizSigmas[unPoint.x][unPoint.y] = 1;
			lOut.add(unPoint);
			lIn.remove(unPoint);
		}
	}

	private static void sacarLinNoCorrespondientes(int[][] matrizSigmas,
			List<Point> lIn, Iterator<Point> iteradorPuntosLin) {
		Point unPoint = iteradorPuntosLin.next();
		
		int valorMatrizIzquierda = matrizSigmas[unPoint.x - 1][unPoint.y];
		int valorMatrizDerecha = matrizSigmas[unPoint.x + 1][unPoint.y];
		int valorMatrizArriba = matrizSigmas[unPoint.x][unPoint.y - 1];
		int valorMatrizAbajo = matrizSigmas[unPoint.x][unPoint.y + 1];
		
		if(valorMatrizIzquierda < 0 && valorMatrizDerecha < 0 && valorMatrizArriba < 0 && valorMatrizAbajo < 0){
			
			matrizSigmas[unPoint.x][unPoint.y] = -3;
			lIn.remove(unPoint);
		}
	}

	private static void expandir(BufferedImage imagen, int[][] matrizSigmas,
			List<Point> lIn, List<Point> lOut, int[] promedio,
			Iterator<Point> iteradorPuntos, int contadorPixelFuera, int contadorPixelDentro) {
		
		Point unPoint = iteradorPuntos.next();
		
		if(calcularFd(promedio, imagen, unPoint) > 0){
			
			int valorMatrizIzquierda = matrizSigmas[unPoint.x - 1][unPoint.y];
			int valorMatrizDerecha = matrizSigmas[unPoint.x + 1][unPoint.y];
			int valorMatrizArriba = matrizSigmas[unPoint.x][unPoint.y - 1];
			int valorMatrizAbajo = matrizSigmas[unPoint.x][unPoint.y + 1];
			
			if(valorMatrizIzquierda == 3){
				
				matrizSigmas[unPoint.x - 1][unPoint.y] = 1;
				lOut.add(new Point(unPoint.x - 1, unPoint.y));
			}

			if(valorMatrizDerecha == 3){

				matrizSigmas[unPoint.x + 1][unPoint.y] = 1;
				lOut.add(new Point(unPoint.x + 1, unPoint.y));
			}
			
			if(valorMatrizArriba == 3){
				
				matrizSigmas[unPoint.x][unPoint.y - 1] = 1;
				lOut.add(new Point(unPoint.x, unPoint.y - 1));
			}

			if(valorMatrizAbajo == 3){
				
				matrizSigmas[unPoint.x][unPoint.y + 1] = 1;
				lOut.add(new Point(unPoint.x, unPoint.y + 1));
			}
			
			lIn.add(new Point(unPoint.x, unPoint.y));
			matrizSigmas[unPoint.x][unPoint.y] = -1;
			lOut.remove(unPoint);
		}
	}
	
	private static int calcularFd(int[] promedio, BufferedImage imagen, Point posicion) {

		Color colorPromedio = new Color(promedio[0], promedio[1], promedio[2]);
//		int cantidadDePixelesDentroConColorPromedio = 0;
//		int cantidadDePixelesFueraConColorPromedio = 0;
//		
//		//Probabilidad de que un pixel dentro de la region de la curva tenga el color promedio
//		float probabilidadOmega1 = 0;
//		//Probabilidad de que un pixel fuera de la region de la curva tenga el color promedio
//		float probabilidadOmega2 = 0;
//		
//		for (int i=0; i< matrizSigmas.length; i++ ){
//			for (int j=0; j< matrizSigmas[0].length; j++ ){
//				
//				if (sonColoresSimilares(imagen, colorPromedio, i, j) && matrizSigmas[i][j]==-3 ){
//					
//					cantidadDePixelesDentroConColorPromedio++;
//				} else if (sonColoresSimilares(imagen, colorPromedio, i, j) && matrizSigmas[i][j]>=1 ) {//Lout tambien esta fuera de la curva por eso tomo los sigmas 1 y 3
//					
//				    cantidadDePixelesFueraConColorPromedio++;
//				}
//			}
//		}
//		
//		probabilidadOmega1 = (float) cantidadDePixelesDentroConColorPromedio / contadorPixelDentro;
//		probabilidadOmega2 = (float) cantidadDePixelesFueraConColorPromedio / contadorPixelFuera;
//		
		int fD = 1;
		
		boolean debeSeguir = sonColoresSimilares(imagen, colorPromedio, posicion.x, posicion.y);
		
		if ( !debeSeguir ){
			
			fD = -1;
		}
		
		return fD;
	}

	private static boolean sonColoresSimilares(BufferedImage imagen,
			Color colorPromedio, int i, int j) {
		
		Color colorEnEsePunto = new Color(imagen.getRGB(i, j));
		
//		double mediaCuadrada = Math.sqrt( Math.pow(colorEnEsePunto.getRed()-colorPromedio.getRed(), 2) +
//										  Math.pow(colorEnEsePunto.getGreen()-colorPromedio.getGreen(), 2) +
//										  Math.pow(colorEnEsePunto.getBlue()-colorPromedio.getBlue(), 2)) / Math.pow(255, 3);
//		
//		boolean esSimilar = mediaCuadrada < 0.9;
		
		boolean esSimilar = Math.abs(colorEnEsePunto.getRed()-colorPromedio.getRed()) <= diferenciaColor 
							&& Math.abs(colorEnEsePunto.getGreen()-colorPromedio.getGreen()) <= diferenciaColor
							&& Math.abs(colorEnEsePunto.getBlue()-colorPromedio.getBlue()) <= diferenciaColor;
		
		return esSimilar;
	}

	private static boolean estaAdentro(int i, int j, int desdeX, int hastaX, int desdeY, int hastaY) {
		boolean estaAdentro = false;
		if(i > desdeX && i < hastaX && j > desdeY && j < hastaY){
			estaAdentro = true;
		}
		
		return estaAdentro;
	}

	private static boolean estaAlRededorDelBorde(int i, int j, int desdeX, int hastaX, int desdeY, int hastaY) {
		
		boolean estaAlrededor = false;
		if(j + 1 == desdeY && i <= hastaX && i >= desdeX){
			estaAlrededor = true;
		}else if(i + 1 == desdeX && j >= desdeY && j<= hastaY){
			estaAlrededor = true;
		}else if(i - 1 == hastaX && j >= desdeY && j<= hastaY){
			estaAlrededor = true;
		}else if(j - 1 == hastaY && i >= desdeX && i <= hastaX){
			estaAlrededor = true;
		}
		
		return estaAlrededor;
	}

	private static boolean estaEnBorde(int i, int j, int desdeX, int hastaX, int desdeY, int hastaY) {
		boolean estaEnBorde = false;
		if(j == desdeY && i <= hastaX && i >= desdeX){
			estaEnBorde = true;
		}else if(i == desdeX && j >= desdeY && j<= hastaY){
			estaEnBorde = true;
		}else if(i == hastaX && j >= desdeY && j<= hastaY){
			estaEnBorde = true;
		}else if(j == hastaY && i >= desdeX && i <= hastaX){
			estaEnBorde = true;
		}
		
		return estaEnBorde;
	}

	private static int[] obtenerPromedioRGB(BufferedImage imagen, Point punto1, Point punto2){
	       
		   int rojoAcumulado = 0 ;
	       int verdeAcumulado = 0;
	       int azulAcumulado = 0;
	       
	       int contadorCantPixel = 0;
	       
	       int promedioRojo = 0;
	       int promedioVerde = 0;
	       int promedioAzul = 0;
	       
	       int primerPuntoX = punto1.x;
	       int primerPuntoY = punto1.y;
	       int segundoPuntoX = punto2.x;
	       int segundoPuntoY = punto2.y;
	       
	       int desdeX = 0;
	       int hastaX = 0;
	       int desdeY = 0;
	       int hastaY = 0;
	       
	       if(primerPuntoX <= segundoPuntoX){
	    	   desdeX = primerPuntoX;
	    	   hastaX = segundoPuntoX;
	       } else {
	    	   desdeX = segundoPuntoX;
	    	   hastaX = primerPuntoX;
	       }
	       
	       if(primerPuntoY<= segundoPuntoY){
	    	   desdeY = primerPuntoY;
	    	   hastaY = segundoPuntoY;
	       } else {
	    	   desdeY = segundoPuntoY;
	    	   hastaY = primerPuntoY;
	       }
	       
	       for(int i = desdeX+1 ; i < hastaX-1 ; i++){
	    	   for(int j = desdeY+1 ; j < hastaY-1 ; j++){
			       Color color = new Color(imagen.getRGB(i, j));
			       int blue = color.getBlue();
			       int green = color.getGreen();
			       int red = color.getRed();
			       
			       rojoAcumulado += red;
			       verdeAcumulado += green;
			       azulAcumulado += blue;
			       
			       contadorCantPixel++;
	    	   }
	       }
	       
	       promedioRojo = (int) (rojoAcumulado / contadorCantPixel);
	       promedioVerde = (int) (verdeAcumulado / contadorCantPixel);
	       promedioAzul = (int) (azulAcumulado / contadorCantPixel);

	       int[] valoresPromedio = new int[3];
	       valoresPromedio[0] = promedioRojo;
	       valoresPromedio[1] = promedioVerde;
		   valoresPromedio[2] = promedioAzul;
	       
	       return valoresPromedio;

	}
}
