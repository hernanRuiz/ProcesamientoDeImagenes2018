package ar.com.untref.imagenes.bordes;

public class DetectorDeBordesLorentz implements InterfaceDetectorDeBordes{

	private double sigma;
	
	public DetectorDeBordesLorentz(double sigma){
		this.sigma = sigma;
	}
	
	@Override
	public float gradiente(float derivada) {
		return (float) ( 1/ ( ((float) (Math.pow(Math.abs(derivada), 2) / Math.pow(sigma, 2))) + 1) );
	}
	
}
