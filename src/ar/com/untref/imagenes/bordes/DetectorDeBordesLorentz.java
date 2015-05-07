package ar.com.untref.imagenes.bordes;

public class DetectorDeBordesLorentz implements InterfaceDetectorDeBordes{

	private double sigma;
	
	public DetectorDeBordesLorentz(double sigma){
		this.sigma = sigma;
	}
	
	@Override
	public int gradiente(int x) {
		return (int) (1/((Math.pow(Math.abs(x), 2) / Math.pow(sigma, 2)) + 1));
	}
	
}
