package ar.com.untref.imagenes.listeners;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JColorChooser;

import ar.com.untref.imagenes.ventanas.VentanaPrincipal;

public class MostrarTablaDeColoresListener implements MouseListener{

	private VentanaPrincipal ventanaPrincipal;
	
	public MostrarTablaDeColoresListener(VentanaPrincipal panel){
		
		this.ventanaPrincipal = panel;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		Color colorElegido = JColorChooser.showDialog(null, "Pixel - Cambiar el color", Color.WHITE);
		if (colorElegido!=null){
			
			ventanaPrincipal.cambiarColorDePixel(colorElegido.getRGB());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		ventanaPrincipal.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		ventanaPrincipal.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
