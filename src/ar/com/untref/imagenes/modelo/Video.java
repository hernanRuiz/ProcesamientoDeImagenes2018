package ar.com.untref.imagenes.modelo;

import java.util.ArrayList;
import java.util.List;

public class Video {

	private String nombre;
	private List<String> fotogramas = new ArrayList<String>();

	public Video (String nombre, List<String> fotogramas){
		
		this.nombre = nombre;
		this.fotogramas = fotogramas;
	}
	
	public String getPrimerFotograma(){
		
		String fotograma = null;
		
		if (!fotogramas.isEmpty()){
			
			fotograma = fotogramas.get(0);
		}
		
		return fotograma;
	}
	
	public String getUltimoFotograma(){
		
		String fotograma = null;
		
		if (!fotogramas.isEmpty()){
			
			fotograma = fotogramas.get(fotogramas.size()-1);
		}
		
		return fotograma;
	}
	
	/**
	 * 
	 * @param posicion (La primera posicion es 0)
	 * @return
	 */
	public String getFotogramaPorPosicion(int posicion){
		
		String fotograma = null;
		
		if (posicion < fotogramas.size()){
			
			fotograma = fotogramas.get(posicion);
		}
		
		return fotograma;
	}

	public String getNombre() {
		return nombre;
	}

	public List<String> getFotogramas() {
		return fotogramas;
	}
}
