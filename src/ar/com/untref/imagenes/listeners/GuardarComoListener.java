package ar.com.untref.imagenes.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.modelo.Imagen;

public class GuardarComoListener implements ActionListener {

	private Imagen imagenAGuardar;
	private JPanel panel;

	public GuardarComoListener(Imagen imagen, JPanel panel) {

		this.imagenAGuardar = imagen;
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JFileChooser fileChooser = new JFileChooser();
		int saveValue = fileChooser.showSaveDialog(null);
		
		if (saveValue == JFileChooser.APPROVE_OPTION) {
			try {
				
				ImageIO.write(imagenAGuardar.getBufferedImage(), imagenAGuardar.getFormato().getNombre(), new File(fileChooser.getSelectedFile().getAbsolutePath() + imagenAGuardar.getFormato().getExtension()));
				DialogsHelper.mostarMensaje(panel, "Imagen guardada.");
			
			} catch (IOException exception) {
				
				exception.printStackTrace();
				DialogsHelper.mostarMensaje(panel, "No se pudo guardar la imagen.", NivelMensaje.ERROR);
			}
		}
	}
	
}
