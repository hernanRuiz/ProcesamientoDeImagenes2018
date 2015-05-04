package ar.com.untref.imagenes.ventanas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
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
import ar.com.untref.imagenes.listeners.GuardarHistogramasListener;
import ar.com.untref.imagenes.modelo.Histograma;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;
import ar.com.untref.imagenes.ruido.GeneradorDeRuido;

@SuppressWarnings("serial")
public class VentanaHistogramas extends JFrame{

	private JPanel contentPane;
	private JMenuItem menuItemGuardarComo;
	private JMenuItem menuItemEcualizar;
	private JFreeChart histogramaRojo;
	private JFreeChart histogramaVerde;
	private JFreeChart histogramaAzul;
	
	public VentanaHistogramas(Imagen imagen, boolean esHistogramaEstandar) {
		
		this.setTitle("Histogramas");
		
		setPreferredSize(new Dimension(80, 600));
		setSize(new Dimension(800, 600));
		setBounds(100, 100, 800, 600);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("Archivo");
		menuBar.add(menu);
		
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
		Image dimg = imagen.getBufferedImage().getScaledInstance(labelImagen.getWidth(), labelImagen.getHeight(), Image.SCALE_REPLICATE);
		labelImagen.setIcon(new ImageIcon(dimg));
		
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
				
				VentanaHistogramas.this.setVisible(false);
			}
		});
		menuItemGuardarComo = new JMenuItem("Guardar Como...");
		
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
		
		menu.add(menuItemGuardarComo);
		
		menu.add(menuItem);
		
		if (!esHistogramaEstandar && GeneradorDeRuido.getMatrizRojos()!=null){
			
			dibujarHistogramaConValoresSinTransformar(imagen, panelHistoRojo, panelHistoAzul, panelHistoVerde, GeneradorDeRuido.getMatrizRojos(), GeneradorDeRuido.getMatrizVerdes(), GeneradorDeRuido.getMatrizAzules());
		} else {
			
			dibujarHistogramaComun(imagen, panelHistoRojo, panelHistoAzul, panelHistoVerde);
		}
		
		GuardarHistogramasListener listener = new GuardarHistogramasListener(contentPane, histogramaRojo, histogramaVerde, histogramaAzul);
		menuItemGuardarComo.addActionListener(listener);
	}

	private void dibujarHistogramaComun(Imagen imagen, JPanel panelHistoRojo,
			JPanel panelHistoAzul, JPanel panelHistoVerde) {
		for (int i = 0; i < 3; i++) {

			switch (i) {
			case 0:
				
				histogramaRojo = dibujarHistograma(Histograma.calcularHistogramaRojo(imagen.getBufferedImage()), panelHistoRojo, Canal.ROJO);
				break;
			case 1:

				histogramaVerde = dibujarHistograma(Histograma.calcularHistogramaVerde(imagen.getBufferedImage()), panelHistoVerde, Canal.VERDE);
				break;
			case 2:
				
				histogramaAzul = dibujarHistograma(Histograma.calcularHistogramaAzul(imagen.getBufferedImage()), panelHistoAzul, Canal.AZUL);
				break;
			}
		}
	}
	
	private void dibujarHistogramaConValoresSinTransformar(Imagen imagen, JPanel panelHistoRojo, 
			JPanel panelHistoAzul, JPanel panelHistoVerde, float[][] matrizRojos, float[][] matrizVerdes, float[][] matrizAzules) {
		
		for (int i = 0; i < 3; i++) {

			switch (i) {
			case 0:
				
				histogramaRojo = dibujarHistograma(Histograma.calcularHistogramaDelCanal(matrizRojos),panelHistoRojo, Canal.ROJO);
				break;
			case 1:

				histogramaVerde= dibujarHistograma(Histograma.calcularHistogramaDelCanal(matrizVerdes),panelHistoVerde, Canal.VERDE);
				break;
			case 2:
				
				histogramaAzul = dibujarHistograma(Histograma.calcularHistogramaDelCanal(matrizAzules), panelHistoAzul, Canal.AZUL);
				break;
			}
		}
	}
	
	private JFreeChart dibujarHistograma(float[] histograma, JPanel jPanel, Canal colorBarras) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		String serie = "Porcentaje";

		if (histograma.length > 256){

			int separacion = (int) (histograma.length / 255);
			
			for (int i = 0; i < histograma.length; i=i+separacion) {
				dataset.addValue(histograma[i], serie, "" + i);
			}
			
		} else {
			
			for (int i = 0; i < histograma.length; i++) {
				dataset.addValue(histograma[i], serie, "" + i);
			}
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
