package ar.com.untref.imagenes.procesamiento;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.modelo.Archivo;
import ar.com.untref.imagenes.modelo.Imagen;

public class ProcesadorDeImagenes {

	private static ProcesadorDeImagenes instancia;
	private Archivo archivoActual;
	private Imagen imagenActual;

	private ProcesadorDeImagenes(){}
	
	public static ProcesadorDeImagenes obtenerInstancia(){
		
		if ( instancia==null ){
			
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
				"JPG & GIF & BMP & RAW", "raw", "jpg", "gif", "bmp");
		selector.setFileFilter(filtroImagen);

		int flag = selector.showOpenDialog(null);

		if (flag == JFileChooser.APPROVE_OPTION) {
			try {

				archivoActual = new Archivo(selector.getSelectedFile());
				FormatoDeImagen formatoDeLaImagen = FormatoDeImagen
						.getFormato(archivoActual.getExtension());

				BufferedImage bufferedImage;
				
				if (archivoActual.getExtension().equalsIgnoreCase("raw")){
					
					bufferedImage = leerUnaImagenRAW(archivoActual,256,256);
				} else {
					
					bufferedImage = leerUnaImagen();
				}

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

	private BufferedImage leerUnaImagenRAW(Archivo archivoActual, int width, int height) {

		BufferedImage imagen = null;
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(archivoActual.getFile().toPath());
			imagen = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
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
		BufferedImage bufferedImage = ImageIO.read(archivoActual
				.getFile());
		return bufferedImage;
	}

	public Imagen getImagenActual() {
		return imagenActual;
	}
	
	public int[][] calcularMatrizDeLaImagen(BufferedImage image) {

		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
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
	
	public BufferedImage getBufferedImageDeMatriz(int[][] matriz, int ancho, int alto){
		
		BufferedImage bufferedImage = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
	    for (int i = 0; i < matriz.length; i++) {
	        for (int j = 0; j < matriz[0].length; j++) {
	            int pixel=matriz[i][j];
	            bufferedImage.setRGB(i, j, pixel);
	        }
	    }
	    
	    return bufferedImage;
	}
	
}
