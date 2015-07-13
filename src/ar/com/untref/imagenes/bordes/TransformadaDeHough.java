package ar.com.untref.imagenes.bordes;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;

public class TransformadaDeHough {

	public static Imagen aplicarTransformadaDeHough(
			Imagen imagenOriginalUmbralizada, int titaMin, int titaMax,
			int discretizadoDeTitas, int roMin, int roMax, int discretizadoDeRos, int umbral, VentanaPrincipal ventana) {

		Point[][] matrizDeRectas = crearMatrizDeRectas(titaMin, titaMax, roMin,
				roMax, discretizadoDeTitas, discretizadoDeRos);

		int[][] matrizDeAcumulados = new int[matrizDeRectas.length][matrizDeRectas[0].length];

		for (int i = 0; i < imagenOriginalUmbralizada.getBufferedImage()
				.getWidth(); i++) {
			for (int j = 0; j < imagenOriginalUmbralizada.getBufferedImage()
					.getHeight(); j++) {

				Color colorEnUmbralizada = new Color(imagenOriginalUmbralizada
						.getBufferedImage().getRGB(i, j));
				if (colorEnUmbralizada.getRed() == 255
						&& colorEnUmbralizada.getGreen() == 255
						&& colorEnUmbralizada.getBlue() == 255) {

					evaluarPuntoEnLaMatriz(matrizDeRectas, matrizDeAcumulados,
							i, j);
				}
			}
		}

		List<Point> rectasMaximas = buscarRectasMaximas(matrizDeRectas,
				matrizDeAcumulados, umbral);

		dibujarLasRectas(imagenOriginalUmbralizada, rectasMaximas);
		ventana.refrescarImagen();
		return null;
	}

	private static void dibujarLasRectas(Imagen imagen, List<Point> rectas) {

		for (Point recta: rectas){
			
			//angulo
			if ( recta.x == 0 || recta.x == 180){
				
				for (int i=0; i< imagen.getBufferedImage().getHeight(); i++){
					imagen.getBufferedImage().setRGB(recta.y, i, Color.RED.getRGB());
				}
			}
			
			if ( recta.x == 90 || recta.x == 270 ){
				
				for (int i=0; i< imagen.getBufferedImage().getWidth(); i++){
					imagen.getBufferedImage().setRGB(i, recta.y, Color.GREEN.getRGB());
				}
			}
			
		}
	}

	private static List<Point> buscarRectasMaximas(Point[][] matrizDeRectas,
			int[][] matrizDeAcumulados, int umbral) {

		int maximo = Integer.MIN_VALUE;
		List<Point> posiciones = new ArrayList<Point>();
		List<Point> rectas = new ArrayList<Point>();

		for (int i = 0; i < matrizDeAcumulados.length; i++) {
			for (int j = 0; j < matrizDeAcumulados[0].length; j++) {

				if (matrizDeAcumulados[i][j] > maximo) {

					maximo = matrizDeAcumulados[i][j];
					posiciones.clear();
					posiciones.add(new Point(i, j));
				} else if (matrizDeAcumulados[i][j] == maximo) {

					posiciones.add(new Point(i, j));
				}
			}
		}

		//Busco las proximas al maximo
		posiciones.clear();
		for (int i = 0; i < matrizDeAcumulados.length; i++) {
			for (int j = 0; j < matrizDeAcumulados[0].length; j++) {

				if (maximo - matrizDeAcumulados[i][j] < umbral ) {

					posiciones.add(new Point(i, j));
				}	
			}
		}
		
		for (Point puntos : posiciones) {

			int ro = matrizDeRectas[puntos.x][puntos.y].x;
			int tita = matrizDeRectas[puntos.x][puntos.y].y;

			rectas.add(new Point(ro, tita));
		}

		return rectas;
	}

	private static void evaluarPuntoEnLaMatriz(Point[][] matrizDeRectas,
			int[][] matrizDeAcumulados, int i, int j) {

		for (int w = 0; w < matrizDeRectas[0].length; w++) {
			for (int h = 0; h < matrizDeRectas.length; h++) {

				int tita = matrizDeRectas[h][w].x;
				int ro = matrizDeRectas[h][w].y;

				float ecuacion = (float) Math.abs(ro
						- (i * Math.cos(Math.toRadians(tita)))
						- (j * Math.sin(Math.toRadians(tita))));
				float epsilon = 0.1f;

				if (ecuacion < epsilon) {

					matrizDeAcumulados[h][w] = matrizDeAcumulados[h][w] + 1;
				}
			}
		}
	}

	private static Point[][] crearMatrizDeRectas(int titaMin, int titaMax,
			int roMin, int roMax, int discretizadoTitas, int discretizadoRos) {

		int cantidadDeTitas = (int) ((float) ((titaMax - titaMin) / discretizadoTitas)) + 1;
		int cantidadDeRos = (int) ((float) ((roMax - roMin) / discretizadoRos)) + 1;
		Point[][] matrizDeRectas = new Point[cantidadDeTitas][cantidadDeRos];

		for (int i = 0; i < cantidadDeTitas; i++) {
			for (int j = 0; j < cantidadDeRos; j++) {

				matrizDeRectas[i][j] = new Point(titaMin
						+ (discretizadoTitas * i), roMin
						+ (discretizadoRos * j));
			}
		}

		return matrizDeRectas;
	}
}
