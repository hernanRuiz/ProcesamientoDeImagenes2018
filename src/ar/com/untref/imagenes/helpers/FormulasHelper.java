package ar.com.untref.imagenes.helpers;


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
}
