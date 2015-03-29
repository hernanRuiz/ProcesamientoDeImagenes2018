package ar.com.untref.imagenes.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;

public class GuardarHistogramasListener implements ActionListener{

	private JPanel panel;
	private JFreeChart histogramaRojo;
	private JFreeChart histogramaVerde;
	private JFreeChart histogramaAzul;
	
	public GuardarHistogramasListener(JPanel panel, JFreeChart histogramaRojo, JFreeChart histogramaVerde, JFreeChart histogramaAzul) {

		this.panel = panel;
		this.histogramaRojo = histogramaRojo;
		this.histogramaAzul = histogramaAzul;
		this.histogramaVerde = histogramaVerde;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {

		JFileChooser fileChooser = new JFileChooser();
		int saveValue = fileChooser.showSaveDialog(null);

		if (saveValue == JFileChooser.APPROVE_OPTION) {
			try {

				File ubicacion = fileChooser.getSelectedFile();
				
				File nuevoArchivoHistRojo = new File(ubicacion.getAbsolutePath()+"_rojo.jpg");
				File nuevoArchivoHistVerde = new File(ubicacion.getAbsolutePath()+"_verde.jpg");
				File nuevoArchivoHistAzul = new File(ubicacion.getAbsolutePath()+"_azul.jpg");

				ChartUtilities.saveChartAsJPEG(nuevoArchivoHistRojo, histogramaRojo, 400, 300);
				ChartUtilities.saveChartAsJPEG(nuevoArchivoHistVerde, histogramaVerde, 400, 300);
				ChartUtilities.saveChartAsJPEG(nuevoArchivoHistAzul, histogramaAzul, 400, 300);

				DialogsHelper.mostarMensaje(panel, "Histogramas guardados.");

			} catch (IOException exception) {

				exception.printStackTrace();
				DialogsHelper.mostarMensaje(panel,
						"No se pudieron guardar los histogramas.", NivelMensaje.ERROR);
			}
		}
		
	}

}
