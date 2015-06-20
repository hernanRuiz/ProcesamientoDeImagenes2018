package ar.com.untref.imagenes.bordes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ar.com.untref.imagenes.modelo.Imagen;

public class TransformadaDeHough {

	public static Imagen aplicarTransformadaDeHough(Imagen imagenOriginalUmbralizada, int titaMin, int titaMax, int discretizadoDeTitas,
													int roMin, int roMax, int discretizadoDeRos, Graphics2D graphics){
		
		Point[][] matrizDeRectas = crearMatrizDeRectas(titaMin, titaMax, roMin, roMax, discretizadoDeTitas, discretizadoDeRos);
		
		int[][] matrizDeAcumulados = new int[matrizDeRectas[0].length][matrizDeRectas.length];
		
		for (int i=0; i< imagenOriginalUmbralizada.getBufferedImage().getWidth(); i++){
			for (int j=0; j< imagenOriginalUmbralizada.getBufferedImage().getHeight(); j++){
				
				Color colorEnUmbralizada = new Color(imagenOriginalUmbralizada.getBufferedImage().getRGB(i, j));
				if (colorEnUmbralizada.getRed()==255 && colorEnUmbralizada.getGreen()==255 && colorEnUmbralizada.getBlue()==255){
					
					evaluarPuntoEnLaMatriz(matrizDeRectas, matrizDeAcumulados, i, j);
				}
			}
		}
		
		List<Point> rectasMaximas = buscarRectasMaximas(matrizDeRectas, matrizDeAcumulados);
		
	    Graphics2D g2 = (Graphics2D) graphics;
//		g2.draw(new Line2D.Double(x1, y1, x2, y2));

		rectasMaximas.clear();
		return null;
	}

	private static List<Point> buscarRectasMaximas(Point[][] matrizDeRectas,
			int[][] matrizDeAcumulados) {
		
		int maximo = Integer.MIN_VALUE;
		List<Point> posiciones = new ArrayList<Point>();
		List<Point> rectas = new ArrayList<Point>();
		
		for ( int i=0; i < matrizDeAcumulados[0].length ; i++){
			for ( int j=0; j < matrizDeAcumulados.length ; j++){
				
				if ( matrizDeAcumulados[i][j] > maximo ){
					
					maximo = matrizDeAcumulados[i][j];
					posiciones.clear();
					posiciones.add(new Point(i,j));
				} else if (matrizDeAcumulados[i][j] == maximo ){
					
					posiciones.add(new Point(i,j));
				}
			}	
		}
		
		for ( Point puntos: posiciones ){
			
			int ro = matrizDeRectas[puntos.x][puntos.y].x;
			int tita = matrizDeRectas[puntos.x][puntos.y].y;
					
			rectas.add(new Point(ro, tita));
		}
		
		return rectas;
	}

	private static void evaluarPuntoEnLaMatriz(Point[][] matrizDeRectas, int[][] matrizDeAcumulados, int i, int j) {

		for ( int w=0; w < matrizDeRectas[0].length; w++){
			for ( int h=0; h < matrizDeRectas.length; h++){
				
				int ro = matrizDeRectas[w][h].x;
				int tita = matrizDeRectas[w][h].y;

				float ecuacion = (float) Math.abs(ro - (i * Math.cos(tita)) - (j * Math.sin(tita)) );
				int epsilon = 1;
				
				if ( ecuacion < epsilon){
					
					matrizDeAcumulados[w][h] = matrizDeAcumulados[w][h]+1;
				}
			}
		}
	}

	private static Point[][] crearMatrizDeRectas(int titaMin, int titaMax,
			int roMin, int roMax, int discretizadoTitas, int discretizadoRos) {

		int cantidadDeTitas =  (int) ((float)((titaMax-titaMin)/discretizadoTitas));
		int cantidadDeRos =  (int) ((float)((roMax-roMin)/discretizadoRos));
		Point[][] matrizDeRectas = new Point[cantidadDeRos][cantidadDeTitas];
		
		for(int i = 0; i < cantidadDeRos; i++){
           for(int j = 0; j < cantidadDeTitas; j++){
        	   
        	   matrizDeRectas[i][j] = new Point(roMin + (discretizadoRos*i), titaMin + (discretizadoTitas*j));
           }
        }
		
		return matrizDeRectas;
	}
}
