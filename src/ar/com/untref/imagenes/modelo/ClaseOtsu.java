package ar.com.untref.imagenes.modelo;

import java.util.ArrayList;
import java.util.List;

public class ClaseOtsu {

	private String nombre;
	private int rojoPromedio;
	private int verdePromedio;
	private int azulPromedio;
	private double varianza;
	private List<Pixel> pixeles;

	public ClaseOtsu(String nombre) {

		super();
		this.pixeles = new ArrayList<Pixel>();
		this.nombre = nombre;
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
	public String toString() {
		return "ClaseOtsu " + nombre + " [rojoPromedio=" + rojoPromedio + ", verdePromedio="
				+ verdePromedio + ", azulPromedio=" + azulPromedio
				+ ", varianza=" + varianza + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
	
}
