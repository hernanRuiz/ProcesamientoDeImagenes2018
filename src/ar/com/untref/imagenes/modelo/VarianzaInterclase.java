package ar.com.untref.imagenes.modelo;

public class VarianzaInterclase {

	private double varianza;
	private ClaseOtsu claseA;
	private ClaseOtsu claseB;

	public double getVarianza() {
		return varianza;
	}

	public void setVarianza(double varianza) {
		this.varianza = varianza;
	}

	public ClaseOtsu getClaseA() {
		return claseA;
	}

	public void setClaseA(ClaseOtsu claseA) {
		this.claseA = claseA;
	}

	public ClaseOtsu getClaseB() {
		return claseB;
	}

	public void setClaseB(ClaseOtsu claseB) {
		this.claseB = claseB;
	}

}
