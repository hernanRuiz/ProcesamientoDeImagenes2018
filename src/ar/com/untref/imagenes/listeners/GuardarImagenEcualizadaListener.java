package ar.com.untref.imagenes.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;

public class GuardarImagenEcualizadaListener implements ActionListener{

	private JPanel panel;
	private BufferedImage imagen;
	
	public GuardarImagenEcualizadaListener(BufferedImage imagen) {

		this.imagen = imagen;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {

		JFileChooser fileChooser = new JFileChooser();
		int saveValue = fileChooser.showSaveDialog(null);

		if (saveValue == JFileChooser.APPROVE_OPTION) {
			try {
				
				ImageIO.write(imagen, "jpg",
						new File(fileChooser.getSelectedFile()
								.getAbsolutePath() + ".jpg"));

				DialogsHelper.mostarMensaje(panel, "Imagen guardada.");

			} catch (IOException exception) {

				exception.printStackTrace();
				DialogsHelper.mostarMensaje(panel,
						"No se pudo guardar la imagen.", NivelMensaje.ERROR);
			}
		}
		
	}

}
