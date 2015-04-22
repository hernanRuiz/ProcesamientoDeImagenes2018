package ar.com.untref.imagenes.procesamiento;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.modelo.OperacionMatematica;
import ar.com.untref.imagenes.ventanas.VentanaHistogramaEcualizado;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;

public class OperacionesManager {

	public static void aplicarOperacionMatematica(VentanaPrincipal ventana, JPanel panel, Imagen primeraImagen,
			Imagen segundaImagen, OperacionMatematica operacion) {
		
		int[][] matriz1Rojos = MatricesManager.calcularMatrizDeLaImagen(primeraImagen.getBufferedImage(), Canal.ROJO);
		int[][] matriz1Verdes = MatricesManager.calcularMatrizDeLaImagen(primeraImagen.getBufferedImage(), Canal.VERDE);
		int[][] matriz1Azules = MatricesManager.calcularMatrizDeLaImagen(primeraImagen.getBufferedImage(), Canal.AZUL);
		
		int[][] matriz2Rojos = MatricesManager.calcularMatrizDeLaImagen(segundaImagen.getBufferedImage(), Canal.ROJO);
		int[][] matriz2Verdes = MatricesManager.calcularMatrizDeLaImagen(segundaImagen.getBufferedImage(), Canal.VERDE);
		int[][] matriz2Azules = MatricesManager.calcularMatrizDeLaImagen(segundaImagen.getBufferedImage(), Canal.AZUL);
		
		int cantidadFilasMatriz1 = matriz1Rojos.length;
		int cantidadColumnasMatriz1 = matriz1Rojos[0].length;
		int cantidadFilasMatriz2 = matriz2Rojos.length;
		int cantidadColumnasMatriz2 = matriz2Rojos[0].length;
		
		int[][] matrizResultanteRojos = new int[cantidadFilasMatriz1][cantidadColumnasMatriz2];
		int[][] matrizResultanteVerdes = new int[cantidadFilasMatriz1][cantidadColumnasMatriz2];
		int[][] matrizResultanteAzules = new int[cantidadFilasMatriz1][cantidadColumnasMatriz2];
		
		if (cantidadFilasMatriz1 == cantidadFilasMatriz2 && cantidadColumnasMatriz1 == cantidadColumnasMatriz2) {
			
			switch (operacion){
			
				case RESTA:
					
					matrizResultanteRojos = MatricesManager.aplicarTransformacionLineal(MatricesManager.restarMatrices(matriz1Rojos, matriz2Rojos));
					matrizResultanteVerdes = MatricesManager.aplicarTransformacionLineal(MatricesManager.restarMatrices(matriz1Verdes, matriz2Verdes));
					matrizResultanteAzules = MatricesManager.aplicarTransformacionLineal(MatricesManager.restarMatrices(matriz1Azules, matriz2Azules));
					break;
					
				case SUMA:
					
					matrizResultanteRojos = MatricesManager.aplicarTransformacionLineal(MatricesManager.sumarMatrices(matriz1Rojos, matriz2Rojos));
					matrizResultanteVerdes = MatricesManager.aplicarTransformacionLineal(MatricesManager.sumarMatrices(matriz1Verdes, matriz2Verdes));
					matrizResultanteAzules = MatricesManager.aplicarTransformacionLineal(MatricesManager.sumarMatrices(matriz1Azules, matriz2Azules));
					break;

				case MULTIPLICACION:
					
					matrizResultanteRojos = MatricesManager.aplicarTransformacionLogaritmica(MatricesManager.multiplicarMatrices(matriz1Rojos, matriz2Rojos));
					matrizResultanteVerdes = MatricesManager.aplicarTransformacionLogaritmica(MatricesManager.multiplicarMatrices(matriz1Verdes, matriz2Verdes));
					matrizResultanteAzules = MatricesManager.aplicarTransformacionLogaritmica(MatricesManager.multiplicarMatrices(matriz1Azules, matriz2Azules));
					break;

			default:
				
				break;
			}
			
			Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
			BufferedImage imagenResultante = MatricesManager.generarImagenRGB(matrizResultanteRojos, matrizResultanteVerdes, matrizResultanteAzules);
			
			Imagen nuevaImagenActual = new Imagen(imagenResultante, imagenAnterior.getFormato(), imagenAnterior.getNombre());
			ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
			ventana.refrescarImagen();
			
		} else {
			
			DialogsHelper.mostarMensaje(panel, "La "+ operacion.getDescripcion() +" no es posible si las matrices no coinciden en la cantidad de filas y columnas", NivelMensaje.ERROR);
			ProcesadorDeImagenes.obtenerInstancia().setImagenOriginal(primeraImagen);
		}
	}
	
	public static void operarConUnEscalar(VentanaPrincipal ventanaPrincipal, JPanel contentPane, int escalar, OperacionMatematica operacion){
		
		BufferedImage bufferedImage = ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage();

		int[][] matriz1Rojos = MatricesManager.calcularMatrizDeLaImagen(bufferedImage, Canal.ROJO);
		int[][] matriz1Verdes = MatricesManager.calcularMatrizDeLaImagen(bufferedImage, Canal.VERDE);
		int[][] matriz1Azules = MatricesManager.calcularMatrizDeLaImagen(bufferedImage, Canal.AZUL);
		
		int[][] matrizResultanteRojos = new int[matriz1Rojos.length][matriz1Rojos[0].length];
		int[][] matrizResultanteVerdes = new int[matriz1Rojos.length][matriz1Rojos[0].length];
		int[][] matrizResultanteAzules = new int[matriz1Rojos.length][matriz1Rojos[0].length];

		switch (operacion) {

			case SUMA:
		
				matrizResultanteRojos = MatricesManager.aplicarTransformacionLineal(MatricesManager.sumarMatrizYEscalar(matriz1Rojos, escalar));
				matrizResultanteVerdes = MatricesManager.aplicarTransformacionLineal(MatricesManager.sumarMatrizYEscalar(matriz1Verdes, escalar));
				matrizResultanteAzules = MatricesManager.aplicarTransformacionLineal(MatricesManager.sumarMatrizYEscalar(matriz1Azules, escalar));
				break;
				
			case RESTA:
				
				matrizResultanteRojos = MatricesManager.aplicarTransformacionLineal(MatricesManager.restarMatrizYEscalar(matriz1Rojos, escalar));
				matrizResultanteVerdes = MatricesManager.aplicarTransformacionLineal(MatricesManager.restarMatrizYEscalar(matriz1Verdes, escalar));
				matrizResultanteAzules = MatricesManager.aplicarTransformacionLineal(MatricesManager.restarMatrizYEscalar(matriz1Azules, escalar));
				break;
			
			case MULTIPLICACION:
				
				matrizResultanteRojos = MatricesManager.aplicarTransformacionLogaritmica(MatricesManager.multiplicarMatrizPorEscalar(matriz1Rojos, escalar));
				matrizResultanteVerdes = MatricesManager.aplicarTransformacionLogaritmica(MatricesManager.multiplicarMatrizPorEscalar(matriz1Verdes, escalar));
				matrizResultanteAzules = MatricesManager.aplicarTransformacionLogaritmica(MatricesManager.multiplicarMatrizPorEscalar(matriz1Azules, escalar));
			default:
				break;
		}
		
		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage imagenResultante = MatricesManager.generarImagenRGB(matrizResultanteRojos, matrizResultanteVerdes, matrizResultanteAzules);

		Imagen nuevaImagenActual = new Imagen(imagenResultante, imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
		ventanaPrincipal.refrescarImagen();
	}
	
	public static Imagen restarImagenes(VentanaHistogramaEcualizado ventana, JPanel panel, Imagen primeraImagen,
			Imagen segundaImagen, OperacionMatematica operacion) {
		
		int[][] matriz1Rojos = MatricesManager.calcularMatrizDeLaImagen(primeraImagen.getBufferedImage(), Canal.ROJO);
		int[][] matriz1Verdes = MatricesManager.calcularMatrizDeLaImagen(primeraImagen.getBufferedImage(), Canal.VERDE);
		int[][] matriz1Azules = MatricesManager.calcularMatrizDeLaImagen(primeraImagen.getBufferedImage(), Canal.AZUL);
		
		int[][] matriz2Rojos = MatricesManager.calcularMatrizDeLaImagen(segundaImagen.getBufferedImage(), Canal.ROJO);
		int[][] matriz2Verdes = MatricesManager.calcularMatrizDeLaImagen(segundaImagen.getBufferedImage(), Canal.VERDE);
		int[][] matriz2Azules = MatricesManager.calcularMatrizDeLaImagen(segundaImagen.getBufferedImage(), Canal.AZUL);
		
		int cantidadFilasMatriz1 = matriz1Rojos.length;
		int cantidadColumnasMatriz1 = matriz1Rojos[0].length;
		int cantidadFilasMatriz2 = matriz2Rojos.length;
		int cantidadColumnasMatriz2 = matriz2Rojos[0].length;
		
		int[][] matrizResultanteRojos = new int[cantidadFilasMatriz1][cantidadColumnasMatriz2];
		int[][] matrizResultanteVerdes = new int[cantidadFilasMatriz1][cantidadColumnasMatriz2];
		int[][] matrizResultanteAzules = new int[cantidadFilasMatriz1][cantidadColumnasMatriz2];
		
		if (cantidadFilasMatriz1 == cantidadFilasMatriz2 && cantidadColumnasMatriz1 == cantidadColumnasMatriz2) {
			
			matrizResultanteRojos = MatricesManager.aplicarTransformacionLineal(MatricesManager.restarMatrices(matriz1Rojos, matriz2Rojos));
			matrizResultanteVerdes = MatricesManager.aplicarTransformacionLineal(MatricesManager.restarMatrices(matriz1Verdes, matriz2Verdes));
			matrizResultanteAzules = MatricesManager.aplicarTransformacionLineal(MatricesManager.restarMatrices(matriz1Azules, matriz2Azules));
	
			Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
			BufferedImage imagenResultante = MatricesManager.generarImagenRGB(matrizResultanteRojos, matrizResultanteVerdes, matrizResultanteAzules);
			
			Imagen nuevaImagenActual = new Imagen(imagenResultante, imagenAnterior.getFormato(), imagenAnterior.getNombre());
			
			return nuevaImagenActual;
			
		} else {
			
			DialogsHelper.mostarMensaje(panel, "La "+ operacion.getDescripcion() +" no es posible si las matrices no coinciden en la cantidad de filas y columnas", NivelMensaje.ERROR);
			ProcesadorDeImagenes.obtenerInstancia().setImagenOriginal(primeraImagen);
			
			return null;
		}
	}
	
}
