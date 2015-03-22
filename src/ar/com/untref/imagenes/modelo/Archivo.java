package ar.com.untref.imagenes.modelo;

import java.io.File;

public class Archivo {

	private File archivo;
	private String nombre;
	private String extension;

	public Archivo(File archivoSeleccionado) {

		this.archivo = archivoSeleccionado;
		this.setNombreDelArchivo(archivoSeleccionado);
		this.setExtensionDelArchivo(archivoSeleccionado);
	}

	public File getFile() {
		return archivo;
	}

	public void setFile(File archivo) {
		this.archivo = archivo;
	}

	public String getNombre() {
		return nombre;
	}

	public String getExtension() {
		return extension;
	}

	private void setNombreDelArchivo(File archivo) {
		
		if ( archivo!=null) {
			
			this.nombre = archivo.getName().split("\\.")[0];
		}
	}
	
	private void setExtensionDelArchivo(File archivo) {
		
		if ( archivo!=null) {
			
			this.extension = archivo.getName().split("\\.")[1];
		}
	}

}
