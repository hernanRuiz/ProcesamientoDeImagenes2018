package ar.com.untref.imagenes.enums;

public enum FormatoDeImagen {
	
	RAW (".raw", "raw"),
	JPG(".jpg", "jpg"),
	GIF(".gif", "gif"),
	BMP(".bmp", "bmp"),
	DESCONOCIDO("","");
	
	private String extension;
	private String nombre;
	
	FormatoDeImagen(String extension, String nombre){
		
		this.extension = extension;
		this.nombre = nombre;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public static FormatoDeImagen getFormato(String stringFormato){
		
		FormatoDeImagen formato = FormatoDeImagen.DESCONOCIDO;
		
		for (FormatoDeImagen formatoActual: FormatoDeImagen.values()){
			
			if (formatoActual.getNombre().equalsIgnoreCase(stringFormato)){
				
				formato = formatoActual;
			}
		}
		
		return formato;
	}
	
}
