package ar.com.untref.imagenes.helpers;


public class FormulasHelper {

	public static int potenciarColorPorSuCuadrado(int color){
		
		int colorResultante = ((color*color) / 255);
		
		return colorResultante;
	}
	
	/**
	 * R(x,y)=[f(x,y) - C] * k] + C
	 * C=127
	 * k=factor de incremento de contraste
	 * @param color
	 * @return
	 */
	public static int aumentoContrasteConFactor(int color, int factor){
		
		float colorResultante = ((color - 127) * factor ) + 127;
		
		return (int) colorResultante;
	}
	
	/**
	 * R(x,y)=[f(x,y) - fmin]*[255/(fmax-fmin)]
	 * @param color
	 * @return
	 */
	public static int aplicarFormulaVariacionContrasteAutomatico(int color, int minimo, int maximo){
		
		float colorResultante = (color - minimo) * ( (float)255/(maximo-minimo));
		
		return (int) colorResultante;
	}
}
