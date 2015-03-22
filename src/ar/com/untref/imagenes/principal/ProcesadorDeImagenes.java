package ar.com.untref.imagenes.principal;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.modelo.Archivo;
import ar.com.untref.imagenes.modelo.Imagen;

public class ProcesadorDeImagenes {
	
    private Archivo archivoActual;
    private Imagen imagenActual;
    
    /**
     * Abre una imagen de archivo y la convierte en buffered image.
     * @return Imagen
     */
    public Imagen cargarUnaImagenDesdeArchivo(){
    	
        Imagen imagenADevolver = null;

        JFileChooser selector=new JFileChooser();
        selector.setDialogTitle("Seleccione una imagen");

        FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("JPG & GIF & BMP & RAW", "raw", "jpg", "gif", "bmp");
        selector.setFileFilter(filtroImagen);
        
        int flag=selector.showOpenDialog(null);
        
        if(flag==JFileChooser.APPROVE_OPTION){
            try {

            	archivoActual= new Archivo(selector.getSelectedFile());
                BufferedImage bufferedImage = ImageIO.read(archivoActual.getFile());

				FormatoDeImagen formatoDeLaImagen = FormatoDeImagen.getFormato(archivoActual.getExtension());
                
				if (formatoDeLaImagen != FormatoDeImagen.DESCONOCIDO){
					
					Imagen imagen = new Imagen(bufferedImage, formatoDeLaImagen, archivoActual.getNombre());
					
					imagenActual = imagen;
					imagenADevolver = imagen;
				}
            
            } catch (Exception e) {
            	
            	e.printStackTrace();
            }
        }
        
        return imagenADevolver;
    }
    
	public Imagen getImagenActual() {
		return imagenActual;
	}

}
