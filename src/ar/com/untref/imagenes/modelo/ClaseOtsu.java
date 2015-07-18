package ar.com.untref.imagenes.modelo;

import java.util.ArrayList;
import java.util.List;

public class ClaseOtsu {

	private int rojoPromedio;
	private int verdePromedio;
	private int azulPromedio;
	// Within-variance
	private double varianza;
	private List<Pixel> pixeles;

	public ClaseOtsu() {

		super();
		this.pixeles = new ArrayList<Pixel>();
	}

	public void setRojoPromedio(int rojoPromedio) {
		this.rojoPromedio = rojoPromedio;
	}

	public void setVerdePromedio(int verdePromedio) {
		this.verdePromedio = verdePromedio;
	}

	public void setAzulPromedio(int azulPromedio) {
		this.azulPromedio = azulPromedio;
	}

	public void setVarianza(double varianza) {
		this.varianza = varianza;
	}

	public int getRojoPromedio() {
		return rojoPromedio;
	}

	public int getVerdePromedio() {
		return verdePromedio;
	}

	public int getAzulPromedio() {
		return azulPromedio;
	}

	public double getVarianza() {
		return varianza;
	}

	public List<Pixel> getPixeles() {
		return pixeles;
	}
	
	public void agregarPixeles(List<Pixel> lista) {
		this.pixeles.addAll(lista);
	}

	public void agregarPixel(Pixel pixel) {

		this.pixeles.add(pixel);
	}

}
