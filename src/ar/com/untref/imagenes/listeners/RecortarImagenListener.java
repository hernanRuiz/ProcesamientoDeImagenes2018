package ar.com.untref.imagenes.listeners;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;

public class RecortarImagenListener implements MouseListener{
	
	private VentanaPrincipal ventanaPrincipal;
	private Integer x1;
	private Integer y1;
	private Integer x2;
	private Integer y2;
	private Dimension bordeSuperiorIzquierdo;
	private Dimension bordeInferiorDerecho;

	public RecortarImagenListener(VentanaPrincipal ventana) {

		this.ventanaPrincipal = ventana;
		bordeInferiorDerecho = calcularBordeInferiorDerecho(ventanaPrincipal.getPanelDeImagen(), ProcesadorDeImagenes.obtenerInstancia().getImagenActual());
		bordeSuperiorIzquierdo = calcularBordeSuperiorIzquierdo(ventanaPrincipal.getPanelDeImagen(), ProcesadorDeImagenes.obtenerInstancia().getImagenActual());
	}
	
	
	@Override
	public void mouseClicked(MouseEvent click) {

		if (x1==null){
			
			x1 = click.getX();
			y1= click.getY();
		} else {
			
			x2 = click.getX();
			y2= click.getY();

			if(validarClick(x1, y1) && validarClick(x2, y2)){
				
				int x1Final = (int) (x1 - bordeSuperiorIzquierdo.getWidth());
				int y1Final = (int) (y1 - bordeSuperiorIzquierdo.getHeight());
				
				int x2Final = (int) (x2 - bordeSuperiorIzquierdo.getWidth());
				int y2Final = (int) (y2 - bordeSuperiorIzquierdo.getHeight());
				
				ProcesadorDeImagenes.obtenerInstancia().recortarImagenActual(x1Final, y1Final, x2Final, y2Final, ventanaPrincipal);
				((Component) click.getSource()).removeMouseListener(this);
				ventanaPrincipal.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
		}
	}
	
	public boolean validarClick(int x1, int y1){
		
		return x1 >= bordeSuperiorIzquierdo.getWidth() && y1 >= bordeSuperiorIzquierdo.getHeight() && x1 <= bordeInferiorDerecho.getWidth() && y1 <= bordeInferiorDerecho.getHeight();
	}
	
	public Dimension calcularBordeSuperiorIzquierdo(JLabel panelPrincipal, Imagen imagenActual){
		
		int centroImagenX = (imagenActual.getBufferedImage().getWidth())/2;
		int centroPanelPrincipalX = panelPrincipal.getWidth()/2;
		
		int centroImagenY = (imagenActual.getBufferedImage().getHeight())/2;
		int centroPanelPrincipalY = panelPrincipal.getHeight()/2;
		
		
		int xInicial = centroPanelPrincipalX - centroImagenX;
		int YInicial = centroPanelPrincipalY - centroImagenY;
		
		return new Dimension(xInicial, YInicial);
	}

	public Dimension calcularBordeInferiorDerecho(JLabel panelPrincipal, Imagen imagenActual){
		
		int centroImagenX = (imagenActual.getBufferedImage().getWidth())/2;
		int centroPanelPrincipalX = panelPrincipal.getWidth()/2;
		
		int centroImagenY = (imagenActual.getBufferedImage().getHeight())/2;
		int centroPanelPrincipalY = panelPrincipal.getHeight()/2;
		
		
		int xInicial = centroPanelPrincipalX + centroImagenX;
		int YInicial = centroPanelPrincipalY + centroImagenY;
		
		return new Dimension(xInicial, YInicial);
	}
	
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		
		ventanaPrincipal.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
		ventanaPrincipal.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
