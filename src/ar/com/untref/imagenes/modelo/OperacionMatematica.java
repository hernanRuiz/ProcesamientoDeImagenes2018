package ar.com.untref.imagenes.modelo;

public enum OperacionMatematica {

	SUMA("Suma"), RESTA("Resta"), MULTIPLICACION("Multiplicacion");

	private String descripcion;

	OperacionMatematica(String operacion) {

		this.descripcion = operacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
