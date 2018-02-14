package ar.com.untref.imagenes.procesamiento;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.filtros.FiltroGaussiano;
import ar.com.untref.imagenes.modelo.Archivo;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;
import ar.com.untref.imagenes.ventanas.VentanaRuido;

public class ProcesadorDeImagenes {

	private static ProcesadorDeImagenes instancia;
	private Archivo archivoActual;
	private static Imagen imagenActual;
	private static Imagen imagenOriginal;

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

		JFileChooser selector = new JFileChooser("D:\\1736 - 3 Hernan Ruiz\\User Hernan\\DocT\\Resultados");
		selector.setDialogTitle("Seleccione una imagen");

		FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter(
				"JPG & GIF & BMP & PNG & PGM", "jpg", "gif", "bmp", "png", "pgm");
		
		selector.setFileFilter(filtroImagen);

		int flag = selector.showOpenDialog(null);

		if (flag == JFileChooser.APPROVE_OPTION) {
			try {

				archivoActual = new Archivo(selector.getSelectedFile());
				FormatoDeImagen formatoDeLaImagen = FormatoDeImagen
						.getFormato(archivoActual.getExtension());

				BufferedImage bufferedImage = leerUnaImagen();

				if (formatoDeLaImagen != FormatoDeImagen.DESCONOCIDO) {

						Imagen imagen;
						
						if (formatoDeLaImagen != FormatoDeImagen.PGM){
	
							imagen = new Imagen(bufferedImage,
									formatoDeLaImagen, archivoActual.getNombre());
							
						imagen.setMatriz(MatricesManager.calcularMatrizDeLaImagen(
								bufferedImage, Canal.ROJO), Canal.ROJO);
						imagen.setMatriz(MatricesManager.calcularMatrizDeLaImagen(
								bufferedImage, Canal.VERDE), Canal.VERDE);
						imagen.setMatriz(MatricesManager.calcularMatrizDeLaImagen(
								bufferedImage, Canal.AZUL), Canal.AZUL);
	
						imagenActual = new Imagen(imagen.getBufferedImage(),
								imagen.getFormato(), imagen.getNombre(),
								imagen.getMatriz(Canal.ROJO),
								imagen.getMatriz(Canal.VERDE),
								imagen.getMatriz(Canal.AZUL));
						imagenOriginal = new Imagen(imagen.getBufferedImage(),
								imagen.getFormato(), imagen.getNombre(),
								imagen.getMatriz(Canal.ROJO),
								imagen.getMatriz(Canal.VERDE),
								imagen.getMatriz(Canal.AZUL));
						imagenADevolver = new Imagen(imagen.getBufferedImage(),
								imagen.getFormato(), imagen.getNombre(),
								imagen.getMatriz(Canal.ROJO),
								imagen.getMatriz(Canal.VERDE),
								imagen.getMatriz(Canal.AZUL));
					} else {
						
						imagen  = new Imagen(readPGMFile(selector.getSelectedFile()),
								formatoDeLaImagen, archivoActual.getNombre());
						
						imagenActual = new Imagen(imagen.getBufferedImage(),
								imagen.getFormato(), imagen.getNombre());
						imagenOriginal = new Imagen(imagen.getBufferedImage(),
								imagen.getFormato(), imagen.getNombre());
						imagenADevolver = new Imagen(imagen.getBufferedImage(),
								imagen.getFormato(), imagen.getNombre());
					
					}
				} 
					
			} catch (Exception e) {

				e.printStackTrace();
			}
			
		}

		return imagenADevolver;
	}
	
	
	  /**
	   * Lee un archivo PGM y devuelve la imagen.
	   * @param archivo seleccionado
	   * @return BufferedImage
	 * @throws IOException 
	   */
	  public static BufferedImage readPGMFile(File archivoActual) throws IOException
	  {
	    
		FileInputStream f = new FileInputStream(archivoActual);
		BufferedReader d = new BufferedReader(new InputStreamReader(f));
		String line = d.readLine(); // second line contains height and width

		String valores[] = line.split(" ");

		int width = Integer.valueOf(valores[0]);
		int height = Integer.valueOf(valores[1]);

		line = d.readLine();

		int max = Integer.valueOf(line);

		while (line.startsWith("#")) {
			line = d.readLine();
		}

		int[][] image = new int[height][width];

		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				//normalizo a 255
				int value = d.read();
				value = (int) Math.round((((double) value) * 255) / max);
				image[i][j] = value;
			}
		}

		BufferedImage imagenResultado = MatricesManager
				.obtenerImagenDeMatrizPGM(image);
		
		d.close();

		return imagenResultado;
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

				int[][] matrizCanal = MatricesManager.calcularMatrizDeLaImagen(
						bufferedImage, Canal.ROJO);

				imagen.setMatriz(matrizCanal, Canal.ROJO);
				imagen.setMatriz(matrizCanal, Canal.VERDE);
				imagen.setMatriz(matrizCanal, Canal.AZUL);

				imagenActual = new Imagen(imagen.getBufferedImage(),
						imagen.getFormato(), imagen.getNombre(),
						imagen.getMatriz(Canal.ROJO),
						imagen.getMatriz(Canal.VERDE),
						imagen.getMatriz(Canal.AZUL));
				imagenOriginal = new Imagen(imagen.getBufferedImage(),
						imagen.getFormato(), imagen.getNombre(),
						imagen.getMatriz(Canal.ROJO),
						imagen.getMatriz(Canal.VERDE),
						imagen.getMatriz(Canal.AZUL));
				imagenADevolver = new Imagen(imagen.getBufferedImage(),
						imagen.getFormato(), imagen.getNombre(),
						imagen.getMatriz(Canal.ROJO),
						imagen.getMatriz(Canal.VERDE),
						imagen.getMatriz(Canal.AZUL));

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

					int argb = 0;
					argb += -16777216; // 255 alpha
					int blue = ((int) bytes[contador] & 0xff);
					int green = ((int) bytes[contador] & 0xff) << 8;
					int red = ((int) bytes[contador] & 0xff) << 16;
					int color = argb + red + green + blue;
					imagen.setRGB(j, i, color);
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

	public BufferedImage getBufferedImageDeMatriz(int[][] matriz, int ancho,
			int alto) {

		BufferedImage bufferedImage = new BufferedImage(ancho, alto,
				BufferedImage.TYPE_3BYTE_BGR);
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				int pixel = matriz[i][j];
				bufferedImage.setRGB(i, j, pixel);
			}
		}

		return bufferedImage;
	}

	public Imagen aplicarFiltroGaussiano(Imagen imagen, int sigma) {
		
		return FiltroGaussiano.aplicarFiltroGaussiano(imagen, sigma);
	}
	
	public void setImagenActual(Imagen imagen) {

		imagenActual = imagen;
	}

	public Archivo getArchivoActual() {

		return this.archivoActual;
	}

	public Imagen getImagenOriginal() {

		return imagenOriginal;
	}

	public void setImagenOriginal(Imagen imagen) {
		imagenOriginal = imagen;
	}


	/**
	 * @param imagen - imagen a umbralizar
	 * @param umbral - valor que har� de separador entre valores 0 y 255
	 */
	public void umbralizarImagen(VentanaPrincipal ventana, int umbral) {

		imagenActual = Umbralizador.umbralizarImagen(imagenOriginal, umbral);
		ventana.refrescarImagen();
	}

	public void encontrarUmbralGlobal(VentanaPrincipal ventana, int umbralViejo) {

		int deltaUmbral = 30;
		boolean finalizo = false;

		while (!finalizo) {

			int umbralNuevo = Umbralizador.encontrarNuevoUmbralGlobal(
					imagenOriginal, umbralViejo);
			finalizo = (Math.abs(umbralViejo - umbralNuevo) < deltaUmbral);

			umbralViejo = umbralNuevo;
		}

		umbralizarImagen(ventana, umbralViejo);
	}

	public void encontrarUmbralGlobal(VentanaRuido ventana, int umbralViejo) {

		int deltaUmbral = 30;
		boolean finalizo = false;

		while (!finalizo) {

			int umbralNuevo = Umbralizador.encontrarNuevoUmbralGlobal(
					imagenOriginal, umbralViejo);
			finalizo = (Math.abs(umbralViejo - umbralNuevo) < deltaUmbral);

			umbralViejo = umbralNuevo;
		}

		imagenActual = Umbralizador.umbralizarImagen(imagenOriginal,
				umbralViejo);
		ventana.refrescarImagen();
	}

	public BufferedImage clonarBufferedImage(BufferedImage buffered) {

		ColorModel cm = buffered.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = buffered.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public File obtenerFileDesdeArchivo() {

		File fileADevolver = null;

		JFileChooser selector = new JFileChooser();
		selector.setDialogTitle("Seleccione una imagen");

		FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter(
				"JPG & GIF & BMP & PNG", "jpg", "gif", "bmp", "png");
		selector.setFileFilter(filtroImagen);

		int flag = selector.showOpenDialog(null);

		if (flag == JFileChooser.APPROVE_OPTION) {
			try {

				fileADevolver = selector.getSelectedFile();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		return fileADevolver;
	}
}
