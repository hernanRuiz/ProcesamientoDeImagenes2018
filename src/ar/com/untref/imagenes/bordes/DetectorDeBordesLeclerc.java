package ar.com.untref.imagenes.bordes;

public class DetectorDeBordesLeclerc implements InterfaceDetectorDeBordes {

	private double sigma;
	
	public DetectorDeBordesLeclerc(double sigma){
		this.sigma = sigma;
	}

	@Override
	public int gradiente(int x) {
		return (int) Math.exp(-Math.pow(Math.abs(x), 2) / Math.pow(sigma, 2));
	}
	
}
