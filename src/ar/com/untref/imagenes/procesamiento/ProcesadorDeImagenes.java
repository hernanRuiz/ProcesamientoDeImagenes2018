package ar.com.untref.imagenes.procesamiento;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
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
	private Imagen imagenOriginal;
	private static JLabel labelImagenMarcada;
	private Integer x1;
	private Integer x2;
	private Integer y1;
	private Integer y2;

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
					imagenOriginal = imagen;
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
				imagenOriginal = imagen;
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
					
					int argb = 0;
					argb += -16777216; // 255 alpha
					int blue = ( (int)bytes[contador] & 0xff);
		            int green = ((int)bytes[contador] & 0xff) << 8;
		            int red = ((int)bytes[contador] & 0xff) << 16;
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

	public void recortarImagenActual(Integer x1, Integer y1, Integer x2,
			Integer y2, VentanaPrincipal ventana) {

		int cantidadPixeles = 0;
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

			BufferedImage imagenRecortada = getBufferedImageDeMatriz(matrizRecortada, ancho+1, alto+1);
			cantidadPixeles = imagenRecortada.getWidth()* imagenRecortada.getHeight();
			Imagen nuevaImagenRecortada = new Imagen(imagenRecortada, imagenActual.getFormato(), imagenActual.getNombre());
			this.imagenActual = nuevaImagenRecortada;
			ventana.refrescarImagen();
			ventana.refrescarCantidadPixeles(cantidadPixeles);
		}
	}
	
	public int[] calcularValoresPromedio(BufferedImage bufferedImage, int ancho, int alto){
		int acumuladorRojo = 0;
		int acumuladorVerde = 0;
		int acumuladorAzul = 0;
		int promedioRojo = 0;
		int promedioVerde = 0;
		int promedioAzul = 0;
		int cantidadPixeles = ancho * alto;
		int[] valoresPromedio = new int[3];
		Color color;

		for (int i = 0; i < ancho; i++) {
	        for (int j = 0; j < alto; j++) {
	        	color = new Color(bufferedImage.getRGB(i, j));
	        	acumuladorRojo+=color.getRed();
	        	acumuladorVerde+=color.getGreen();
	        	acumuladorAzul+=color.getBlue();
	        }
		}
		
		promedioRojo = (acumuladorRojo / cantidadPixeles);
		promedioVerde = (acumuladorVerde / cantidadPixeles);
		promedioAzul = (acumuladorAzul / cantidadPixeles);
		valoresPromedio[0] = promedioRojo;
		valoresPromedio[1] = promedioVerde;
		valoresPromedio[2] = promedioAzul;
		return valoresPromedio;
	}

	public Imagen aplicarNegativo(Imagen imagen) {
		
		Imagen imagenEnNegativo = null;
		
		if (imagen != null) {

			this.imagenOriginal = imagen;
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
		}
		
		return imagenEnNegativo;
	}

	public void setImagenActual(Imagen imagen) {

		this.imagenActual = imagen;
	}
	
	public Archivo getArchivoActual(){
		
		return this.archivoActual;
	}
	
	public Imagen getImagenOriginal(){
		
		return imagenOriginal;
	}
	
	public void setImagenOriginal(Imagen imagen){
		this.imagenOriginal = imagen;
	}
	
	public void aumentarContrastePorFactor(Imagen imagen){
		
		setImagenOriginal(imagen);
		BufferedImage buffered = imagen.getBufferedImage();
		
		int[][] matrizRojos = new int[imagen.getBufferedImage().getWidth()][imagen.getBufferedImage().getHeight()];
		int[][] matrizVerdes = new int[imagen.getBufferedImage().getWidth()][imagen.getBufferedImage().getHeight()];
		int[][] matrizAzules = new int[imagen.getBufferedImage().getWidth()][imagen.getBufferedImage().getHeight()];

		
		for (int x = 0; x < buffered.getWidth(); x++) {
			for (int y = 0; y < buffered.getHeight(); y++) {

				int rgba = buffered.getRGB(x, y);
				
				Color col = new Color(rgba, true);
				
				matrizRojos[x][y] = FormulasHelper.aumentoContrasteConFactor(col.getRed(), 2f);
				
				matrizVerdes[x][y] =  FormulasHelper.aumentoContrasteConFactor(col.getGreen(), 2f);
				
				matrizAzules[x][y] = FormulasHelper.aumentoContrasteConFactor(col.getBlue(), 2f);
			}
		}
		
		matrizRojos = MatricesManager.aplicarTransformacionLogaritmica(matrizRojos);
		matrizVerdes = MatricesManager.aplicarTransformacionLogaritmica(matrizVerdes);
		matrizAzules = MatricesManager.aplicarTransformacionLogaritmica(matrizAzules);
		
		buffered = MatricesManager.generarImagenRGBconContraste(matrizRojos, matrizVerdes, matrizAzules);
		
		this.imagenActual= new Imagen(buffered, imagenOriginal.getFormato(), imagenOriginal.getNombre());
	}
	
	/**
	 * @param imagen - imagen a umbralizar
	 * @param umbral - valor que hará de separador entre valores 0 y 255
	 */
	public void umbralizarImagen(int umbral){
		
		BufferedImage buffered = new BufferedImage(imagenOriginal.getBufferedImage().getWidth(), imagenOriginal.getBufferedImage().getHeight(), imagenOriginal.getBufferedImage().getType());
		
		for (int x = 0; x < buffered.getWidth(); x++) {
			for (int y = 0; y < buffered.getHeight(); y++) {

				int rgba = imagenOriginal.getBufferedImage().getRGB(x, y);
				Color col = new Color(rgba, true);
				
				if ( col.getRed()<= umbral){
					
					col = new Color(0,0,0);
				} else {
					
					col = new Color(255,255,255);
				}
				buffered.setRGB(x, y, col.getRGB());
			}
		}
		
		this.imagenActual = new Imagen(buffered, imagenOriginal.getFormato(), imagenOriginal.getNombre());
	}
	
	public BufferedImage aplicarTransformacionLogaritmica(BufferedImage bufferedImage){
		
		float rojoMax;
		float verdeMax;
		float azulMax;

		BufferedImage imagenTransformada;
		int nrows = bufferedImage.getWidth();
		int ncols = bufferedImage.getHeight();
		imagenTransformada = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);
		
//		Color color = new Color(bufferedImage.getRGB(0, 0));
		rojoMax = 255;
		verdeMax = 255;
		azulMax = 255;

		for (int f = 0; f < nrows; f++) {
			for (int g = 0; g < ncols; g++) {

				Color colorActual = new Color(bufferedImage.getRGB(f, g));
				int rojoActual = colorActual.getRed();
				int verdeActual = colorActual.getGreen();
				int azulActual = colorActual.getBlue();
				
				if (rojoMax < rojoActual) {
					rojoMax = rojoActual;
				}

				if (verdeMax < verdeActual) {
					verdeMax = verdeActual;
				}

				if (azulMax < azulActual) {
					azulMax = azulActual;
				}

			}

		}

		float[] maximosYMinimos = new float[6];
		maximosYMinimos[0] = rojoMax;
		maximosYMinimos[1] = verdeMax;
		maximosYMinimos[2] = azulMax;
		
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {

				Color colorActual = new Color(bufferedImage.getRGB(i, j));
				int rojoActual = colorActual.getRed();
				int verdeActual = colorActual.getGreen();
				int azulActual = colorActual.getBlue();
				
				int rojoTransformado = (int) ((255f / (Math.log(rojoMax))) * Math.log(1 + rojoActual));
				int verdeTransformado = (int) ((255f / (Math.log(verdeMax))) * Math.log(1 + verdeActual));
				int azulTransformado = (int) ((255f / (Math.log(azulMax))) * Math.log(1 + azulActual));

				Color colorModificado = new Color(rojoTransformado, verdeTransformado, azulTransformado);
				imagenTransformada.setRGB(i, j, colorModificado.getRGB());
			}
		}
		
		return imagenTransformada;
	}
	
	public BufferedImage aplicarTransformacionLineal(BufferedImage bufferedImage){
		
		float rojoMin;
		float rojoMax;
		float verdeMin;
		float verdeMax;
		float azulMin;
		float azulMax;

		BufferedImage imagenTransformada;
		int nrows = bufferedImage.getWidth();
		int ncols = bufferedImage.getHeight();
		imagenTransformada = new BufferedImage(nrows, ncols, BufferedImage.TYPE_3BYTE_BGR);
		
		//Color color = new Color(bufferedImage.getRGB(0, 0));
		rojoMin = 0;
		rojoMax = 255;
		verdeMin = 0;
		verdeMax = 255;
		azulMin = 0;
		azulMax = 255;

		for (int f = 0; f < nrows; f++) {
			for (int g = 0; g < ncols; g++) {
		
				Color colorActual = new Color(bufferedImage.getRGB(f, g));
				int rojoActual = colorActual.getRed();
				int verdeActual = colorActual.getGreen();
				int azulActual = colorActual.getBlue();
				
				if (rojoMin > rojoActual) {
					rojoMin = rojoActual;
				}

				if (rojoMax < rojoActual) {
					rojoMax = rojoActual;
				}

				if (verdeMin > verdeActual) {
					verdeMin = verdeActual;
				}

				if (verdeMax < verdeActual) {
					verdeMax = verdeActual;
				}

				if (azulMin > azulActual) {
					azulMin = azulActual;
				}

				if (azulMax < azulActual) {
					azulMax = azulActual;
				}

			}

		}

		float[] maximosYMinimos = new float[6];
		maximosYMinimos[0] = rojoMin;
		maximosYMinimos[1] = rojoMax;
		maximosYMinimos[2] = verdeMin;
		maximosYMinimos[3] = verdeMax;
		maximosYMinimos[4] = azulMin;
		maximosYMinimos[5] = azulMax;
		
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {

				Color colorActual = new Color(bufferedImage.getRGB(i, j));
				int rojoActual = colorActual.getRed();
				int verdeActual = colorActual.getGreen();
				int azulActual = colorActual.getBlue();
				
				int rojoTransformado = (int) ((((255f) / (rojoMax - rojoMin)) * rojoActual) - ((rojoMin * 255f) / (rojoMax - rojoMin)));
				int verdeTransformado = (int) (((255f / (verdeMax - verdeMin)) * verdeActual) - ((verdeMin * 255f) / (verdeMax - verdeMin)));
				int azulTransformado = (int) (((255f / (azulMax - azulMin)) * azulActual) - ((azulMin * 255f) / (azulMax - azulMin)));

				Color colorModificado = new Color(rojoTransformado, verdeTransformado, azulTransformado);
				imagenTransformada.setRGB(i, j, colorModificado.getRGB());
			}
		}
		
		return imagenTransformada;
	}

	public JLabel marcarImagenActual(Integer x1, Integer y1, Integer x2,
			Integer y2, VentanaPrincipal ventana) {

		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		
		int cantidadPixeles = 0;
		if (imagenActual != null) {

			int ancho = x2 - x1;
			int alto = y2 - y1;
			/*int[][] matrizRecortada = new int[ancho + 1][alto + 1];

			for (int i = 0; i <= ancho; i++) {
				for (int j = 0; j <= alto; j++) {
					int valorDelPixel = imagenActual.getBufferedImage().getRGB(
							i + x1, j + y1);

					matrizRecortada[i][j] = valorDelPixel;
				}
			}*/
			
			//BufferedImage imagenRecortada = getBufferedImageDeMatriz(matrizRecortada, ancho+1, alto+1);
			BufferedImage imagenRecortada = imagenActual.getBufferedImage().getSubimage(x1, y1, ancho, alto);
			cantidadPixeles = imagenRecortada.getWidth()* imagenRecortada.getHeight();
			Imagen nuevaImagenRecortada = new Imagen(imagenRecortada, imagenActual.getFormato(), imagenActual.getNombre());
			this.imagenActual = nuevaImagenRecortada;
			labelImagenMarcada = new JLabel(new ImageIcon(imagenActual.getBufferedImage()));
			
			ventana.mostrarImagenMarcada();
			ventana.refrescarCantidadPixeles(cantidadPixeles);
		}
		return labelImagenMarcada;
	}
	
	public Integer[] getXEY(){
		Integer[] valoresXEY = new Integer[4];
		valoresXEY[0] = x1;
		valoresXEY[1] = x2;
		valoresXEY[2] = y1;
		valoresXEY[3] = y2;
		
		return valoresXEY;
	}

}
