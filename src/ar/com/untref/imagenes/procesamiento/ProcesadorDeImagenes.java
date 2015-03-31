package ar.com.untref.imagenes.procesamiento;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.helpers.FormulasHelper;
import ar.com.untref.imagenes.modelo.Archivo;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;

public class ProcesadorDeImagenes {

	private static ProcesadorDeImagenes instancia;
	private Archivo archivoActual;
	private Imagen imagenActual;

	private ProcesadorDeImagenes() {
	}

	public static ProcesadorDeImagenes obtenerInstancia() {

		if (instancia == null) {

			instancia = new ProcesadorDeImagenes();
		}

		return instancia;
	}

	/**
	 * Abre una imagen de archivo y la convierte en buffered image.
	 * 
	 * @return Imagen
	 */
	public Imagen cargarUnaImagenDesdeArchivo() {

		Imagen imagenADevolver = null;

		JFileChooser selector = new JFileChooser();
		selector.setDialogTitle("Seleccione una imagen");

		FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter(
				"JPG & GIF & BMP & PNG", "jpg", "gif", "bmp", "png");
		selector.setFileFilter(filtroImagen);

		int flag = selector.showOpenDialog(null);

		if (flag == JFileChooser.APPROVE_OPTION) {
			try {

				archivoActual = new Archivo(selector.getSelectedFile());
				FormatoDeImagen formatoDeLaImagen = FormatoDeImagen
						.getFormato(archivoActual.getExtension());

				BufferedImage bufferedImage = leerUnaImagen();

				if (formatoDeLaImagen != FormatoDeImagen.DESCONOCIDO) {

					Imagen imagen = new Imagen(bufferedImage,
							formatoDeLaImagen, archivoActual.getNombre());

					imagenActual = imagen;
					imagenADevolver = imagen;
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		return imagenADevolver;
	}

	/**
	 * Abre una imagen en formato RAW de archivo, con las medidas definidas y la
	 * convierte en buffered image.
	 * 
	 * @return Imagen
	 */
	public Imagen cargarUnaImagenRawDesdeArchivo(Integer alto, Integer ancho) {

		Imagen imagenADevolver = null;

		JFileChooser selector = new JFileChooser();
		selector.setDialogTitle("Seleccione una imagen RAW");

		FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter(
				"RAW", "raw");
		selector.setFileFilter(filtroImagen);

		int flag = selector.showOpenDialog(null);

		if (flag == JFileChooser.APPROVE_OPTION) {
			try {

				archivoActual = new Archivo(selector.getSelectedFile());
				FormatoDeImagen formatoDeLaImagen = FormatoDeImagen
						.getFormato(archivoActual.getExtension());

				BufferedImage bufferedImage;
				bufferedImage = leerUnaImagenRAW(archivoActual, alto, ancho);

				Imagen imagen = new Imagen(bufferedImage, formatoDeLaImagen,
						archivoActual.getNombre());
				imagenActual = imagen;
				imagenADevolver = imagen;
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		return imagenADevolver;
	}

	private BufferedImage leerUnaImagenRAW(Archivo archivoActual, int width,
			int height) {

		BufferedImage imagen = null;
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(archivoActual.getFile().toPath());
			imagen = new BufferedImage(width, height,
					BufferedImage.TYPE_3BYTE_BGR);
			double[][] matrizDeImagen = new double[width][height];
			int contador = 0;
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					matrizDeImagen[j][i] = bytes[contador];
					imagen.setRGB(j, i, bytes[contador]);
					contador++;
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return imagen;
	}

	private BufferedImage leerUnaImagen() throws IOException {
		BufferedImage bufferedImage = ImageIO.read(archivoActual.getFile());
		return bufferedImage;
	}

	public Imagen getImagenActual() {
		return imagenActual;
	}

	public int[][] calcularMatrizDeLaImagen(BufferedImage image) {

		final byte[] pixels = ((DataBufferByte) image.getRaster()
				.getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;

		int[][] matriz = new int[height][width];
		if (hasAlphaChannel) {
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
				argb += ((int) pixels[pixel + 1] & 0xff); // blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
				matriz[row][col] = argb;
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		} else {
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				matriz[row][col] = argb;
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		}

		return matriz;
	}

	public BufferedImage getBufferedImageDeMatriz(int[][] matriz, int ancho,
			int alto) {

		BufferedImage bufferedImage = new BufferedImage(ancho, alto,
				BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				int pixel = matriz[i][j];
				bufferedImage.setRGB(i, j, pixel);
			}
		}

		return bufferedImage;
	}

	public void recortarImagenActual(Integer x1, Integer y1, Integer x2,
			Integer y2, VentanaPrincipal ventana) {

		if (imagenActual != null) {

			int ancho = x2 - x1;
			int alto = y2 - y1;
			int[][] matrizRecortada = new int[ancho + 1][alto + 1];

			for (int i = 0; i <= ancho; i++) {
				for (int j = 0; j <= alto; j++) {
					int valorDelPixel = imagenActual.getBufferedImage().getRGB(
							i + x1, j + y1);

					matrizRecortada[i][j] = valorDelPixel;
				}
			}

			BufferedImage imagenRecortada = getBufferedImageDeMatriz(
					matrizRecortada, ancho + 1, alto + 1);
			Imagen nuevaImagenRecortada = new Imagen(imagenRecortada,
					imagenActual.getFormato(), imagenActual.getNombre());
			this.imagenActual = nuevaImagenRecortada;
			ventana.refrescarImagen();
		}
	}

	public Imagen aplicarNegativo(Imagen imagen) {

		Imagen imagenEnNegativo = null;
		
		if (imagen != null) {

			BufferedImage resultado = imagen.getBufferedImage();

			for (int x = 0; x < resultado.getWidth(); x++) {
				for (int y = 0; y < resultado.getHeight(); y++) {

					int rgba = resultado.getRGB(x, y);
					Color col = new Color(rgba, true);
					col = new Color(255 - col.getRed(), 255 - col.getGreen(),
							255 - col.getBlue());
					resultado.setRGB(x, y, col.getRGB());
				}
			}

			imagenEnNegativo = new Imagen(resultado, imagen.getFormato(), imagen.getNombre());
			this.imagenActual = imagenEnNegativo;
		}
		
		return imagenEnNegativo;
	}

	public void setImagenActual(Imagen imagen) {

		this.imagenActual = imagen;
	}
	
	public Archivo getArchivoActual(){
		
		return this.archivoActual;
	}
	
	public void aumentarContrastePorElCuadrado(Imagen imagen){
		
		BufferedImage buffered = imagen.getBufferedImage();
		
		for (int x = 0; x < buffered.getWidth(); x++) {
			for (int y = 0; y < buffered.getHeight(); y++) {

				int rgba = buffered.getRGB(x, y);
				Color col = new Color(rgba, true);
				col = new Color(FormulasHelper.potenciarColorPorSuCuadrado(col
						.getRed()),
						FormulasHelper.potenciarColorPorSuCuadrado(col
								.getGreen()),
						FormulasHelper.potenciarColorPorSuCuadrado(col
								.getBlue()));
				buffered.setRGB(x, y, col.getRGB());
			}
		}
		this.imagenActual.setBufferedImage(buffered);
	}
	
	public void aumentoContrasteAutomatico(Imagen imagen) {

		BufferedImage buffered = imagen.getBufferedImage();
		int minimoRojo = 255;
		int maximoRojo = 0;
		int minimoVerde = 255;
		int maximoVerde = 0;
		int minimoAzul = 255;
		int maximoAzul = 0;

		//Busco valores maximos y minimos por banda
		for (int x = 0; x < buffered.getWidth(); x++) {
			for (int y = 0; y < buffered.getHeight(); y++) {

				int rgba = buffered.getRGB(x, y);
				Color col = new Color(rgba, true);

				if (col.getRed() < minimoRojo) {

					minimoRojo = col.getRed();
				} else if (col.getRed() > maximoRojo) {

					maximoRojo = col.getRed();
				}

				if (col.getGreen() < minimoVerde) {

					minimoVerde = col.getGreen();
				} else if (col.getGreen() > maximoVerde) {

					maximoVerde = col.getGreen();
				}

				if (col.getBlue() < minimoAzul) {

					minimoAzul = col.getBlue();
				} else if (col.getBlue() > maximoAzul) {

					maximoAzul = col.getBlue();
				}
			}
		}

		for (int x = 0; x < buffered.getWidth(); x++) {
			for (int y = 0; y < buffered.getHeight(); y++) {

				int rgba = buffered.getRGB(x, y);
				Color col = new Color(rgba, true);
				col = new Color(
						FormulasHelper.aplicarFormulaVariacionContrasteAutomatico(
								col.getRed(), minimoRojo, maximoRojo),
						FormulasHelper
								.aplicarFormulaVariacionContrasteAutomatico(
										col.getGreen(), minimoVerde,
										maximoVerde),
						FormulasHelper
								.aplicarFormulaVariacionContrasteAutomatico(
										col.getBlue(), minimoAzul, maximoAzul));
				buffered.setRGB(x, y, col.getRGB());
			}
		}
		this.imagenActual.setBufferedImage(buffered);
	}
	
	/**
	 * @param imagen - imagen a umbralizar
	 * @param umbral - valor que hará de separador entre valores 0 y 255
	 */
	public void umbralizarImagen(Imagen imagen, int umbral){
		
		BufferedImage buffered = imagen.getBufferedImage();
		
		for (int x = 0; x < buffered.getWidth(); x++) {
			for (int y = 0; y < buffered.getHeight(); y++) {

				int rgba = buffered.getRGB(x, y);
				Color col = new Color(rgba, true);
				
				if ( col.getRed()<= umbral){
					
					col = new Color(0,0,0);
				} else {
					
					col = new Color(255,255,255);
				}
				buffered.setRGB(x, y, col.getRGB());
			}
		}
		
		this.imagenActual.setBufferedImage(buffered);
	}

}
