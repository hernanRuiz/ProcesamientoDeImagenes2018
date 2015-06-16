package ar.com.untref.imagenes.modelo;

import java.util.ArrayList;
import java.util.List;

public enum Fotogramas {

	ABUELA;
	
	
	public static List<String> obtenerTodosLosFotogramas(Fotogramas fotogramas){
		
		List<String> lista = new ArrayList<>();
		
		if (ABUELA.equals(fotogramas)){
			
			for (int i = 1; i< 96; i++){
				
				lista.add("/resources/Abuela/Frame"+ i +".jpeg");
			}
		}
		
		return lista;
	}
}
