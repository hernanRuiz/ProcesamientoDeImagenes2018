package ar.com.untref.imagenes.bordes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.Umbralizador;

public class DetectorDeBordesMoravec {
	
	private static List<Integer> resultadosX = new LinkedList<Integer>();
	private static List<Integer> resultadosY = new LinkedList<Integer>();
	private static PrintStream fileStreamMoravec;

    public static BufferedImage aplicarMoravec(Imagen imagenOriginal, int tamanio, int umbral, boolean flagResultados) { 
    
    	//En ejecución individual seteamos archivo de salida
    	if(flagResultados){			
    		try {
    			fileStreamMoravec = new PrintStream("Salida_algoritmo_Moravec.txt");
    			System.setOut(fileStreamMoravec);
    			System.out.println("Resultados encontrados (máscara: " + tamanio + " x " + tamanio + ", umbral: " 
    					+ umbral + "):");
    			System.out.println();
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		}
		}
    	
    	//nodos vecinos en las 8 direcciones posibles desde el central
    	int[] xDelta = new int[]{ 
    			-1, 0, 1, 
    			-1, 1, 
    			-1, 0, 1 
    	}; 
    	
    	int[] yDelta = new int[]{ 
    			-1, -1, -1, 
    			0, 0,
    			1, 1, 1 
    	};
    	
    	Imagen imagenEsquinas = new Imagen(new BufferedImage(imagenOriginal.getBufferedImage().getWidth(), imagenOriginal.getBufferedImage().getHeight(), imagenOriginal.getBufferedImage().getType()), imagenOriginal.getFormato(), imagenOriginal.getNombre()+"_moravec");
    	ArrayList<int[][]> matrices = generateWindow(tamanio);
    	if (umbral < 100){umbral *= 350;} else {umbral *= 1.5;};
        int[][] xRedDelta = matrices.get(1);
        int[][] yRedDelta = matrices.get(0);
        int width = imagenOriginal.getBufferedImage().getWidth(); 
        int height = imagenOriginal.getBufferedImage().getHeight(); 
        
        //radio: para prevenir excepci�n por b�squeda fuera de los l�mites
        int radio = tamanio / 2; 
 
        //evaluamos cada pixel 
        for (int y = radio, maxY = height - radio; y < maxY; y++) { 
            for (int x = radio, maxX = width - radio; x < maxX; x++) { 
                
            	int minSum = Integer.MAX_VALUE; 
 
                //evaluamos en las 8 direcciones a su al rededor 
                for (int k = 0; k < 8; k++) { 
                    
                	//sy y sx son los puntos centrales de cada ventana desplazada 
                    int sy = y + yDelta[k]; 
                    int sx = x + xDelta[k]; 
 
                    //Evaluamos que las ventanas desplazadas no se exedan de los l�mites 
                    if (sy < radio || sx < radio || sy >= maxY || sx >= maxX) { 
                        continue; 
                    } 
 
                    int sum = 0; 
 
                    //Sumamos la diferencia entre la ventana al rededor del pixel actual y la ventana desplazada
                    for (int j = 0; j < xRedDelta.length; j++) {
                    	for (int i = 0; i < xRedDelta.length; i++) {
	                        int redX = x + xRedDelta[i][j]; 
	                        int redY = y + yRedDelta[i][j];
	                        Color colorRED = new Color (imagenOriginal.getBufferedImage().getRGB(redX, redY));
	                        int redValue = colorRED.getRed(); 
	                        int blueX = sx + xRedDelta[i][j]; 
	                        int blueY = sy + yRedDelta[i][j];
	                        Color colorBLUE = new Color (imagenOriginal.getBufferedImage().getRGB(blueX, blueY));
	                        int blueValue = colorBLUE.getRed(); 
	                        int dif = redValue - blueValue; 
	                        sum += dif * dif;
                    	}
                    }
                    
                    if (sum < minSum) { 
                        minSum = sum; 
                    } 
                } 
  
                //Evaluamos contra el umbral 
                if (minSum < umbral) { 
                    minSum = 0; 
                } else {
                	imagenEsquinas.getBufferedImage().setRGB(x, y, Color.RED.getRGB()); 
                } 
            } 	 
        }
        
        Imagen imagenUmbralizada = Umbralizador.umbralizarImagen(imagenEsquinas, Umbralizador.encontrarNuevoUmbralGlobal(imagenOriginal, Umbralizador.generarUmbralizacionOtsu(imagenEsquinas, Canal.ROJO, false)));
        Imagen imagenFinal = superponerAImagenOriginal(imagenUmbralizada, imagenOriginal, flagResultados);
            
        return imagenFinal.getBufferedImage();
    }
    
    //Generamos las dos máscaras que utiliza el detector
    private static ArrayList<int[][]> generateWindow(int tamanio) { 
        
      if ( tamanio % 2 == 0 ){
    	  tamanio = tamanio - 1;
      }
	  
	  ArrayList<int[][]> matrices = new ArrayList<>(); 
	  
	  int radio = tamanio / 2; 
      
	  int[][] mascara = new int[tamanio][tamanio];
	  int[][] mascaraTranspuesta = new int[tamanio][tamanio];
 
        for (int i = 0; i < tamanio; i++) { 
            for (int j = 0; j < tamanio; j++) {
            	
            	mascara[i][j] = i - radio;
            }
        }
        
        for (int a = 0; a < tamanio; a++) { 
            for (int b = 0; b < tamanio; b++) {
            	mascaraTranspuesta[b][a] = mascara[a][b];
            }
        }
           
		matrices.add(mascara);
		matrices.add(mascaraTranspuesta);
		
		return matrices;
    } 
    
	//Marcamos los puntos encontrados en la imagen original
    private static Imagen superponerAImagenOriginal(Imagen umbralizada, Imagen original, boolean flagResultados) {

		Imagen imagenFinal = new Imagen(new BufferedImage(umbralizada.getBufferedImage().getWidth(), umbralizada.getBufferedImage().getHeight(), umbralizada.getBufferedImage().getType()), FormatoDeImagen.JPEG, "final");
		
		for (int i=0; i< umbralizada.getBufferedImage().getWidth(); i++){
			for (int j=0; j< umbralizada.getBufferedImage().getHeight(); j++){
				
				Color colorEnUmbralizada = new Color(umbralizada.getBufferedImage().getRGB(i, j));
				if (colorEnUmbralizada.getRed()==255){
					
					imagenFinal.getBufferedImage().setRGB(i, j, Color.RED.getRGB());
					if(flagResultados){
						System.out.println(i + "," + j);
					}
					
					resultadosX.add(i);
					resultadosY.add(j);
					
				} else {
					
					imagenFinal.getBufferedImage().setRGB(i, j, original.getBufferedImage().getRGB(i, j));
				}
			}
		}
		
		if(flagResultados){
			resultadosX = new LinkedList<Integer>();
			resultadosY = new LinkedList<Integer>();
			fileStreamMoravec.flush();
			fileStreamMoravec.close();
		}
		
		return imagenFinal;
    }
    
    
    public static List<Integer> getResultadosX() {
		return resultadosX;
	}

	public static void setResultadosX(List<Integer> resultadosX) {
		DetectorDeBordesMoravec.resultadosX = resultadosX;
	}

	public static List<Integer> getResultadosY() {
		return resultadosY;
	}

	public static void setResultadosY(List<Integer> resultadosY) {
		DetectorDeBordesMoravec.resultadosY = resultadosY;
	}
}
