package ar.com.untref.imagenes.procesamiento;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.modelo.Archivo;
import ar.com.untref.imagenes.modelo.Imagen;

public class ProcesadorDeImagenes {

	private Archivo archivoActual;
	private Imagen imagenActual;

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
				"JPG & GIF & BMP & RAW", "raw", "jpg", "gif", "bmp");
		selector.setFileFilter(filtroImagen);

		int flag = selector.showOpenDialog(null);

		if (flag == JFileChooser.APPROVE_OPTION) {
			try {

				archivoActual = new Archivo(selector.getSelectedFile());
				BufferedImage bufferedImage = ImageIO.read(archivoActual
						.getFile());

				FormatoDeImagen formatoDeLaImagen = FormatoDeImagen
						.getFormato(archivoActual.getExtension());

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

	public Imagen getImagenActual() {
		return imagenActual;
	}

	public int[][] getMatrizDeLaImagen(BufferedImage image) {

		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;

		int[][] result = new int[height][width];
		if (hasAlphaChannel) {
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
				argb += ((int) pixels[pixel + 1] & 0xff); // blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
				result[row][col] = argb;
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
				result[row][col] = argb;
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		}

		return result;
	}
	
}
