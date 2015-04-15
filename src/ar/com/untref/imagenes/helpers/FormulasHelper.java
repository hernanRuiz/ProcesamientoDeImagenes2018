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
	public static int aumentoContrasteConFactor(int color, float factor){
		
		float colorResultante = ((((color/255f) - 0.5f) * factor ) + 0.5f) * 255f;
		
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
