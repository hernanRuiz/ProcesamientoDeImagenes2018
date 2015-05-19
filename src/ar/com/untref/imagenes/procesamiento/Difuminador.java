package ar.com.untref.imagenes.procesamiento;

import java.awt.image.BufferedImage;

import ar.com.untref.imagenes.bordes.InterfaceDetectorDeBordes;
import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.modelo.Imagen;

public class Difuminador {
	
	private static Imagen imagenOriginal;

	/**
	 * Si la difusion es isotropica el detector de bordes es null.
	 * @param imagen
	 * @param detectorDeBordes
	 * @param repeticiones
	 * @param esIsotropica
	 * @return
	 */
	public static BufferedImage aplicarDifusion(Imagen imagen,
			InterfaceDetectorDeBordes detectorDeBordes, int repeticiones, boolean esIsotropica) {

		imagenOriginal = ProcesadorDeImagenes.obtenerInstancia().getImagenOriginal();
		
		Imagen imagenResultante = new Imagen(imagenOriginal.getBufferedImage(),
				imagenOriginal.getFormato(), imagenOriginal.getNombre(),
				imagenOriginal.getMatriz(Canal.ROJO),
				imagenOriginal.getMatriz(Canal.VERDE),
				imagenOriginal.getMatriz(Canal.AZUL));
		int ancho = imagen.getBufferedImage().getWidth();
		int alto = imagen.getBufferedImage().getHeight();

		int[][] matrizRojoResultante = new int[alto][ancho];
		int[][] matrizVerdeResultante = new int[alto][ancho];
		int[][] matrizAzulResultante = new int[alto][ancho];

		for (int h = 0; h < repeticiones; h++) {
			for (int i = 0; i < ancho; i++) {
				for (int j = 0; j < alto; j++) {

					int rojoActual = imagenResultante.getMatriz(Canal.ROJO)[j][i];
					int verdeActual = imagenResultante.getMatriz(Canal.VERDE)[j][i];
					int azulActual = imagenResultante.getMatriz(Canal.AZUL)[j][i];

					float derivadaNorteRojo = calcularDerivadaNorte(
							imagenResultante, i, j, Canal.ROJO);
					float derivadaNorteVerde = calcularDerivadaNorte(
							imagenResultante, i, j, Canal.VERDE);
					float derivadaNorteAzul = calcularDerivadaNorte(
							imagenResultante, i, j, Canal.AZUL);

					float derivadaEsteRojo = calcularDerivadaEste(
							imagenResultante, i, j, Canal.ROJO);
					float derivadaEsteVerde = calcularDerivadaEste(
							imagenResultante, i, j, Canal.VERDE);
					float derivadaEsteAzul = calcularDerivadaEste(
							imagenResultante, i, j, Canal.AZUL);

					float derivadaOesteRojo = calcularDerivadaOeste(
							imagenResultante, i, j, Canal.ROJO);
					float derivadaOesteVerde = calcularDerivadaOeste(
							imagenResultante, i, j, Canal.VERDE);
					float derivadaOesteAzul = calcularDerivadaOeste(
							imagenResultante, i, j, Canal.AZUL);

					float derivadaSurRojo = calcularDerivadaSur(
							imagenResultante, i, j, Canal.ROJO);
					float derivadaSurVerde = calcularDerivadaSur(
							imagenResultante, i, j, Canal.VERDE);
					float derivadaSurAzul = calcularDerivadaSur(
							imagenResultante, i, j, Canal.AZUL);

					float nuevoValorRojo;
					float nuevoValorVerde;
					float nuevoValorAzul;
					
					if (esIsotropica){
						
						nuevoValorRojo = calcularValorDifusionIsotropica(
								rojoActual, derivadaNorteRojo,
								derivadaSurRojo, derivadaEsteRojo,
								derivadaOesteRojo);
						nuevoValorVerde = calcularValorDifusionIsotropica(
								verdeActual, derivadaNorteVerde,
								derivadaSurVerde, derivadaEsteVerde,
								derivadaOesteVerde);
						nuevoValorAzul = calcularValorDifusionIsotropica(
								azulActual, derivadaNorteAzul,
								derivadaSurAzul, derivadaEsteAzul,
								derivadaOesteAzul);
					} else {
						
						nuevoValorRojo = calcularValorDifusionAnisotropica(
								detectorDeBordes, rojoActual, derivadaNorteRojo,
								derivadaSurRojo, derivadaEsteRojo,
								derivadaOesteRojo);
						nuevoValorVerde = calcularValorDifusionAnisotropica(
								detectorDeBordes, verdeActual, derivadaNorteVerde,
								derivadaSurVerde, derivadaEsteVerde,
								derivadaOesteVerde);
						nuevoValorAzul = calcularValorDifusionAnisotropica(
								detectorDeBordes, azulActual, derivadaNorteAzul,
								derivadaSurAzul, derivadaEsteAzul,
								derivadaOesteAzul);
					}
					
					matrizRojoResultante[j][i] = (int) nuevoValorRojo;
					matrizVerdeResultante[j][i] = (int) nuevoValorVerde;
					matrizAzulResultante[j][i] = (int) nuevoValorAzul;
				}
			}

			imagenResultante.setMatriz(matrizRojoResultante, Canal.ROJO);
			imagenResultante.setMatriz(matrizVerdeResultante, Canal.VERDE);
			imagenResultante.setMatriz(matrizAzulResultante, Canal.AZUL);

		}

		// aplicar transformada para mostrar la imagen resultante
		int[][] matrizRojoFinal = MatricesManager
				.aplicarTransformacionLineal(imagenResultante
						.getMatriz(Canal.ROJO));
		int[][] matrizVerdeFinal = MatricesManager
				.aplicarTransformacionLineal(imagenResultante
						.getMatriz(Canal.VERDE));
		int[][] matrizAzulFinal = MatricesManager
				.aplicarTransformacionLineal(imagenResultante
						.getMatriz(Canal.AZUL));

		// Transpuestas
		int[][] matrizRojoTranspuesta = new int[imagenResultante
				.getBufferedImage().getWidth()][imagenResultante
				.getBufferedImage().getHeight()];
		int[][] matrizVerdeTranspuesta = new int[imagenResultante
				.getBufferedImage().getWidth()][imagenResultante
				.getBufferedImage().getHeight()];
		int[][] matrizAzulTranspuesta = new int[imagenResultante
				.getBufferedImage().getWidth()][imagenResultante
				.getBufferedImage().getHeight()];

		for (int j = 0; j < matrizRojoFinal.length; j++) {
			for (int i = 0; i < matrizRojoFinal[0].length; i++) {
				matrizRojoTranspuesta[i][j] = matrizRojoFinal[j][i];
				matrizVerdeTranspuesta[i][j] = matrizVerdeFinal[j][i];
				matrizAzulTranspuesta[i][j] = matrizAzulFinal[j][i];
			}
		}

		imagenResultante.setBufferedImage(MatricesManager
				.obtenerImagenDeMatrices(matrizRojoTranspuesta,
						matrizVerdeTranspuesta, matrizAzulTranspuesta));

		return imagenResultante.getBufferedImage();
	}

	private static float calcularValorDifusionAnisotropica(
			InterfaceDetectorDeBordes detectorDeBordes, int colorActual,
			float derivadaNorte, float derivadaSur, float derivadaEste,
			float derivadaOeste) {

		float Cnij = detectorDeBordes.gradiente(derivadaNorte);
		float Csij = detectorDeBordes.gradiente(derivadaSur);
		float Ceij = detectorDeBordes.gradiente(derivadaEste);
		float Coij = detectorDeBordes.gradiente(derivadaOeste);

		float DnIijCnij = derivadaNorte * Cnij;
		float DsIijCsij = derivadaSur * Csij;
		float DeIijCeij = derivadaEste * Ceij;
		float DoIijCoij = derivadaOeste * Coij;

		float lambda = 0.25f;
		float nuevoValor = colorActual + lambda
				* (DnIijCnij + DsIijCsij + DeIijCeij + DoIijCoij);
		return nuevoValor;
	}
	
	private static float calcularValorDifusionIsotropica(
			int colorActual,
			float derivadaNorte, float derivadaSur, float derivadaEste,
			float derivadaOeste) {

		float lambda = 0.25f;
		float nuevoValor = colorActual + lambda * (derivadaNorte + derivadaSur + derivadaEste + derivadaOeste);
		return nuevoValor;
	}

	private static int calcularDerivadaEste(Imagen imagen, int j, int k,
			Canal canal) {

		int coordenada = 0;
		int valorADevolver = 0;
		int colorActual = 0;
		int colorCorrido = 0;

		switch (canal) {
		case ROJO:
			coordenada = j + 1;
			colorActual = imagen.getMatriz(Canal.ROJO)[k][j];
			if (coordenada < imagen.getBufferedImage().getWidth()
					&& coordenada >= 0) {

				colorCorrido = imagen.getMatriz(Canal.ROJO)[k][j + 1];
			} else {
				colorCorrido = colorActual;
			}
			valorADevolver = colorCorrido - colorActual;
			break;

		case VERDE:
			coordenada = j + 1;
			colorActual = imagen.getMatriz(Canal.VERDE)[k][j];

			if (coordenada < imagen.getBufferedImage().getWidth()
					&& coordenada >= 0) {

				colorCorrido = imagen.getMatriz(Canal.VERDE)[k][j + 1];
			} else {
				colorCorrido = colorActual;
			}
			valorADevolver = colorCorrido - colorActual;
			break;
		default:
			coordenada = j + 1;
			colorActual = imagen.getMatriz(Canal.AZUL)[k][j];
			if (coordenada < imagen.getBufferedImage().getWidth()
					&& coordenada >= 0) {

				colorCorrido = imagen.getMatriz(Canal.AZUL)[k][j + 1];
			} else {
				colorCorrido = colorActual;
			}
			valorADevolver = colorCorrido - colorActual;
			break;
		}

		return valorADevolver;
	}

	private static int calcularDerivadaOeste(Imagen imagen, int j, int k,
			Canal canal) {

		int valorADevolver = 0;
		int coordenada = 0;
		int colorActual = 0;
		int colorCorrido = 0;

		switch (canal) {
		case ROJO:
			coordenada = j - 1;
			colorActual = imagen.getMatriz(Canal.ROJO)[k][j];
			if (coordenada < imagen.getBufferedImage().getWidth()
					&& coordenada >= 0) {

				colorCorrido = imagen.getMatriz(Canal.ROJO)[k][j - 1];
			} else {
				colorCorrido = colorActual;
			}
			valorADevolver = colorCorrido - colorActual;
			break;

		case VERDE:
			coordenada = j - 1;
			colorActual = imagen.getMatriz(Canal.VERDE)[k][j];
			if (coordenada < imagen.getBufferedImage().getWidth()
					&& coordenada >= 0) {

				colorCorrido = imagen.getMatriz(Canal.VERDE)[k][j - 1];
			} else {
				colorCorrido = colorActual;
			}
			valorADevolver = colorCorrido - colorActual;
			break;

		default:
			coordenada = j - 1;
			colorActual = imagen.getMatriz(Canal.AZUL)[k][j];
			if (coordenada < imagen.getBufferedImage().getWidth()
					&& coordenada >= 0) {

				colorCorrido = imagen.getMatriz(Canal.AZUL)[k][j - 1];
			} else {
				colorCorrido = colorActual;
			}
			valorADevolver = colorCorrido - colorActual;
			break;
		}

		return valorADevolver;
	}

	private static int calcularDerivadaSur(Imagen imagen, int j, int k,
			Canal canal) {

		int valorADevolver = 0;
		int coordenada = 0;
		int colorActual = 0;
		int colorCorrido = 0;

		switch (canal) {
		case ROJO:
			coordenada = k + 1;
			colorActual = imagen.getMatriz(Canal.ROJO)[k][j];
			if (coordenada < imagen.getBufferedImage().getHeight()
					&& coordenada >= 0) {

				colorCorrido = imagen.getMatriz(Canal.ROJO)[k + 1][j];
			} else {
				colorCorrido = colorActual;
			}
			valorADevolver = colorCorrido - colorActual;
			break;

		case VERDE:
			coordenada = k + 1;
			colorActual = imagen.getMatriz(Canal.VERDE)[k][j];
			if (coordenada < imagen.getBufferedImage().getHeight()
					&& coordenada >= 0) {

				colorCorrido = imagen.getMatriz(Canal.VERDE)[k + 1][j];
			} else {
				colorCorrido = colorActual;
			}
			valorADevolver = colorCorrido - colorActual;
			break;

		default:
			coordenada = k + 1;
			colorActual = imagen.getMatriz(Canal.AZUL)[k][j];
			if (coordenada < imagen.getBufferedImage().getHeight()
					&& coordenada >= 0) {

				colorCorrido = imagen.getMatriz(Canal.AZUL)[k + 1][j];
			} else {
				colorCorrido = colorActual;
			}
			valorADevolver = colorCorrido - colorActual;
			break;
		}

		return valorADevolver;
	}

	private static int calcularDerivadaNorte(Imagen imagen, int j, int k,
			Canal canal) {

		int valorADevolver = 0;
		int coordenada = 0;
		int colorActual = 0;
		int colorCorrido = 0;

		switch (canal) {
		case ROJO:
			coordenada = k - 1;
			colorActual = imagen.getMatriz(Canal.ROJO)[k][j];
			if (coordenada < imagen.getBufferedImage().getHeight()
					&& coordenada >= 0) {

				colorCorrido = imagen.getMatriz(Canal.ROJO)[k - 1][j];
			} else {
				colorCorrido = colorActual;
			}
			valorADevolver = colorCorrido - colorActual;
			break;

		case VERDE:
			coordenada = k - 1;
			colorActual = imagen.getMatriz(Canal.VERDE)[k][j];
			if (coordenada < imagen.getBufferedImage().getHeight()
					&& coordenada >= 0) {

				colorCorrido = imagen.getMatriz(Canal.VERDE)[k - 1][j];
			} else {
				colorCorrido = colorActual;
			}
			valorADevolver = colorCorrido - colorActual;
			break;

		default:
			coordenada = k - 1;
			colorActual = imagen.getMatriz(Canal.AZUL)[k][j];
			if (coordenada < imagen.getBufferedImage().getHeight()
					&& coordenada >= 0) {

				colorCorrido = imagen.getMatriz(Canal.AZUL)[k - 1][j];
			} else {
				colorCorrido = colorActual;
			}
			valorADevolver = colorCorrido - colorActual;
			break;
		}

		return valorADevolver;
	}

}
