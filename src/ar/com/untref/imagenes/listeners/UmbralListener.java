package ar.com.untref.imagenes.listeners;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;

public class UmbralListener implements ChangeListener {

	private VentanaPrincipal ventana;

	public UmbralListener(Imagen imagen, VentanaPrincipal ventana) {

		this.ventana = ventana;
		ProcesadorDeImagenes.obtenerInstancia().setImagenOriginal(imagen);
	}

	@Override
	public void stateChanged(ChangeEvent e) {

		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) {

			ProcesadorDeImagenes.obtenerInstancia().umbralizarImagen(((JSlider) e.getSource()).getValue());
			ventana.refrescarImagen();
		}
	}

}
