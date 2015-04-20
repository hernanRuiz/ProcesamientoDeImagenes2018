package ar.com.untref.imagenes.helpers;

import java.util.Arrays;


public class FormulasHelper {

	/**
	 * R(x,y)=[f(x,y) - C] * k] + C
	 * C=128
	 * k=factor de incremento de contraste
	 * @param color
	 * @return
	 */
	public static int aumentoContrasteConFactor(int color, float factor){
		
		float colorResultante = (((color - 128f) * factor) + 128f);
		return (int) colorResultante;
	}

	public static double obtenerValorMedio(float[] valores) {

		Arrays.sort(valores);
		double mediana;
		if (valores.length % 2 == 0)
		    mediana = ((double)valores[valores.length/2] + (double)valores[valores.length/2 - 1])/2;
		else
		    mediana = (double) valores[valores.length/2];
		
		return mediana;
	}
}
