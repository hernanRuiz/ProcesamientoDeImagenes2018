package ar.com.untref.imagenes.filtros;

import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.awt.image.Kernel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import ar.com.untref.imagenes.helpers.FormulasHelper;
import ar.com.untref.imagenes.modelo.Imagen;

public class FiltroDeLaMediana extends Filtro{
	
	public FiltroDeLaMediana(Kernel kernel) {
		super(kernel);
	}

	public Imagen aplicarFiltroDeLaMediana(Imagen imagenOriginal) {

		BufferedImage im = new BufferedImage(imagenOriginal.getBufferedImage()
				.getWidth(), imagenOriginal.getBufferedImage().getHeight(),
				imagenOriginal.getBufferedImage().getType());
		
		Imagen imagenFiltrada = new Imagen(im, imagenOriginal.getFormato(),
				imagenOriginal.getNombre());

		// Aplicamos el filtro
		filtrar(imagenOriginal.getBufferedImage(), imagenFiltrada.getBufferedImage());

		return imagenFiltrada;
	}

	private BufferedImage filtrar(BufferedImage imagenOriginal, BufferedImage imagenDestino){
		
		if (imagenOriginal == imagenDestino)
			throw new IllegalArgumentException("La imagen original y la de destino no pueden ser las mismas");

		if (imagenDestino == null)
			imagenDestino = createCompatibleDestImage(imagenOriginal, imagenOriginal.getColorModel());

		BufferedImage src1 = imagenOriginal;
		BufferedImage dst1 = imagenDestino;
		
		if (src1.getColorModel().getColorSpace().getType() != imagenDestino
				.getColorModel().getColorSpace().getType())
			dst1 = createCompatibleDestImage(imagenOriginal, imagenOriginal.getColorModel());

		filter(src1.getRaster(), dst1.getRaster());

		return imagenDestino;
	}
	
	public final WritableRaster filter(Raster imagenInicial, WritableRaster imagenDestino) {
		if (imagenInicial == imagenDestino)
			throw new IllegalArgumentException("imagen origen y destino deben ser distintas");
		if (kernel.getWidth() > imagenInicial.getWidth()
				|| kernel.getHeight() > imagenInicial.getHeight())
			throw new ImagingOpException("La máscara es muy grande");
		if (imagenDestino == null)
			imagenDestino = imagenInicial.createCompatibleWritableRaster();
		else if (imagenInicial.getNumBands() != imagenDestino.getNumBands())
			throw new ImagingOpException(
					"imagen origen y destino tienen distinto numero de bandas");

		int anchoMascara = kernel.getWidth();
		int alturaMascara = kernel.getHeight();
		int izquierda = kernel.getXOrigin();
		int derecha = Math.max(anchoMascara - izquierda - 1, 0);
		int arriba = kernel.getYOrigin();
		int abajo = Math.max(alturaMascara - arriba - 1, 0);

		//Magia de buffered image
		int[] valorMaximo = imagenInicial.getSampleModel().getSampleSize();
		for (int i = 0; i < valorMaximo.length; i++)
			valorMaximo[i] = (int) Math.pow(2, valorMaximo[i]) - 1;

		int anchoDeLaRegionAlcanzable = imagenInicial.getWidth() - izquierda - derecha;
		int altoDeLaRegionAlcanzable = imagenInicial.getHeight() - arriba - abajo;
		float[] valoresDeLaMascara = kernel.getKernelData(null);
		float[] matrizTemporal = new float[anchoMascara * alturaMascara];

		for (int x = 0; x < anchoDeLaRegionAlcanzable; x++) {
			for (int y = 0; y < altoDeLaRegionAlcanzable; y++) {

				for (int banda = 0; banda < imagenInicial.getNumBands(); banda++) {
					float[] valores = new float[kernel.getHeight()*kernel.getWidth()];
					imagenInicial.getSamples(x, y, anchoMascara, alturaMascara, banda, matrizTemporal);
					for (int i = 0; i < matrizTemporal.length; i++){
						
						valores[i] += matrizTemporal[matrizTemporal.length - i - 1] * valoresDeLaMascara[i];
					}
					
					float valorMedio = (float) FormulasHelper.obtenerValorMedio(valores);
					
					imagenDestino.setSample(x + kernel.getXOrigin(), y + kernel.getYOrigin(), banda, valorMedio);
				}
			}
		}

		return imagenDestino;
	}
	
}