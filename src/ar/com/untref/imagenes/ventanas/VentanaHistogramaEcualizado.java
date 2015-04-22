package ar.com.untref.imagenes.ventanas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.listeners.GuardarComoListener;
import ar.com.untref.imagenes.listeners.GuardarImagenEcualizadaListener;
import ar.com.untref.imagenes.modelo.HistogramEQ;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.modelo.OperacionMatematica;
import ar.com.untref.imagenes.procesamiento.OperacionesManager;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;

@SuppressWarnings("serial")
public class VentanaHistogramaEcualizado extends JFrame {

	private JPanel contentPane;
	private JMenuItem menuItemGuardarComo;
	private JMenuItem menuItemEcualizar;
	private BufferedImage bufferedImage;
	
	public VentanaHistogramaEcualizado(final Imagen imagen) {
		
		this.setTitle("Ecualización de Histogramas");
		
		setPreferredSize(new Dimension(80, 600));
		setSize(new Dimension(800, 600));
		setBounds(100, 100, 800, 600);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("Archivo");
		menuBar.add(menu);
		
		menuItemEcualizar = new JMenuItem("Ecualizar");
		menuItemEcualizar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
				
				Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				if ( imagenActual!=null ){
					
					VentanaHistogramaEcualizado ventanaHistogramaEcualizado = new VentanaHistogramaEcualizado(imagenActual);
					ventanaHistogramaEcualizado.setVisible(true);
				}
			}
		});
		menu.add(menuItemEcualizar);
		
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(800, 600));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(2, 2, 0, 0));
		
		JPanel panelImagen = new JPanel();
		contentPane.add(panelImagen);
		
		JLabel labelImagen = new JLabel("");
		labelImagen.setPreferredSize(new Dimension(380, 250));
		labelImagen.setSize(new Dimension(380, 250));
		panelImagen.add(labelImagen);
		
		BufferedImage miImagen = imagen.getBufferedImage();
		ArrayList<float[]> histogramaEQ = HistogramEQ.histogramEqualization(miImagen);
		bufferedImage = HistogramEQ.getImagenEcualizada();
		Image imagenEcualizada = bufferedImage.getScaledInstance(labelImagen.getWidth(), labelImagen.getHeight(), Image.SCALE_SMOOTH);
		labelImagen.setIcon(new ImageIcon(imagenEcualizada));
		
		JPanel panelHistoRojo = new JPanel();
		contentPane.add(panelHistoRojo);
		
		JLabel labelHistoRojo = new JLabel("");
		panelHistoRojo.add(labelHistoRojo);
		
		JPanel panelHistoAzul = new JPanel();
		contentPane.add(panelHistoAzul);
		
		JLabel labelHistoAzul = new JLabel("");
		panelHistoAzul.add(labelHistoAzul);
		
		JPanel panelHistoVerde = new JPanel();
		contentPane.add(panelHistoVerde);
		
		JLabel labelHistoVerde = new JLabel("");
		panelHistoVerde.add(labelHistoVerde);
		
		JMenuItem menuItem = new JMenuItem("Cerrar");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				VentanaHistogramaEcualizado.this.setVisible(false);
			}
		});
		menuItemGuardarComo = new JMenuItem("Guardar Como...");
		menu.add(menuItemGuardarComo);
		
		JMenuItem menuItemEcualizarYRestar = new JMenuItem("Ecualizar y restar");
		menuItemEcualizarYRestar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				if ( imagenActual!=null ){
					
					VentanaHistogramaEcualizado ventanaHistogramaEcualizado = new VentanaHistogramaEcualizado(imagenActual);
					ventanaHistogramaEcualizado.setVisible(true);
					ventanaHistogramaEcualizado.mostrarResta(imagen, imagenActual);
				}
			}
		});
		menu.add(menuItemEcualizarYRestar);
		
		menu.add(menuItem);
		
		for (int i = 0; i < 3; i++) {

			switch (i) {
			case 0:
				
				dibujarHistograma(histogramaEQ.get(0), panelHistoRojo, Canal.ROJO);
				break;
			case 1:

				dibujarHistograma(histogramaEQ.get(1), panelHistoVerde, Canal.VERDE);
				break;
			case 2:
				
				dibujarHistograma(histogramaEQ.get(2), panelHistoAzul, Canal.AZUL);
				break;
			}
		}
		
		GuardarImagenEcualizadaListener listener = new GuardarImagenEcualizadaListener(bufferedImage);
		menuItemGuardarComo.addActionListener(listener);
	
	}
	
	protected void mostrarResta(Imagen imagen, Imagen imagenActual) {

		Imagen imagenDeResta = OperacionesManager.restarImagenes(VentanaHistogramaEcualizado.this, contentPane, imagen, imagenActual, OperacionMatematica.RESTA);
		guardarImagen(imagenDeResta);
	}

	private void guardarImagen(Imagen imagenAGuardar) {

		JFileChooser fileChooser = new JFileChooser();
		int saveValue = fileChooser.showSaveDialog(null);

		if (saveValue == JFileChooser.APPROVE_OPTION) {
			try {

				// Si la imagen era un .raw la guardo como JPG mandatoriamente,
				// sino se guarda con el mismo formato que tenia previamente
				if (imagenAGuardar.getFormato().equals(FormatoDeImagen.RAW)) {

					ImageIO.write(imagenAGuardar.getBufferedImage(), "jpg",
							new File(fileChooser.getSelectedFile()
									.getAbsolutePath() + ".jpg"));
				} else {

					ImageIO.write(imagenAGuardar.getBufferedImage(),
							imagenAGuardar.getFormato().getNombre(), new File(
									fileChooser.getSelectedFile()
											.getAbsolutePath()
											+ imagenAGuardar.getFormato()
													.getExtension()));
				}
				DialogsHelper.mostarMensaje(contentPane, "Imagen guardada.");

			} catch (IOException exception) {

				exception.printStackTrace();
				DialogsHelper.mostarMensaje(contentPane,
						"No se pudo guardar la imagen.", NivelMensaje.ERROR);
			}
		}
	}

	private JFreeChart dibujarHistograma(float[] histograma, JPanel jPanel, Canal colorBarras) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		String serie = "Porcentaje";

		for (int i = 0; i < histograma.length; i++) {
			dataset.addValue(histograma[i], serie, "" + i);
		}
		// Creamos el chart
		JFreeChart chart = ChartFactory.createBarChart("Histograma " + colorBarras.getNombre(), null,
				null, dataset, PlotOrientation.VERTICAL, true, true, false);
		// Modificamos el diseño del chart
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, colorBarras.getColor());
		chart.setAntiAlias(true);
		chart.setBackgroundPaint(new Color(255, 255, 255));
		jPanel.removeAll();
		jPanel.repaint();
		jPanel.setLayout(new java.awt.BorderLayout());
		jPanel.add(new ChartPanel(chart));
		
		chart.createBufferedImage(400, 300);
		jPanel.validate();
		
		return chart;
	}

}
