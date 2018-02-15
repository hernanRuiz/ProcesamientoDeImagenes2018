package ar.com.untref.imagenes.bordes;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.modelo.Archivo;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.ruido.GeneradorDeRuido;

public class AlgoritmoComparativoDeDetectores {
	
	private static DecimalFormat numberFormat = new DecimalFormat("0.00");
	private static List<Point> coordenadas;
	private static List<Imagen> imagenesAProcesar = new LinkedList<Imagen>();
	private static List<Imagen> imagenesAProcesarConRuido = new LinkedList<Imagen>();
	
	/* ==================== AUXILIARES =====================*/
	private static int contadorPositivos = 0;
	private static int contadorFalsosPositivos = 0;
	private static String[] mejorCaso = new String[8];
	private static boolean flagSinRuido = true;
	/* ==================================================== */
	
	/* ==================== HARRIS =====================*/
	private static int contadorHarrisCorrectosSinRuidoTotal = 0;
	private static int contadorHarrisFalsosPositivosSinRuidoTotal = 0;
	private static int contadorHarrisCorrectosConRuidoTotal = 0;
	private static int contadorHarrisFalsosPositivosConRuidoTotal = 0;
	/* ==================================================== */
	
	/* ======================= SUSAN ========================*/
	private static int contadorSusanCorrectosSinRuidoTotal = 0;
	private static int contadorSusanFalsosPositivosSinRuidoTotal = 0;
	private static int contadorSusanCorrectosConRuidoTotal = 0;
	private static int contadorSusanFalsosPositivosConRuidoTotal = 0;
	/* ==================================================== */
	
	/* ==================== MORAVEC =====================*/
	private static String[] tamanios = {"3", "5", "7"};
	private static String[] umbrales = {"10", "20", "50"};
	private static String[] umbralesParaRuido = {"200", "300", "400"};
	private static int contadorMoravecCorrectosSinRuidoTotalA = 0;
	private static int contadorMoravecFalsosPositivosSinRuidoTotalA = 0;
	private static int contadorMoravecCorrectosSinRuidoTotalB = 0;
	private static int contadorMoravecFalsosPositivosSinRuidoTotalB = 0;
	private static int contadorMoravecCorrectosSinRuidoTotalC = 0;
	private static int contadorMoravecFalsosPositivosSinRuidoTotalC = 0;
	private static int contadorMoravecCorrectosConRuidoTotalA = 0;
	private static int contadorMoravecFalsosPositivosConRuidoTotalA = 0;
	private static int contadorMoravecCorrectosConRuidoTotalB = 0;
	private static int contadorMoravecFalsosPositivosConRuidoTotalB = 0;
	private static int contadorMoravecCorrectosConRuidoTotalC = 0;
	private static int contadorMoravecFalsosPositivosConRuidoTotalC = 0;
	/* ==================================================*/
	
	/* ==================== DoG =====================*/
	private static Integer[] sigmas1 = {1, 3, 5, 7};
	private static Integer[] sigmas2 = {2, 4, 6, 8};
	private static Integer[] sigmas3 = {1, 2, 3, 4};
	private static List<Integer[]> sigmas = new LinkedList<Integer[]>();
	private static int contadorDoGCorrectosSinRuidoTotalA = 0;
	private static int contadorDoGFalsosPositivosSinRuidoTotalA = 0;
	private static int contadorDoGCorrectosSinRuidoTotalB = 0;
	private static int contadorDoGFalsosPositivosSinRuidoTotalB = 0;
	private static int contadorDoGCorrectosSinRuidoTotalC = 0;
	private static int contadorDoGFalsosPositivosSinRuidoTotalC = 0;
	private static int contadorDoGCorrectosConRuidoTotalA = 0;
	private static int contadorDoGFalsosPositivosConRuidoTotalA = 0;
	private static int contadorDoGCorrectosConRuidoTotalB = 0;
	private static int contadorDoGFalsosPositivosConRuidoTotalB = 0;
	private static int contadorDoGCorrectosConRuidoTotalC = 0;
	private static int contadorDoGFalsosPositivosConRuidoTotalC = 0;
	/* ==============================================*/
	
	private static String linea = "====================================================================================";
	private static HashMap<Double, String> tiemposSinRuido = new HashMap<Double, String>();
	private static HashMap<Double, String> tiemposConRuido = new HashMap<Double, String>();
	private static File fileActual = null;
	
	/**TODO:
	 * SUSAN: evaluar uso de dialog en ejecución individual (bordes y sierras están 
	 * comentados)
	 * Limpieza general de código
	 * Ventana Ruido: eliminar mu y pasarlo siempre como 0. Eliminar ruido blanco.
	 **/
	/**
	 * 
	 * @param imagenOriginal
	 * @param coleccion (porcentaje = diferencia entre figura y fondo)
	 * @param sigma para ruido gaussiano
	 * @throws IOException
	 */
	public static void aplicarAlgoritmo(Imagen imagenOriginal, String coleccion, int sigma) throws IOException{
		
		double tiempoTotalInicio = System.nanoTime();
		String path = "resources/" + coleccion.substring(0, coleccion.indexOf('%'));
		
		/* ======== Esquinas Rectas ==================*/
		String pathEsquinasRectas = path + "/esquinaRecta.png";
		Imagen imagenEsquinasRectas = obtenerImagen(pathEsquinasRectas);
		/* ===========================================*/
		
		/* ======== Esquinas Obtusas ==================*/
		String pathEsquinasObtusas = path + "/esquinaObtusa.png";
		Imagen imagenEsquinasObtusas = obtenerImagen(pathEsquinasObtusas);
		/* ===========================================*/
		
		/* ============== Esquina T ==================*/
		String pathEsquinasT = path + "/esquinaT.png";
		Imagen imagenEsquinasT = obtenerImagen(pathEsquinasT);
		/* ===========================================*/
		
		/* ============== Esquinas X ==================*/
		String pathEsquinasX = path + "/esquinaX.png";
		Imagen imagenEsquinasX = obtenerImagen(pathEsquinasX);
		/* ===========================================*/
		
		/* ======== Esquinas Flecha ==================*/
		String pathEsquinasFlecha = path + "/esquinaFlecha.png";
		Imagen imagenEsquinasFlecha = obtenerImagen(pathEsquinasFlecha);
		/* ===========================================*/
		
		imagenesAProcesar.add(imagenEsquinasRectas);
		imagenesAProcesar.add(imagenEsquinasObtusas);
		imagenesAProcesar.add(imagenEsquinasX);
		imagenesAProcesar.add(imagenEsquinasT);
		imagenesAProcesar.add(imagenEsquinasFlecha);
		
		
		PrintStream fileStreamAlgoritmoComparativo = new PrintStream("Salida_algoritmo_comparativo.txt");
		fileStreamAlgoritmoComparativo.flush();
		System.setOut(fileStreamAlgoritmoComparativo);
		
		System.out.println("+-------------------------------------------------+");
		System.out.println("| ALGORITMO COMPARATIVO DE DETECTORES DE ESQUINAS |");
		System.out.println("+-------------------------------------------------+");
		System.out.println();
		
		
		//------------------------------ HARRIS sin Ruido ------------------------------
		double tiempoInicioHarrisSinRuido = System.nanoTime();
		System.out.println("Resultado Detector de Harris: \n\n");
		System.out.println();
		
		int[] contadoresCorrectosHarrisSinRuido = new int[5];
		int[] contadoresFalsosHarrisSinRuido = new int[5];
		
		List<Integer> resultadosHarrisSinRuido = new LinkedList<Integer>();
		
		for (int i = 0; i < imagenesAProcesar.size(); i++){
			resultadosHarrisSinRuido = aplicarDetectorHarrisSusan(imagenesAProcesar.get(i), i, "Harris");
			
			contadoresCorrectosHarrisSinRuido[i] = resultadosHarrisSinRuido.get(0);
			contadoresFalsosHarrisSinRuido[i] = resultadosHarrisSinRuido.get(1);
			
			setContadorHarrisCorrectosSinRuidoTotal(resultadosHarrisSinRuido.get(0));
			setContadorHarrisFalsosPositivosSinRuidoTotal(resultadosHarrisSinRuido.get(1));
		}
		
		mostrarResultados(contadoresCorrectosHarrisSinRuido, contadoresFalsosHarrisSinRuido);
		
		//Calculamos tiempo de procesamiento
		double tiempoFinHarrisSinRuido = System.nanoTime();
		double tiempoProcesamientoHarrisSinRuido = (tiempoFinHarrisSinRuido - tiempoInicioHarrisSinRuido) / 1000000000;
		System.out.println();
		if(tiempoProcesamientoHarrisSinRuido < 0){tiempoProcesamientoHarrisSinRuido = Double.valueOf("0" + tiempoProcesamientoHarrisSinRuido);}; 
		System.out.println("Tiempo de procesamiento: " 
    			+ numberFormat.format(tiempoProcesamientoHarrisSinRuido) + " segundos");
		tiemposSinRuido.put(tiempoProcesamientoHarrisSinRuido, "Harris sin ruido");
		System.out.println();
		System.out.print(linea);
		System.out.println();
		System.out.println();
		//-----------------------------------------------------------------------------
		
		//------------------------------ SUSAN sin Ruido ------------------------------
		double tiempoInicioSusanSinRuido = System.nanoTime();
		System.out.println("Resultado Detector de Susan: \n\n");
		System.out.println();
		int[] contadoresCorrectosSusanSinRuido = new int[5];
		int[] contadoresFalsosSusanSinRuido = new int[5];
		
		List<Integer> resultadosSusanSinRuido = new LinkedList<Integer>();
		
		for (int i = 0; i < imagenesAProcesar.size(); i++){
			resultadosSusanSinRuido = aplicarDetectorHarrisSusan(imagenesAProcesar.get(i), i, "Susan");
		
			contadoresCorrectosSusanSinRuido[i] = resultadosSusanSinRuido.get(0);
			contadoresFalsosSusanSinRuido[i] = resultadosSusanSinRuido.get(1);
			
			setContadorSusanCorrectosSinRuidoTotal(resultadosSusanSinRuido.get(0));
			setContadorSusanFalsosPositivosSinRuidoTotal(resultadosSusanSinRuido.get(1));
		}
		
		mostrarResultados(contadoresCorrectosSusanSinRuido, contadoresFalsosSusanSinRuido);
		
		//Calculamos tiempo de procesamiento
		double tiempoFinSusanSinRuido = System.nanoTime();
		double tiempoProcesamientoSusanSinRuido = (tiempoFinSusanSinRuido - tiempoInicioSusanSinRuido) / 1000000000;
		System.out.println();
		if(tiempoProcesamientoSusanSinRuido < 0){tiempoProcesamientoSusanSinRuido = Double.valueOf("0" + tiempoProcesamientoSusanSinRuido);};
		System.out.println("Tiempo de procesamiento: " 
    			+ numberFormat.format(tiempoProcesamientoSusanSinRuido) + " segundos");
		tiemposSinRuido.put(tiempoProcesamientoSusanSinRuido, "Susan sin ruido");
		//------------------------------------------------------------------------------
		
		
		//---------------------------------MORAVEC SIN RUIDO----------------------------
		List<Integer> resultadosMoravecSinRuido = new LinkedList<Integer>();
		
		double tiempoInicioMoravecASinRuido = 0.0;
		double tiempoInicioMoravecBSinRuido = 0.0;
		double tiempoInicioMoravecCSinRuido = 0.0;
		
		for(int h = 0; h < umbrales.length; h++){
			
			System.out.println();
			
			switch (h) {
			case 0:
				System.out.print(linea);
				System.out.println();
				tiempoInicioMoravecASinRuido = System.nanoTime();
				break;
			case 1:
				tiempoInicioMoravecBSinRuido = System.nanoTime();
				break;
			default:
				tiempoInicioMoravecCSinRuido = System.nanoTime();
				break;
			}
			
			int[] contadoresCorrectos = new int[5];
			int[] contadoresFalsos = new int[5];
			
			System.out.println();
			System.out.println("Resultado Detector de Moravec (tamaño máscara: " + tamanios[h] + ", umbral: "
					+ umbrales[h] + "): \n\n");
			System.out.println();
			
			for(int f = 0; f < imagenesAProcesar.size(); f++){
			
				Imagen imagenAProcesar = imagenesAProcesar.get(f);
			
				resultadosMoravecSinRuido = aplicarMoravec(imagenAProcesar, f, Integer.valueOf(tamanios[h]), 
						Integer.valueOf(umbrales[h]));
				
				contadoresCorrectos[f] = resultadosMoravecSinRuido.get(0);
				contadoresFalsos[f] = resultadosMoravecSinRuido.get(1);
				
				switch (h) {
				case 0:
					setContadorMoravecCorrectosSinRuidoTotalA(resultadosMoravecSinRuido.get(0));
					setContadorMoravecFalsosPositivosSinRuidoTotalA(resultadosMoravecSinRuido.get(1));
					break;
				case 1:
					setContadorMoravecCorrectosSinRuidoTotalB(resultadosMoravecSinRuido.get(0));
					setContadorMoravecFalsosPositivosSinRuidoTotalB(resultadosMoravecSinRuido.get(1));
					break;
				default:
					setContadorMoravecCorrectosSinRuidoTotalC(resultadosMoravecSinRuido.get(0));
					setContadorMoravecFalsosPositivosSinRuidoTotalC(resultadosMoravecSinRuido.get(1));
					break;
				}
			}
			
			mostrarResultados(contadoresCorrectos, contadoresFalsos);
		
			//Calculamos tiempo de procesamiento
			switch (h) {
			case 0:
				double tiempoFinMoravecASinRuido = System.nanoTime();
				double tiempoProcesamientoMoravecASinRuido = (tiempoFinMoravecASinRuido - tiempoInicioMoravecASinRuido) / 1000000000;
				System.out.println();
				if(tiempoProcesamientoMoravecASinRuido < 0){tiempoProcesamientoMoravecASinRuido = Double.valueOf("0" + tiempoProcesamientoMoravecASinRuido);};
				System.out.println("Tiempo de procesamiento: " 
		    			+ numberFormat.format(tiempoProcesamientoMoravecASinRuido) + " segundos");
				tiemposSinRuido.put(tiempoProcesamientoMoravecASinRuido, "Moravec sin ruido caso A");
				break;
			case 1:
				double tiempoFinMoravecBSinRuido = System.nanoTime();
				double tiempoProcesamientoMoravecBSinRuido = (tiempoFinMoravecBSinRuido - tiempoInicioMoravecBSinRuido) / 1000000000;
				System.out.println();
				if(tiempoProcesamientoMoravecBSinRuido < 0){tiempoProcesamientoMoravecBSinRuido = Double.valueOf("0" + tiempoProcesamientoMoravecBSinRuido);};
				System.out.println("Tiempo de procesamiento: " 
		    			+ numberFormat.format(tiempoProcesamientoMoravecBSinRuido) + " segundos");
				tiemposSinRuido.put(tiempoProcesamientoMoravecBSinRuido, "Moravec sin ruido caso B");
				break;
			default:
				double tiempoFinMoravecCSinRuido = System.nanoTime();
				double tiempoProcesamientoMoravecCSinRuido = (tiempoFinMoravecCSinRuido - tiempoInicioMoravecCSinRuido) / 1000000000;
				System.out.println();
				if(tiempoProcesamientoMoravecCSinRuido < 0){tiempoProcesamientoMoravecCSinRuido = Double.valueOf("0" + tiempoProcesamientoMoravecCSinRuido);};
				System.out.println("Tiempo de procesamiento: " 
		    			+ numberFormat.format(tiempoProcesamientoMoravecCSinRuido) + " segundos");
				tiemposSinRuido.put(tiempoProcesamientoMoravecCSinRuido, "Moravec sin ruido caso C");
				break;
			}
			
		}
		//------------------------------------------------------------------------------
		
		//Sigmas para DoG
		sigmas.add(sigmas1);
		sigmas.add(sigmas2);
		sigmas.add(sigmas3);
		
		//---------------------------------DoG SIN RUIDO----------------------------
		List<Integer> resultadosDoGSinRuido = new LinkedList<Integer>();
		
		double tiempoInicioDoGASinRuido = 0.0;
		double tiempoInicioDoGBSinRuido = 0.0;
		double tiempoInicioDoGCSinRuido = 0.0;
		
		for(int i = 0; i < sigmas.size(); i++){
			
			System.out.println();
			
			switch (i) {
			case 0:
				System.out.print(linea);
				System.out.println();
				tiempoInicioDoGASinRuido = System.nanoTime();
				break;
			case 1:
				tiempoInicioDoGBSinRuido = System.nanoTime();
				break;
			default:
				tiempoInicioDoGCSinRuido = System.nanoTime();
				break;
			}
			
			
			Integer[] sigmasAUtilizar = sigmas.get(i);
			int[] contadoresCorrectos = new int[5];
			int[] contadoresFalsos = new int[5];
			
			System.out.println();
			System.out.println("Resultado DoG con sigmas: " + sigmasAUtilizar[0] + ", " +
			 		sigmasAUtilizar[1] + ", " + sigmasAUtilizar[2] + " y " + sigmasAUtilizar[3]
					+ ": \n\n");
			System.out.println();
			
			for(int f = 0; f < imagenesAProcesar.size(); f++){
			
				Imagen imagenAProcesar = imagenesAProcesar.get(f);
			
				resultadosDoGSinRuido = aplicarDoG(imagenAProcesar, f, sigmasAUtilizar[0], sigmasAUtilizar[1], sigmasAUtilizar[2], sigmasAUtilizar[3]);
				
				contadoresCorrectos[f] = resultadosDoGSinRuido.get(0);
				contadoresFalsos[f] = resultadosDoGSinRuido.get(1);
			
				switch (i) {
				case 0:
					setContadorDoGCorrectosSinRuidoTotalA(resultadosDoGSinRuido.get(0));
					setContadorDoGFalsosPositivosSinRuidoTotalA(resultadosDoGSinRuido.get(1));
					break;
				case 1:
					setContadorDoGCorrectosSinRuidoTotalB(resultadosDoGSinRuido.get(0));
					setContadorDoGFalsosPositivosSinRuidoTotalB(resultadosDoGSinRuido.get(1));
					break;
				default:
					setContadorDoGCorrectosSinRuidoTotalC(resultadosDoGSinRuido.get(0));
					setContadorDoGFalsosPositivosSinRuidoTotalC(resultadosDoGSinRuido.get(1));
					break;
				}
				
			}
			
			mostrarResultados(contadoresCorrectos, contadoresFalsos);
		
			//Calculamos tiempo de procesamiento
			switch (i) {
			case 0:
				double tiempoFinDoGASinRuido = System.nanoTime();
				double tiempoProcesamientoDoGASinRuido = (tiempoFinDoGASinRuido - tiempoInicioDoGASinRuido) / 1000000000;
				System.out.println();
				if(tiempoProcesamientoDoGASinRuido < 0){tiempoProcesamientoDoGASinRuido = Double.valueOf("0" + tiempoProcesamientoDoGASinRuido);};
				System.out.println("Tiempo de procesamiento: " 
		    			+ numberFormat.format(tiempoProcesamientoDoGASinRuido) + " segundos");
				tiemposSinRuido.put(tiempoProcesamientoDoGASinRuido, "Moravec sin ruido caso A");
				break;
			case 1:
				double tiempoFinDoGBSinRuido = System.nanoTime();
				double tiempoProcesamientoDoGBSinRuido = (tiempoFinDoGBSinRuido - tiempoInicioDoGBSinRuido) / 1000000000;
				System.out.println();
				if(tiempoProcesamientoDoGBSinRuido < 0){tiempoProcesamientoDoGBSinRuido = Double.valueOf("0" + tiempoProcesamientoDoGBSinRuido);};
				System.out.println("Tiempo de procesamiento: " 
		    			+ numberFormat.format(tiempoProcesamientoDoGBSinRuido) + " segundos");
				tiemposSinRuido.put(tiempoProcesamientoDoGBSinRuido, "Moravec sin ruido caso B");
				break;
			default:
				double tiempoFinDoGCSinRuido = System.nanoTime();
				double tiempoProcesamientoDoGCSinRuido = (tiempoFinDoGCSinRuido - tiempoInicioDoGCSinRuido) / 1000000000;
				System.out.println();
				if(tiempoProcesamientoDoGCSinRuido < 0){tiempoProcesamientoDoGCSinRuido = Double.valueOf("0" + tiempoProcesamientoDoGCSinRuido);};
				System.out.println("Tiempo de procesamiento: " 
		    			+ numberFormat.format(tiempoProcesamientoDoGCSinRuido) + " segundos");
				tiemposSinRuido.put(tiempoProcesamientoDoGCSinRuido, "Moravec sin ruido caso C");
				break;
			}
		
		}
		//--------------------------------------------------------------------------------
		
		//Entre los tiempo de procesamiento de los detectores, tomamos el menor 
		System.out.println();
		System.out.print(linea);
		System.out.println();
		System.out.println();
		System.out.println("\nResultados finales sobre imágenes sin ruido \n");
		System.out.println();
		flagSinRuido = true;
		mostrarResultadosFinales(flagSinRuido);
		
		Map.Entry<Double, String> min = null;
		
		for (Map.Entry<Double, String> entry : tiemposSinRuido.entrySet())
		{
		    if (min == null || min.getKey() > entry.getKey())
		    {
		        min = entry;
		    }
		}
		
		System.out.println("El detector de: " + min.getValue() + " es el que menor tiempo de procesamiento "
				+ "implica sonbre imágenes sin ruido (" + numberFormat.format(min.getKey()) + " segundos).");
		
		
		/* =================== Imagenes con Ruido =================== */
		if (sigma > 0){
			
			Imagen imagenEsquinasRectasConRuido = generarImagenConRuido(imagenEsquinasRectas, sigma);
			Imagen imagenEsquinasObtusasConRuido = generarImagenConRuido(imagenEsquinasObtusas, sigma);
			Imagen imagenEsquinasTConRuido = generarImagenConRuido(imagenEsquinasT, sigma);
			Imagen imagenEsquinasXConRuido = generarImagenConRuido(imagenEsquinasX, sigma);
			Imagen imagenEsquinasFlechaConRuido = generarImagenConRuido(imagenEsquinasFlecha, sigma);
		
			imagenesAProcesarConRuido.add(imagenEsquinasRectasConRuido);
			imagenesAProcesarConRuido.add(imagenEsquinasObtusasConRuido);
			imagenesAProcesarConRuido.add(imagenEsquinasXConRuido);
			imagenesAProcesarConRuido.add(imagenEsquinasTConRuido);
			imagenesAProcesarConRuido.add(imagenEsquinasFlechaConRuido);

			//---------------------------------HARRIS CON RUIDO----------------------------
			double tiempoInicioHarrisConRuido = System.nanoTime();
			System.out.println();
			System.out.println();
			System.out.println(linea);
			System.out.println();
			System.out.println("Resultado Detector de Harris con Ruido (sigma = " + sigma + "): \n\n");
			System.out.println();
			
			int[] contadoresCorrectosHarrisConRuido = new int[5];
			int[] contadoresFalsosHarrisConRuido = new int[5];
			
			List<Integer> resultadosHarrisConRuido = new LinkedList<Integer>();
			
			for (int i = 0; i < imagenesAProcesarConRuido.size(); i++){	
				resultadosHarrisConRuido = aplicarDetectorHarrisSusan(imagenesAProcesarConRuido.get(i), i, "Harris");
				
				contadoresCorrectosHarrisConRuido[i] = resultadosHarrisConRuido.get(0);
				contadoresFalsosHarrisConRuido[i] = resultadosHarrisConRuido.get(1);
			
				setContadorHarrisCorrectosConRuidoTotal(resultadosHarrisConRuido.get(0));
				setContadorHarrisFalsosPositivosConRuidoTotal(resultadosHarrisConRuido.get(1));
			
			}
			
			mostrarResultados(contadoresCorrectosHarrisConRuido, contadoresFalsosHarrisConRuido);
			
			//Calculamos tiempo de procesamiento
			System.out.println();
			double tiempoFinHarrisConRuido = System.nanoTime();
			double tiempoProcesamientoHarrisConRuido = (tiempoFinHarrisConRuido - tiempoInicioHarrisConRuido) / 1000000000;
			System.out.println();
			if(tiempoProcesamientoHarrisConRuido < 0){tiempoProcesamientoHarrisConRuido = Double.valueOf("0" + tiempoProcesamientoHarrisConRuido);};
			System.out.println("Tiempo de procesamiento: " 
	    			+ numberFormat.format(tiempoProcesamientoHarrisConRuido) + " segundos");
			tiemposConRuido.put(tiempoProcesamientoHarrisConRuido, "Harris con ruido");
			System.out.println();
			System.out.print(linea);
			System.out.println();
			System.out.println();
			//------------------------------------------------------------------------------
			
			
			//---------------------------------SUSAN CON RUIDO------------------------------
			double tiempoInicioSusanConRuido = System.nanoTime();
			System.out.println("Resultado Detector de Susan con Ruido (sigma = " + sigma + "): \n\n");
			System.out.println();
			
			int[] contadoresCorrectosSusanConRuido = new int[5];
			int[] contadoresFalsosSusanConRuido = new int[5];
			
			List<Integer> resultadosSusanConRuido = new LinkedList<Integer>();

			for (int i = 0; i < imagenesAProcesarConRuido.size(); i++){
				resultadosSusanConRuido = aplicarDetectorHarrisSusan(imagenesAProcesarConRuido.get(i), i, "Susan");
			
				contadoresCorrectosSusanConRuido[i] = resultadosSusanConRuido.get(0);
				contadoresFalsosSusanConRuido[i] = resultadosSusanConRuido.get(1);
			
				setContadorSusanCorrectosConRuidoTotal(resultadosSusanConRuido.get(0));
				setContadorSusanFalsosPositivosConRuidoTotal(resultadosSusanConRuido.get(1));
			}
			
			mostrarResultados(contadoresCorrectosSusanConRuido, contadoresFalsosSusanConRuido);
			
			//Calculamos tiempo de procesamiento
			double tiempoFinSusanConRuido = System.nanoTime();
			double tiempoProcesamientoSusanConRuido = (tiempoFinSusanConRuido - tiempoInicioSusanConRuido) / 1000000000;
			System.out.println();
			if(tiempoProcesamientoSusanConRuido < 0){tiempoProcesamientoSusanConRuido = Double.valueOf("0" + tiempoProcesamientoSusanConRuido);};
			System.out.println("Tiempo de procesamiento: " 
	    			+ numberFormat.format(tiempoProcesamientoSusanConRuido) + " segundos");
			tiemposConRuido.put(tiempoProcesamientoSusanConRuido, "Susan con ruido");
			//------------------------------------------------------------------------------
			
			//---------------------------------MORAVEC CON RUIDO----------------------------
			if(sigma <= 3){
									
				List<Integer> resultadosMoravecConRuido = new LinkedList<Integer>();					
				
				double tiempoInicioMoravecAConRuido = 0.0;
				double tiempoInicioMoravecBConRuido = 0.0;
				double tiempoInicioMoravecCConRuido = 0.0;
				
				for(int h = 0; h < umbralesParaRuido.length; h++){
				
					int[] contadoresCorrectos = new int[5];
					int[] contadoresFalsos = new int[5];
					
					System.out.println();
					
					switch (h) {
					case 0:
						System.out.print(linea);
						System.out.println();
						System.out.println();
						tiempoInicioMoravecAConRuido = System.nanoTime();
						break;
					case 1:
						tiempoInicioMoravecBConRuido = System.nanoTime();
						break;
					default:
						tiempoInicioMoravecCConRuido = System.nanoTime();
						break;
					}

					System.out.println("Resultado Detector de Moravec (tamaño máscara: " + tamanios[0] + ", umbral: "
							+ umbralesParaRuido[h] + ", ruido con sigma = " + sigma + "): \n\n");
					System.out.println();
					
					for(int f = 0; f < imagenesAProcesarConRuido.size(); f++){
					
						Imagen imagenAProcesar = imagenesAProcesarConRuido.get(f);
					
						resultadosMoravecConRuido = aplicarMoravec(imagenAProcesar, f, Integer.valueOf(tamanios[0]), 
								Integer.valueOf(umbralesParaRuido[h]));
						
						contadoresCorrectos[f] = resultadosMoravecConRuido.get(0);
						contadoresFalsos[f] = resultadosMoravecConRuido.get(1);
					
						switch (h) {
						case 0:
							setContadorMoravecCorrectosConRuidoTotalA(resultadosMoravecConRuido.get(0));
							setContadorMoravecFalsosPositivosConRuidoTotalA(resultadosMoravecConRuido.get(1));
							break;
						case 1:
							setContadorMoravecCorrectosConRuidoTotalB(resultadosMoravecConRuido.get(0));
							setContadorMoravecFalsosPositivosConRuidoTotalB(resultadosMoravecConRuido.get(1));
							break;
						default:
							setContadorMoravecCorrectosConRuidoTotalC(resultadosMoravecConRuido.get(0));
							setContadorMoravecFalsosPositivosConRuidoTotalC(resultadosMoravecConRuido.get(1));
							break;
						}
					}
					
					mostrarResultados(contadoresCorrectos, contadoresFalsos);
					
					//Calculamos tiempo de procesamiento
					switch (h) {
					case 0:
						double tiempoFinMoravecAConRuido = System.nanoTime();
						double tiempoProcesamientoMoravecAConRuido = (tiempoFinMoravecAConRuido - tiempoInicioMoravecAConRuido) / 1000000000;
						System.out.println();
						if(tiempoProcesamientoMoravecAConRuido < 0){tiempoProcesamientoMoravecAConRuido = Double.valueOf("0" + tiempoProcesamientoMoravecAConRuido);};
						System.out.println("Tiempo de procesamiento: " 
				    			+ numberFormat.format(tiempoProcesamientoMoravecAConRuido) + " segundos");
						tiemposConRuido.put(tiempoProcesamientoMoravecAConRuido, "Moravec con ruido caso A");
						break;
					case 1:
						double tiempoFinMoravecBConRuido = System.nanoTime();
						double tiempoProcesamientoMoravecBConRuido = (tiempoFinMoravecBConRuido - tiempoInicioMoravecBConRuido) / 1000000000;
						System.out.println();
						if(tiempoProcesamientoMoravecBConRuido < 0){tiempoProcesamientoMoravecBConRuido = Double.valueOf("0" + tiempoProcesamientoMoravecBConRuido);};
						System.out.println("Tiempo de procesamiento: " 
				    			+ numberFormat.format(tiempoProcesamientoMoravecBConRuido) + " segundos");
						tiemposConRuido.put(tiempoProcesamientoMoravecBConRuido, "Moravec con ruido caso B");
						break;
					default:
						double tiempoFinMoravecCConRuido = System.nanoTime();
						double tiempoProcesamientoMoravecCConRuido = (tiempoFinMoravecCConRuido - tiempoInicioMoravecCConRuido) / 1000000000;
						System.out.println();
						if(tiempoProcesamientoMoravecCConRuido < 0){tiempoProcesamientoMoravecCConRuido = Double.valueOf("0" + tiempoProcesamientoMoravecCConRuido);};
						System.out.println("Tiempo de procesamiento: " 
				    			+ numberFormat.format(tiempoProcesamientoMoravecCConRuido) + " segundos");
						tiemposConRuido.put(tiempoProcesamientoMoravecCConRuido, "Moravec con ruido caso C");
						break;
					}
				}
			} else {
				System.out.println("Resultado no viable para el estudio comparativo \n\n");
			}
			//------------------------------------------------------------------------------
			
			//---------------------------------DoG CON RUIDO--------------------------------
			double tiempoInicioDoGAConRuido = 0.0;
			double tiempoInicioDoGBConRuido = 0.0;
			double tiempoInicioDoGCConRuido = 0.0;
			
			for(int i = 0; i < sigmas.size(); i++){
				
				Integer[] sigmasAUtilizar = sigmas.get(i);
				
				int[] contadoresCorrectos = new int[5];
				int[] contadoresFalsos = new int[5];
				
				List<Integer> resultadosDoGConRuido = new LinkedList<Integer>();
				
				System.out.println();
				
				switch (i) {
				case 0:
					System.out.print(linea);
					System.out.println();
					System.out.println();
					tiempoInicioDoGAConRuido = System.nanoTime();
					break;
				case 1:
					tiempoInicioDoGBConRuido = System.nanoTime();
					break;
				default:
					tiempoInicioDoGCConRuido = System.nanoTime();
					break;
				}
				
				System.out.println("Resultado DoG con sigmas: " + sigmasAUtilizar[0] + ", " +
				 		sigmasAUtilizar[1] + ", " + sigmasAUtilizar[2] + " y " + sigmasAUtilizar[3] + ", "
						+ "con ruido (sigma = " + sigma + "): \n\n");
				System.out.println();
				
				for (int j = 0; j < imagenesAProcesarConRuido.size(); j++){
					
					resultadosDoGConRuido = aplicarDoG(imagenesAProcesarConRuido.get(j), j, sigmasAUtilizar[0],
							sigmasAUtilizar[1], sigmasAUtilizar[2], sigmasAUtilizar[3]);

					contadoresCorrectos[j] = resultadosDoGConRuido.get(0);
					contadoresFalsos[j] = resultadosDoGConRuido.get(1);
				
					switch (i) {
					case 0:
						setContadorDoGCorrectosConRuidoTotalA(resultadosDoGConRuido.get(0));
						setContadorDoGFalsosPositivosConRuidoTotalA(resultadosDoGConRuido.get(1));
						break;
					case 1:
						setContadorDoGCorrectosConRuidoTotalB(resultadosDoGConRuido.get(0));
						setContadorDoGFalsosPositivosConRuidoTotalB(resultadosDoGConRuido.get(1));
						break;
					default:
						setContadorDoGCorrectosConRuidoTotalC(resultadosDoGConRuido.get(0));
						setContadorDoGFalsosPositivosConRuidoTotalC(resultadosDoGConRuido.get(1));
						break;
					}
				}
								
				mostrarResultados(contadoresCorrectos, contadoresFalsos);
			
				//Calculamos tiempo de procesamiento
				switch (i) {
				case 0:
					double tiempoFinDoGAConRuido = System.nanoTime();
					double tiempoProcesamientoDoGAConRuido = (tiempoFinDoGAConRuido - tiempoInicioDoGAConRuido) / 1000000000;
					System.out.println();
					if(tiempoProcesamientoDoGAConRuido < 0){tiempoProcesamientoDoGAConRuido = Double.valueOf("0" + tiempoProcesamientoDoGAConRuido);};
					System.out.println("Tiempo de procesamiento: " 
			    			+ numberFormat.format(tiempoProcesamientoDoGAConRuido) + " segundos");
					tiemposConRuido.put(tiempoProcesamientoDoGAConRuido, "DoG con ruido caso A");
					break;
				case 1:
					double tiempoFinDoGBConRuido = System.nanoTime();
					double tiempoProcesamientoDoGBConRuido = (tiempoFinDoGBConRuido - tiempoInicioDoGBConRuido) / 1000000000;
					System.out.println();
					if(tiempoProcesamientoDoGBConRuido < 0){tiempoProcesamientoDoGBConRuido = Double.valueOf("0" + tiempoProcesamientoDoGBConRuido);};
					System.out.println("Tiempo de procesamiento: " 
			    			+ numberFormat.format(tiempoProcesamientoDoGBConRuido) + " segundos");
					tiemposConRuido.put(tiempoProcesamientoDoGBConRuido, "DoG con ruido caso B");
					break;
				default:
					double tiempoFinDoGCConRuido = System.nanoTime();
					double tiempoProcesamientoDoGCConRuido = (tiempoFinDoGCConRuido - tiempoInicioDoGCConRuido) / 1000000000;
					System.out.println();
					if(tiempoProcesamientoDoGCConRuido < 0){tiempoProcesamientoDoGCConRuido = Double.valueOf("0" + tiempoProcesamientoDoGCConRuido);};
					System.out.println("Tiempo de procesamiento: " 
			    			+ numberFormat.format(tiempoProcesamientoDoGCConRuido) + " segundos");
					tiemposConRuido.put(tiempoProcesamientoDoGCConRuido, "DoG con ruido caso C");
					break;
				}
				
			}
			//------------------------------------------------------------------------------
		
			
			//Entre los tiempo de procesamiento de los detectores, tomamos el menor
			System.out.println();
			System.out.print(linea);
			System.out.println();
			System.out.println();
			System.out.println("\nResultados finales sobre imágenes con ruido (sigma = " + sigma + ")\n");
			System.out.println();
			flagSinRuido = false;
			mostrarResultadosFinales(flagSinRuido);
		
			Map.Entry<Double, String> minConRuido = null;
			
			for (Map.Entry<Double, String> entry : tiemposConRuido.entrySet())
			{
			    if (minConRuido == null || minConRuido.getKey() > entry.getKey())
			    {
			    	minConRuido = entry;
			    }
			}
			
			System.out.println("El detector de: " + minConRuido.getValue() + " es el que menor tiempo de "
					+ "procesamiento implica sobre imágenes con ruido (" 
					+ numberFormat.format(minConRuido.getKey()) + " segundos).");
			System.out.println();
		} else {
			System.out.println();
		}
		
		resetearVariables();
		
		//Mostramos el tiempo de procesamiento total (del algoritmo comparativo entero)
		double tiempoTotalFin = System.nanoTime();
		System.out.println("Tiempo de procesamiento total: " 
				+ numberFormat.format((tiempoTotalFin - tiempoTotalInicio) / 1000000000) + " segundos");
		
		fileStreamAlgoritmoComparativo.flush();
		fileStreamAlgoritmoComparativo.close();
		
	}
	
	
	/**
	 * Método de procesamiento similar para Harris o Susan, diferenciando cual aplicar
	   según parámetro de entrada
	 * @param Imagen original
	 * @param Índice de figura
	 * @param Detector a aplicar
	 * @return Cantidad de resultados divdidos en positivos y falsos positivos
	 */
	private static List<Integer> aplicarDetectorHarrisSusan(Imagen imagenOriginal, int indice, String detector){
		
		List <Integer> resultadosEnX = new LinkedList<Integer>();
		List <Integer> resultadosEnY = new LinkedList<Integer>();
		
		if (coordenadas != null && !coordenadas.isEmpty()){coordenadas.clear();};
		/*Definimos las coordenadas de las esquinas según la figura a analizar
		identificada cada una con un índice*/
		coordenadas = setearCoordenadas(indice);
		
		switch (detector) {
			case "Harris":				
				DetectorDeHarris.detectarEsquinas(imagenOriginal, false);
				resultadosEnX = DetectorDeHarris.getResultadosX();
				resultadosEnY = DetectorDeHarris.getResultadosY();
				DetectorDeHarris.setResultadosX(new LinkedList<Integer>());
				DetectorDeHarris.setResultadosY(new LinkedList<Integer>());
				break;
	
			case "Susan":				
				DetectorSusan.aplicar(imagenOriginal, "Esquinas", false);
				resultadosEnX = DetectorSusan.getResultadosX();
				resultadosEnY = DetectorSusan.getResultadosY();
				DetectorSusan.setResultadosX(new LinkedList<Integer>());
				DetectorSusan.setResultadosY(new LinkedList<Integer>());
				break;
			
			default:
				break;
			}
		
		List<Integer> resultados = new LinkedList<Integer>();		
		
		if(resultadosEnX.size() == 0 || resultadosEnX.size() > 50000){
			
			System.out.println("\t SIN RESULTADOS PARA LA IMAGEN ACTUAL EN LAS CONDICIONES DADAS \n\n"); 
		
		} else { 
		
			for (int i = 0; i < resultadosEnX.size(); i++){
				int x = resultadosEnX.get(i);
				int y = resultadosEnY.get(i);
				
				/*Evaluamos los puntos detectados para saber si son correctos o 
				falsos positivos*/
				int contadores[] = evaluarPuntos(x, y);
				contadorPositivos += contadores[0];
				contadorFalsosPositivos += contadores[1];
			}
			
			int positivos;
			int falsosPositivos;
			
			positivos = contadorPositivos;
			falsosPositivos = contadorFalsosPositivos;
			
			resultados.add(positivos);
			resultados.add(falsosPositivos);
			
			contadorPositivos = 0;
			contadorFalsosPositivos = 0;
			
			resultadosEnX = new LinkedList<Integer>();
			resultadosEnY = new LinkedList<Integer>();
		}
		
		return resultados;
	}
	
	/**
	 * Aplica el detector de Moravec según los parámetros y sobre la imagen que recibe
	 * @param Imagen Original 
	 * @param Índice de figura
	 * @param Tamaño de la Mascara
	 * @param Umbral
	 * @return Cantidad de resultados divdidos en positivos y falsos positivos
	 */
	private static List<Integer> aplicarMoravec(Imagen imagenOriginal, int indice, int tamanioMascara, int umbral){
		
		if (coordenadas != null && !coordenadas.isEmpty()){coordenadas.clear();};
		/*Definimos las coordenadas de las esquinas según la figura a analizar
		identificada cada una con un índice*/
		coordenadas = setearCoordenadas(indice);
		
		DetectorDeBordesMoravec.aplicarMoravec(imagenOriginal, tamanioMascara, umbral, false);
		
		List <Integer> resultadosMoravecEnX = new LinkedList<Integer>();
		List <Integer> resultadosMoravecEnY = new LinkedList<Integer>();
		
		resultadosMoravecEnX = DetectorDeBordesMoravec.getResultadosX();
		resultadosMoravecEnY = DetectorDeBordesMoravec.getResultadosY();
		
		DetectorDeBordesMoravec.setResultadosX(new LinkedList<Integer>());
		DetectorDeBordesMoravec.setResultadosY(new LinkedList<Integer>());
		
		for (int i = 0; i < resultadosMoravecEnX.size(); i++){
			int x = resultadosMoravecEnX.get(i);
			int y = resultadosMoravecEnY.get(i);
			
			/*Evaluamos los puntos detectados para saber si son correctos o 
			falsos positivos*/
			int contadores[] = evaluarPuntos(x, y);
			contadorPositivos += contadores[0];
			contadorFalsosPositivos += contadores[1];
						
		}
		
		int positivos;
		int falsosPositivos;
		
		positivos = contadorPositivos;
		falsosPositivos = contadorFalsosPositivos;
		
		List<Integer> resultados = new LinkedList<Integer>();		
		resultados.add(positivos);
		resultados.add(falsosPositivos);
		
		contadorPositivos = 0;
		contadorFalsosPositivos = 0;
		
		return resultados;			
	}
	
	
	/**
	 * Aplica el detector de Moravec según los parámetros y sobre la imagen que recibe
	 * @param Imagen Original
	 * @param Indice de figura
	 * @param Sigmas, cada uno corresponde a un filtro gaussiano a aplicar
	 * @return Cantidad de resultados divdidos en positivos y falsos positivos
	 */
	private static List<Integer> aplicarDoG(Imagen imagenOriginal, int indice, int sigma1,
			int sigma2, int sigma3, int sigma4){
		
		if (coordenadas != null && !coordenadas.isEmpty()){coordenadas.clear();};
		/*Definimos las coordenadas de las esquinas según la figura a analizar
		identificada cada una con un índice*/
		coordenadas = setearCoordenadas(indice);
		
		DoG.aplicar(imagenOriginal, sigma1, sigma2, sigma3, sigma4, false);
		
		List <Integer> resultadosDoGEnX = new LinkedList<Integer>();
		List <Integer> resultadosDoGEnY = new LinkedList<Integer>();
		
		resultadosDoGEnX = DoG.getResultadosX();
		resultadosDoGEnY = DoG.getResultadosY();
		
		DoG.setResultadosX(new LinkedList<Integer>());
		DoG.setResultadosY(new LinkedList<Integer>());
		
		for (int i = 0; i < resultadosDoGEnX.size(); i++){
			int x = resultadosDoGEnX.get(i);
			int y = resultadosDoGEnY.get(i);
			
			/*Evaluamos los puntos detectados para saber si son correctos o 
			falsos positivos*/
			int contadores[] = evaluarPuntos(x, y);
			contadorPositivos += contadores[0];
			contadorFalsosPositivos += contadores[1];
						
		}
		
		int positivos;
		int falsosPositivos;
		
		positivos = contadorPositivos;
		falsosPositivos = contadorFalsosPositivos;
		
		List<Integer> resultados = new LinkedList<Integer>();		
		resultados.add(positivos);
		resultados.add(falsosPositivos);
		
		contadorPositivos = 0;
		contadorFalsosPositivos = 0;
		
		return resultados;
		
	}
	
	
	
	/**
	 * Evalúa el punto detectado contra las esquinas y sus ocho vecinos
	 * @param x
	 * @param y
	 * @return cantidad de resultados dividios en positivos y falsos positivos
	 */
	private static int[] evaluarPuntos(int x, int y){
		
		int contadorPositivos = 0;
		int contadorFalsosPositivos = 0;
		int[] contadores = new int[2];
		
		for (Point puntoActual : coordenadas) {	
			
			/*El punto detectado es correcto si coincide exactamente con las coordenadas
			de las esquinas reales en la figura*/
			if(Integer.valueOf(puntoActual.x).equals(x) && Integer.valueOf(puntoActual.y).equals(y)){				
				contadorPositivos++;
			}
		}
			
		/*Si el punto analizado no coincide con ninguna esquina, entonces es un falso
		positivo*/
		if(contadorPositivos == 0){		contadorFalsosPositivos++;}
			
		contadores[0] = contadorPositivos;
		contadores[1] = contadorFalsosPositivos;
		
		return contadores;
	}
	
	/**
	 * @param indiceImagen
	 * @return void
	 * Setea las coordenadas de las esquinas para contrastar los
	 * los resultados de los detectores. Cada indice se corresponde con una de las
	 * imágenes cuyas esquinas conocemos de antemano
	 **/
	private static List<Point> setearCoordenadas (int indiceImagen){
		List<Point> coordenadas = new LinkedList<Point>();
		
		switch (indiceImagen) {
			case 0:
				Point coordenadaR1 = new Point(24,21);
				Point coordenadaR2 = new Point(24,208);
				Point coordenadaR3 = new Point(211,21);
				Point coordenadaR4 = new Point(211,208);
				coordenadas.add(coordenadaR1);
				coordenadas.add(coordenadaR2);
				coordenadas.add(coordenadaR3);
				coordenadas.add(coordenadaR4);
			break;
				
			case 1:
				Point coordenadaO1 = new Point(27,68);
				Point coordenadaO2 = new Point(27,160);
				Point coordenadaO3 = new Point(117,22);
				Point coordenadaO4 = new Point(117,206);
				Point coordenadaO5 = new Point(207,68);
				Point coordenadaO6 = new Point(207,160);
				coordenadas.add(coordenadaO1);
				coordenadas.add(coordenadaO2);
				coordenadas.add(coordenadaO3);
				coordenadas.add(coordenadaO4);
				coordenadas.add(coordenadaO5);
				coordenadas.add(coordenadaO6);
			break;
				
			default:
				if (indiceImagen == 2){
					Point coordenadaX1 = new Point(117,114);
					coordenadas.add(coordenadaX1);
				}
				
				if (indiceImagen == 3){
					Point coordenadaT1 = new Point(117,101);
					coordenadas.add(coordenadaT1);
				}
				
				if (indiceImagen == 4){
					Point coordenadaF1 = new Point(118,102);
					coordenadas.add(coordenadaF1);
				}
			break;
		}
		
		return coordenadas;
	}

	
	private static void mostrarResultados(int[] correctos, int[] falsosPositivos) {
		String leftAlignFormat = "| %-15s | %7s | %7s | %7s | %7s | %7s |%n";
		
		System.out.format("                       +---------+---------+---------+---------+---------+%n");
		System.out.format("                       | Esquina | Esquina | Esquina | Esquina | Esquina |%n");
		System.out.format("                       |  Recta  |  Obtusa |    X    |    T    |  Flecha |%n");
		System.out.format("+----------------------+---------+---------+---------+---------+---------+%n");
		System.out.format(leftAlignFormat, "Resultados Correctos", correctos[0], correctos[1], correctos[2], correctos[3], correctos[4]);
		System.out.format(leftAlignFormat, "Falsos Positivos    ", falsosPositivos[0], falsosPositivos[1], falsosPositivos[2], falsosPositivos[3], falsosPositivos[4]);
		System.out.format("+----------------------+---------+---------+---------+---------+---------+%n\n");
	}
	
	//Muestra los resultados finales de cada detector y marca el ganador
	private static void mostrarResultadosFinales(boolean flagSinRuido) {
		
		int contadorCorrectosHarris = 0;
		int contadorFPHarris = 0;
		int contadorCorrectosSusan = 0;
		int contadorFPSusan = 0;
		int contadorCorrectosMoravecA = 0;
		int contadorFPMoravecA = 0;
		int contadorCorrectosMoravecB = 0;
		int contadorFPMoravecB = 0;
		int contadorCorrectosMoravecC = 0;
		int contadorFPMoravecC = 0;
		int contadorCorrectosDoGA = 0;
		int contadorFPDoGA = 0;
		int contadorCorrectosDoGB = 0;
		int contadorFPDoGB = 0;
		int contadorCorrectosDoGC = 0;
		int contadorFPDoGC = 0;
		
		if(flagSinRuido){
			contadorCorrectosHarris = getContadorHarrisCorrectosSinRuidoTotal();
			contadorFPHarris = getContadorHarrisFalsosPositivosSinRuidoTotal();
			contadorCorrectosSusan = getContadorSusanCorrectosSinRuidoTotal();
			contadorFPSusan = getContadorSusanFalsosPositivosSinRuidoTotal();
			contadorCorrectosMoravecA = getContadorMoravecCorrectosSinRuidoTotalA();
			contadorFPMoravecA = getContadorMoravecFalsosPositivosSinRuidoTotalA();
			contadorCorrectosMoravecB = getContadorMoravecCorrectosSinRuidoTotalB();
			contadorFPMoravecB = getContadorMoravecFalsosPositivosSinRuidoTotalB();
			contadorCorrectosMoravecC = getContadorMoravecCorrectosSinRuidoTotalC();
			contadorFPMoravecC = getContadorMoravecFalsosPositivosSinRuidoTotalC();
			contadorCorrectosDoGA = getContadorDoGCorrectosSinRuidoTotalA();
			contadorFPDoGA = getContadorDoGFalsosPositivosSinRuidoTotalA();
			contadorCorrectosDoGB = getContadorDoGCorrectosSinRuidoTotalB();
			contadorFPDoGB = getContadorDoGFalsosPositivosSinRuidoTotalB();
			contadorCorrectosDoGC = getContadorDoGCorrectosSinRuidoTotalC();
			contadorFPDoGC = getContadorDoGFalsosPositivosSinRuidoTotalC();
		
		} else {
			contadorCorrectosHarris = getContadorHarrisCorrectosConRuidoTotal();
			contadorFPHarris = getContadorHarrisFalsosPositivosConRuidoTotal();
			contadorCorrectosSusan = getContadorSusanCorrectosConRuidoTotal();
			contadorFPSusan = getContadorSusanFalsosPositivosConRuidoTotal();
			contadorCorrectosMoravecA = getContadorMoravecCorrectosConRuidoTotalA();
			contadorFPMoravecA = getContadorMoravecFalsosPositivosConRuidoTotalA();
			contadorCorrectosMoravecB = getContadorMoravecCorrectosConRuidoTotalB();
			contadorFPMoravecB = getContadorMoravecFalsosPositivosConRuidoTotalB();
			contadorCorrectosMoravecC = getContadorMoravecCorrectosConRuidoTotalC();
			contadorFPMoravecC = getContadorMoravecFalsosPositivosConRuidoTotalC();
			contadorCorrectosDoGA = getContadorDoGCorrectosConRuidoTotalA();
			contadorFPDoGA = getContadorDoGFalsosPositivosConRuidoTotalA();
			contadorCorrectosDoGB = getContadorDoGCorrectosConRuidoTotalB();
			contadorFPDoGB = getContadorDoGFalsosPositivosConRuidoTotalB();
			contadorCorrectosDoGC = getContadorDoGCorrectosConRuidoTotalC();
			contadorFPDoGC = getContadorDoGFalsosPositivosConRuidoTotalC();
		}
		
		String mensaje = "";
		
		int indiceMejorCaso = seleccionarMejorCaso(contadorCorrectosHarris, contadorCorrectosSusan, 
				contadorCorrectosMoravecA, contadorCorrectosMoravecB, contadorCorrectosMoravecC, 
				contadorCorrectosDoGA, contadorCorrectosDoGB, contadorCorrectosDoGC,
				contadorFPHarris, contadorFPSusan, contadorFPMoravecA, contadorFPMoravecB, contadorFPMoravecC, 
				contadorFPDoGA,  contadorFPDoGB, contadorFPDoGC);
		
		
		String[] nombres = {"Harris", "Susan", "Moravec caso A", "Moravec caso B", "Moravec caso C", 
				"DoG caso A", "DoG caso B", "DoG caso C"};
		
		for (int indice = 0; indice < mejorCaso.length; indice ++){
				mejorCaso[indice] = "-";
		}
		
		//Hay un solo ganador (sea por mayor cantidad de correctos o, a igual cantidad de correctos,
		//menor cantidad de falsos positivos)
		if(indiceMejorCaso != -1){
			mejorCaso[indiceMejorCaso] = "X";
			mensaje = "El detector que muestra mejor desempeño para el caso analizado es: " + nombres[indiceMejorCaso];		
		} else {
			//Si entre los ganadores hay más de un caso con la misma cantidad de falsos positivos
			mensaje = "Ninguno de los algoritmos sobresale de forma única en su resultado sobre los demás.";
		}
		
		String leftAlignFormat = "| %-20s | %12s | %10s | %11s |%n";
		
		System.out.format("                       +--------------+------------+-------------+%n");
		System.out.format("                       |    Puntos    |   Falsos   |  Algoritmo  |%n");
		System.out.format("                       |   Correctos  |  Positivos |   Ganador   |%n");
		System.out.format("+----------------------+--------------+------------+-------------+%n");
		System.out.format(leftAlignFormat, "Harris   ", contadorCorrectosHarris, contadorFPHarris, mejorCaso[0]);
		System.out.format(leftAlignFormat, "Susan    ", contadorCorrectosSusan, contadorFPSusan, mejorCaso[1]);
		System.out.format(leftAlignFormat, "Moravec A", contadorCorrectosMoravecA, contadorFPMoravecA, mejorCaso[2]);
		System.out.format(leftAlignFormat, "Moravec B", contadorCorrectosMoravecB, contadorFPMoravecB, mejorCaso[3]);
		System.out.format(leftAlignFormat, "Moravec C", contadorCorrectosMoravecC, contadorFPMoravecC, mejorCaso[4]);
		System.out.format(leftAlignFormat, "DoG A    ", contadorCorrectosDoGA, contadorFPDoGA, mejorCaso[5]);
		System.out.format(leftAlignFormat, "DoG B    ", contadorCorrectosDoGB, contadorFPDoGB, mejorCaso[6]);
		System.out.format(leftAlignFormat, "DoG C    ", contadorCorrectosDoGC, contadorFPDoGC, mejorCaso[7]);
		System.out.format("+----------------------+--------------+------------+-------------+%n\n");
		
		System.out.println();
		System.out.print(mensaje);
		System.out.println();
		System.out.println();
	}
	
	
	public static int seleccionarMejorCaso (int harris, int susan, int moravecA, int moravecB, 
			int moravecC, int doGA, int doGB, int doGC, int harrisFP, int susanFP,int moravecFPA, int moravecFPB, 
			int moravecFPC, int doGFPA, int doGFPB, int doGFPC){
		
		int indiceAMarcar = 0;
		
		List<Integer> valoresAComparar = new LinkedList<Integer>();
		valoresAComparar.add(harris);
		valoresAComparar.add(susan);
		valoresAComparar.add(moravecA);
		valoresAComparar.add(moravecB);
		valoresAComparar.add(moravecC);
		valoresAComparar.add(doGA);
		valoresAComparar.add(doGB);
		valoresAComparar.add(doGC);
		
		List<Integer> valoresFP = new LinkedList<Integer>();
		valoresFP.add(harrisFP);
		valoresFP.add(susanFP);
		valoresFP.add(moravecFPA);
		valoresFP.add(moravecFPB);
		valoresFP.add(moravecFPC);
		valoresFP.add(doGFPA);
		valoresFP.add(doGFPB);
		valoresFP.add(doGFPC);
		
		List<Integer> valoresACompararFP = new LinkedList<Integer>();
		
		List<Integer> casoMayoresResultadosCorrectos = calcularMayor(valoresAComparar);
		int ret = 0;
		
		//Si hay un único detector cuya cantidad de puntos correctos detectados es la
		//mayor entre todos los detectores, entonces ese es el de mejor desempeño
		if(casoMayoresResultadosCorrectos.size() < 2){
			
			ret = casoMayoresResultadosCorrectos.get(0);			
		} else {
			
			//Si hay un empate de puntos correctos entre dos o más detectores, los
			//comparamos en cantidad de falsos positivos detectados
			for(Integer val : casoMayoresResultadosCorrectos){
				
				switch (val) {
				case 0:
					valoresACompararFP.add(harrisFP);
					break;
				case 1:
					valoresACompararFP.add(susanFP);
					break;
				case 2:
					valoresACompararFP.add(moravecFPA);
					break;
				case 3:
					valoresACompararFP.add(moravecFPB);
					break;
				case 4:
					valoresACompararFP.add(moravecFPC);
					break;
				case 5:
					valoresACompararFP.add(doGFPA);
					break;
				case 6:
					valoresACompararFP.add(doGFPB);
					break;
				default:
					valoresACompararFP.add(doGFPC);
					break;
				}
			}
			
			//el detector que haya detectado menos cantidad de falsos positivos es el
			//de mejor desempeño ante un empate en resultaods correctos
			ret = calcularMenor(valoresACompararFP);
			indiceAMarcar = valoresFP.indexOf(ret);
		}
		
		return indiceAMarcar;
	}
	
	//Calcula el mayor valor de puntos correctos detectados
	public static List<Integer> calcularMayor(List<Integer> valores) {
	    LinkedList<Integer> resultados = new LinkedList<Integer>();
	    int ret = -1;
	    
	    //obtengo mayor valor
	    for (Integer val : valores) {
	        if (val != null && val > ret) {
	            ret = val;
	        }
	    }
	    
	    //obtengo indices correspondientes al mayor valor
	    for (int j = 0; j < valores.size(); j++) {
	        if (valores.get(j) == ret) {
	            resultados.add(j);
	        }
	    }
	   	    
	    return resultados;
	}
	
	
	//Calcula el menor valor de falsos positivos detectados
	public static Integer calcularMenor(List<Integer> valores){
		
	    Integer ret = Integer.MAX_VALUE;
	    int contador = 0;
	    
	    for (int j = 0; j < valores.size(); j++) {
	        if (valores.get(j) < ret) {
	            ret = valores.get(j);
	            contador++;
	        }
	    }
	    
	    if (contador > 1){ret = -1;}
	    
	    return ret;
	}
	
	
	
	 /* Genera un objeto del tipo Imagen a partir del archivo en el path
	 ingresado*/	
	private static Imagen obtenerImagen(String path) throws IOException {
		fileActual = new File(path);
		Archivo archivoActual = new Archivo(fileActual);
		
		BufferedImage esquinasRectas = ImageIO.read(ClassLoader.getSystemResource(path));
		
		FormatoDeImagen formatoDeLaImagen = FormatoDeImagen
				.getFormato(archivoActual.getExtension());

		Imagen imagen = new Imagen(esquinasRectas, formatoDeLaImagen,
				archivoActual.getNombre());
		return imagen;
	}
	
	
	/*Genera imagen con ruido a partir de la imagen y según el sigma que recibe como
	parámetro*/
	public static Imagen generarImagenConRuido(Imagen imagenOriginal, int sigma){
		BufferedImage bufferedImageConRuido = GeneradorDeRuido.generarRuidoGauss(imagenOriginal.getBufferedImage(), sigma, 0);
		Imagen imagenConRuido = new Imagen(bufferedImageConRuido, imagenOriginal.getFormato(), imagenOriginal.getNombre() + "conRuido");
		return imagenConRuido;
	}
	
	
	//----------------------------------------- HARRIS -----------------------------------------//
	public static int getContadorHarrisCorrectosSinRuidoTotal() {
		return contadorHarrisCorrectosSinRuidoTotal;
	}

	public static void setContadorHarrisCorrectosSinRuidoTotal(int contadorHarrisCorrectosSinRuidoTotal) {
		AlgoritmoComparativoDeDetectores.contadorHarrisCorrectosSinRuidoTotal += contadorHarrisCorrectosSinRuidoTotal;
	}


	public static int getContadorHarrisFalsosPositivosSinRuidoTotal() {
		return contadorHarrisFalsosPositivosSinRuidoTotal;
	}

	public static void setContadorHarrisFalsosPositivosSinRuidoTotal(int contadorHarrisFalsosPositivosSinRuidoTotal) {
		AlgoritmoComparativoDeDetectores.contadorHarrisFalsosPositivosSinRuidoTotal += contadorHarrisFalsosPositivosSinRuidoTotal;
	}

	public static int getContadorHarrisCorrectosConRuidoTotal() {
		return contadorHarrisCorrectosConRuidoTotal;
	}

	public static void setContadorHarrisCorrectosConRuidoTotal(int contadorHarrisCorrectosConRuidoTotal) {
		AlgoritmoComparativoDeDetectores.contadorHarrisCorrectosConRuidoTotal += contadorHarrisCorrectosConRuidoTotal;
	}

	public static int getContadorHarrisFalsosPositivosConRuidoTotal() {
		return contadorHarrisFalsosPositivosConRuidoTotal;
	}

	public static void setContadorHarrisFalsosPositivosConRuidoTotal(int contadorHarrisFalsosPositivosConRuidoTotal) {
		AlgoritmoComparativoDeDetectores.contadorHarrisFalsosPositivosConRuidoTotal += contadorHarrisFalsosPositivosConRuidoTotal;
	}
	//----------------------------------------------------------------------------------//



	//----------------------------------------- SUSAN -----------------------------------------//
	public static int getContadorSusanCorrectosSinRuidoTotal() {
		return contadorSusanCorrectosSinRuidoTotal;
	}

	public static void setContadorSusanCorrectosSinRuidoTotal(int contadorSusanCorrectosSinRuidoTotal) {
		AlgoritmoComparativoDeDetectores.contadorSusanCorrectosSinRuidoTotal += contadorSusanCorrectosSinRuidoTotal;
	}

	public static int getContadorSusanFalsosPositivosSinRuidoTotal() {
		return contadorSusanFalsosPositivosSinRuidoTotal;
	}

	public static void setContadorSusanFalsosPositivosSinRuidoTotal(int contadorSusanFalsosPositivosSinRuidoTotal) {
		AlgoritmoComparativoDeDetectores.contadorSusanFalsosPositivosSinRuidoTotal += contadorSusanFalsosPositivosSinRuidoTotal;
	}

	public static int getContadorSusanCorrectosConRuidoTotal() {
		return contadorSusanCorrectosConRuidoTotal;
	}

	public static void setContadorSusanCorrectosConRuidoTotal(int contadorSusanCorrectosConRuidoTotal) {
		AlgoritmoComparativoDeDetectores.contadorSusanCorrectosConRuidoTotal += contadorSusanCorrectosConRuidoTotal;
	}

	public static int getContadorSusanFalsosPositivosConRuidoTotal() {
		return contadorSusanFalsosPositivosConRuidoTotal;
	}

	public static void setContadorSusanFalsosPositivosConRuidoTotal(int contadorSusanFalsosPositivosConRuidoTotal) {
		AlgoritmoComparativoDeDetectores.contadorSusanFalsosPositivosConRuidoTotal += contadorSusanFalsosPositivosConRuidoTotal;
	}
	//----------------------------------------------------------------------------------//


	//----------------------------------------- MORAVEC -----------------------------------------//
	public static int getContadorMoravecCorrectosSinRuidoTotalA() {
		return contadorMoravecCorrectosSinRuidoTotalA;
	}

	public static void setContadorMoravecCorrectosSinRuidoTotalA(int contadorMoravecCorrectosSinRuidoTotalA) {
		AlgoritmoComparativoDeDetectores.contadorMoravecCorrectosSinRuidoTotalA += contadorMoravecCorrectosSinRuidoTotalA;
	}

	public static int getContadorMoravecFalsosPositivosSinRuidoTotalA() {
		return contadorMoravecFalsosPositivosSinRuidoTotalA;
	}

	public static void setContadorMoravecFalsosPositivosSinRuidoTotalA(int contadorMoravecFalsosPositivosSinRuidoTotalA) {
		AlgoritmoComparativoDeDetectores.contadorMoravecFalsosPositivosSinRuidoTotalA += contadorMoravecFalsosPositivosSinRuidoTotalA;
	}

	public static int getContadorMoravecCorrectosSinRuidoTotalB() {
		return contadorMoravecCorrectosSinRuidoTotalB;
	}

	public static void setContadorMoravecCorrectosSinRuidoTotalB(int contadorMoravecCorrectosSinRuidoTotalB) {
		AlgoritmoComparativoDeDetectores.contadorMoravecCorrectosSinRuidoTotalB += contadorMoravecCorrectosSinRuidoTotalB;
	}
	
	public static int getContadorMoravecFalsosPositivosSinRuidoTotalB() {
		return contadorMoravecFalsosPositivosSinRuidoTotalB;
	}

	public static void setContadorMoravecFalsosPositivosSinRuidoTotalB(int contadorMoravecFalsosPositivosSinRuidoTotalB) {
		AlgoritmoComparativoDeDetectores.contadorMoravecFalsosPositivosSinRuidoTotalB += contadorMoravecFalsosPositivosSinRuidoTotalB;
	}

	public static int getContadorMoravecCorrectosSinRuidoTotalC() {
		return contadorMoravecCorrectosSinRuidoTotalC;
	}

	public static void setContadorMoravecCorrectosSinRuidoTotalC(int contadorMoravecCorrectosSinRuidoTotalC) {
		AlgoritmoComparativoDeDetectores.contadorMoravecCorrectosSinRuidoTotalC += contadorMoravecCorrectosSinRuidoTotalC;
	}

	public static int getContadorMoravecFalsosPositivosSinRuidoTotalC() {
		return contadorMoravecFalsosPositivosSinRuidoTotalC;
	}

	public static void setContadorMoravecFalsosPositivosSinRuidoTotalC(int contadorMoravecFalsosPositivosSinRuidoTotalC) {
		AlgoritmoComparativoDeDetectores.contadorMoravecFalsosPositivosSinRuidoTotalC += contadorMoravecFalsosPositivosSinRuidoTotalC;
	}

	public static int getContadorMoravecCorrectosConRuidoTotalA() {
		return contadorMoravecCorrectosConRuidoTotalA;
	}

	public static void setContadorMoravecCorrectosConRuidoTotalA(int contadorMoravecCorrectosConRuidoTotalA) {
		AlgoritmoComparativoDeDetectores.contadorMoravecCorrectosConRuidoTotalA += contadorMoravecCorrectosConRuidoTotalA;
	}

	public static int getContadorMoravecFalsosPositivosConRuidoTotalA() {
		return contadorMoravecFalsosPositivosConRuidoTotalA;
	}

	public static void setContadorMoravecFalsosPositivosConRuidoTotalA(int contadorMoravecFalsosPositivosConRuidoTotalA) {
		AlgoritmoComparativoDeDetectores.contadorMoravecFalsosPositivosConRuidoTotalA += contadorMoravecFalsosPositivosConRuidoTotalA;
	}

	public static int getContadorMoravecCorrectosConRuidoTotalB() {
		return contadorMoravecCorrectosConRuidoTotalB;
	}

	public static void setContadorMoravecCorrectosConRuidoTotalB(int contadorMoravecCorrectosConRuidoTotalB) {
		AlgoritmoComparativoDeDetectores.contadorMoravecCorrectosConRuidoTotalB += contadorMoravecCorrectosConRuidoTotalB;
	}

	public static int getContadorMoravecFalsosPositivosConRuidoTotalB() {
		return contadorMoravecFalsosPositivosConRuidoTotalB;
	}

	public static void setContadorMoravecFalsosPositivosConRuidoTotalB(int contadorMoravecFalsosPositivosConRuidoTotalB) {
		AlgoritmoComparativoDeDetectores.contadorMoravecFalsosPositivosConRuidoTotalB += contadorMoravecFalsosPositivosConRuidoTotalB;
	}

	public static int getContadorMoravecCorrectosConRuidoTotalC() {
		return contadorMoravecCorrectosConRuidoTotalC;
	}

	public static void setContadorMoravecCorrectosConRuidoTotalC(int contadorMoravecCorrectosConRuidoTotalC) {
		AlgoritmoComparativoDeDetectores.contadorMoravecCorrectosConRuidoTotalC += contadorMoravecCorrectosConRuidoTotalC;
	}

	public static int getContadorMoravecFalsosPositivosConRuidoTotalC() {
		return contadorMoravecFalsosPositivosConRuidoTotalC;
	}

	public static void setContadorMoravecFalsosPositivosConRuidoTotalC(int contadorMoravecFalsosPositivosConRuidoTotalC) {
		AlgoritmoComparativoDeDetectores.contadorMoravecFalsosPositivosConRuidoTotalC += contadorMoravecFalsosPositivosConRuidoTotalC;
	}
	//----------------------------------------------------------------------------------//


	//----------------------------------------- DoG -----------------------------------------//
	public static int getContadorDoGCorrectosSinRuidoTotalA() {
		return contadorDoGCorrectosSinRuidoTotalA;
	}

	public static void setContadorDoGCorrectosSinRuidoTotalA(int contadorDoGCorrectosSinRuidoTotalA) {
		AlgoritmoComparativoDeDetectores.contadorDoGCorrectosSinRuidoTotalA += contadorDoGCorrectosSinRuidoTotalA;
	}

	public static int getContadorDoGFalsosPositivosSinRuidoTotalA() {
		return contadorDoGFalsosPositivosSinRuidoTotalA;
	}

	public static void setContadorDoGFalsosPositivosSinRuidoTotalA(int contadorDoGFalsosPositivosSinRuidoTotalA) {
		AlgoritmoComparativoDeDetectores.contadorDoGFalsosPositivosSinRuidoTotalA += contadorDoGFalsosPositivosSinRuidoTotalA;
	}

	public static int getContadorDoGCorrectosSinRuidoTotalB() {
		return contadorDoGCorrectosSinRuidoTotalB;
	}

	public static void setContadorDoGCorrectosSinRuidoTotalB(int contadorDoGCorrectosSinRuidoTotalB) {
		AlgoritmoComparativoDeDetectores.contadorDoGCorrectosSinRuidoTotalB += contadorDoGCorrectosSinRuidoTotalB;
	}

	public static int getContadorDoGFalsosPositivosSinRuidoTotalB() {
		return contadorDoGFalsosPositivosSinRuidoTotalB;
	}

	public static void setContadorDoGFalsosPositivosSinRuidoTotalB(int contadorDoGFalsosPositivosSinRuidoTotalB) {
		AlgoritmoComparativoDeDetectores.contadorDoGFalsosPositivosSinRuidoTotalB += contadorDoGFalsosPositivosSinRuidoTotalB;
	}

	public static int getContadorDoGCorrectosSinRuidoTotalC() {
		return contadorDoGCorrectosSinRuidoTotalC;
	}

	public static void setContadorDoGCorrectosSinRuidoTotalC(int contadorDoGCorrectosSinRuidoTotalC) {
		AlgoritmoComparativoDeDetectores.contadorDoGCorrectosSinRuidoTotalC += contadorDoGCorrectosSinRuidoTotalC;
	}

	public static int getContadorDoGFalsosPositivosSinRuidoTotalC() {
		return contadorDoGFalsosPositivosSinRuidoTotalC;
	}

	public static void setContadorDoGFalsosPositivosSinRuidoTotalC(int contadorDoGFalsosPositivosSinRuidoTotalC) {
		AlgoritmoComparativoDeDetectores.contadorDoGFalsosPositivosSinRuidoTotalC += contadorDoGFalsosPositivosSinRuidoTotalC;
	}

	public static int getContadorDoGCorrectosConRuidoTotalA() {
		return contadorDoGCorrectosConRuidoTotalA;
	}

	public static void setContadorDoGCorrectosConRuidoTotalA(int contadorDoGCorrectosConRuidoTotalA) {
		AlgoritmoComparativoDeDetectores.contadorDoGCorrectosConRuidoTotalA += contadorDoGCorrectosConRuidoTotalA;
	}

	public static int getContadorDoGFalsosPositivosConRuidoTotalA() {
		return contadorDoGFalsosPositivosConRuidoTotalA;
	}

	public static void setContadorDoGFalsosPositivosConRuidoTotalA(int contadorDoGFalsosPositivosConRuidoTotalA) {
		AlgoritmoComparativoDeDetectores.contadorDoGFalsosPositivosConRuidoTotalA += contadorDoGFalsosPositivosConRuidoTotalA;
	}

	public static int getContadorDoGCorrectosConRuidoTotalB() {
		return contadorDoGCorrectosConRuidoTotalB;
	}

	public static void setContadorDoGCorrectosConRuidoTotalB(int contadorDoGCorrectosConRuidoTotalB) {
		AlgoritmoComparativoDeDetectores.contadorDoGCorrectosConRuidoTotalB += contadorDoGCorrectosConRuidoTotalB;
	}

	public static int getContadorDoGFalsosPositivosConRuidoTotalB() {
		return contadorDoGFalsosPositivosConRuidoTotalB;
	}

	public static void setContadorDoGFalsosPositivosConRuidoTotalB(int contadorDoGFalsosPositivosConRuidoTotalB) {
		AlgoritmoComparativoDeDetectores.contadorDoGFalsosPositivosConRuidoTotalB += contadorDoGFalsosPositivosConRuidoTotalB;
	}

	public static int getContadorDoGCorrectosConRuidoTotalC() {
		return contadorDoGCorrectosConRuidoTotalC;
	}

	public static void setContadorDoGCorrectosConRuidoTotalC(int contadorDoGCorrectosConRuidoTotalC) {
		AlgoritmoComparativoDeDetectores.contadorDoGCorrectosConRuidoTotalC += contadorDoGCorrectosConRuidoTotalC;
	}

	public static int getContadorDoGFalsosPositivosConRuidoTotalC() {
		return contadorDoGFalsosPositivosConRuidoTotalC;
	}

	public static void setContadorDoGFalsosPositivosConRuidoTotalC(int contadorDoGFalsosPositivosConRuidoTotalC) {
		AlgoritmoComparativoDeDetectores.contadorDoGFalsosPositivosConRuidoTotalC += contadorDoGFalsosPositivosConRuidoTotalC;
	}
	//----------------------------------------------------------------------------------//
	
	
	/*LLevamos a 0 todas las variables para que no se acumulen resultados 
	en la siguiente ejecución del algoritmo comparativo*/
	public static void resetearVariables(){
		
		contadorHarrisCorrectosSinRuidoTotal = 0;
		contadorHarrisFalsosPositivosSinRuidoTotal = 0;
		contadorHarrisCorrectosConRuidoTotal = 0;
		contadorHarrisFalsosPositivosConRuidoTotal = 0;
		contadorSusanCorrectosSinRuidoTotal = 0;
		contadorSusanFalsosPositivosSinRuidoTotal = 0;
		contadorSusanCorrectosConRuidoTotal = 0;
		contadorSusanFalsosPositivosConRuidoTotal = 0;
		contadorMoravecCorrectosSinRuidoTotalA = 0;
		contadorMoravecFalsosPositivosSinRuidoTotalA = 0;
		contadorMoravecCorrectosSinRuidoTotalB = 0;
		contadorMoravecFalsosPositivosSinRuidoTotalB = 0;
		contadorMoravecCorrectosSinRuidoTotalC = 0;
		contadorMoravecFalsosPositivosSinRuidoTotalC = 0;
		contadorMoravecCorrectosConRuidoTotalA = 0;
		contadorMoravecFalsosPositivosConRuidoTotalA = 0;
		contadorMoravecCorrectosConRuidoTotalB = 0;
		contadorMoravecFalsosPositivosConRuidoTotalB = 0;
		contadorMoravecCorrectosConRuidoTotalC = 0;
		contadorMoravecFalsosPositivosConRuidoTotalC = 0;
		sigmas = new LinkedList<Integer[]>();
		contadorDoGCorrectosSinRuidoTotalA = 0;
		contadorDoGFalsosPositivosSinRuidoTotalA = 0;
		contadorDoGCorrectosSinRuidoTotalB = 0;
		contadorDoGFalsosPositivosSinRuidoTotalB = 0;
		contadorDoGCorrectosSinRuidoTotalC = 0;
		contadorDoGFalsosPositivosSinRuidoTotalC = 0;
		contadorDoGCorrectosConRuidoTotalA = 0;
		contadorDoGFalsosPositivosConRuidoTotalA = 0;
		contadorDoGCorrectosConRuidoTotalB = 0;
		contadorDoGFalsosPositivosConRuidoTotalB = 0;
		contadorDoGCorrectosConRuidoTotalC = 0;
		contadorDoGFalsosPositivosConRuidoTotalC = 0;
		tiemposSinRuido = new HashMap<Double, String>();
		tiemposConRuido = new HashMap<Double, String>();
		imagenesAProcesar = new LinkedList<Imagen>();
		imagenesAProcesarConRuido = new LinkedList<Imagen>();
		mejorCaso = new String[8];
	}
}
