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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pixeles == null) ? 0 : pixeles.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClaseOtsu other = (ClaseOtsu) obj;
		if (pixeles == null) {
			if (other.pixeles != null)
				return false;
		} else if (!pixeles.equals(other.pixeles))
			return false;
		return true;
	}
	
}
