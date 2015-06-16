package ar.com.untref.imagenes.ventanas;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.listeners.MarcarFotogramaListener;
import ar.com.untref.imagenes.modelo.Fotogramas;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeVideo;
import ar.com.untref.imagenes.segmentacion.Segmentador;

@SuppressWarnings("serial")
public class VentanaVideo extends JFrame{
	
	private JPanel panelBotones;
	private JLabel labelPrincipal;
	private JButton botonSegmentar;
	private JButton botonSiguienteFotograma;
	private JButton botonFotogramaAnterior;
	private JButton botonPrincipio;
	private JButton botonPlay;
	
	public VentanaVideo() {
		this.setTitle("Procesamiento de Video");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		getContentPane().setLayout(null);
		
		labelPrincipal = new JLabel("");
		labelPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
		labelPrincipal.setBounds(0, 0, 774, 474);
		getContentPane().add(labelPrincipal);
		
		panelBotones = new JPanel();
		panelBotones.setBounds(0, 485, 774, 43);
		getContentPane().add(panelBotones);
		
		JPanel panel1 = new JPanel();
		panelBotones.add(panel1);
		
		botonSegmentar = new JButton("Segmentar");
		botonSegmentar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				BufferedImage bufferSegmentado = segmentarImagen();
				VentanaVideo.this.refrescarImagen(bufferSegmentado);
				habilitarBotonesNavegacion();
				botonSegmentar.setEnabled(false);
			}

		});
		botonSegmentar.setHorizontalAlignment(SwingConstants.LEFT);
		botonSegmentar.setEnabled(false);
		panel1.add(botonSegmentar);
		
		JButton botonSeleccionar = new JButton("Seleccionar");
		botonSeleccionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DialogsHelper.mostarMensaje(getContentPane(), "Cliquea en la esquina superior izquierda y la inferior derecha que formarán el cuadrado para marcar una región en la imagen");
				labelPrincipal.addMouseListener(new MarcarFotogramaListener(VentanaVideo.this));
				botonSegmentar.setEnabled(true);
			}
		});
		panel1.add(botonSeleccionar);
		
		botonFotogramaAnterior = new JButton("Anterior");
		botonFotogramaAnterior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				botonSiguienteFotograma.setEnabled(true);
				botonPlay.setEnabled(true);
				
				if (ProcesadorDeVideo.obtenerInstancia().retrocederUnFotograma()){
					
					BufferedImage bufferSegmentado = volverASegmentarImagen();
					refrescarImagen(bufferSegmentado);
					botonPrincipio.setEnabled(true);
					botonFotogramaAnterior.setEnabled(true);
				} else {
					
					DialogsHelper.mostarMensaje(getContentPane(), "El video comienza aquí");
					botonPrincipio.setEnabled(false);
					botonFotogramaAnterior.setEnabled(false);
				};
			
			}
		});
		
		botonPrincipio = new JButton("Principio");
		botonPrincipio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ProcesadorDeVideo.obtenerInstancia().reiniciar();
				
				BufferedImage bufferSegmentado = segmentarImagen();
				refrescarImagen(bufferSegmentado);
				botonPrincipio.setEnabled(false);
				botonFotogramaAnterior.setEnabled(false);
				botonSiguienteFotograma.setEnabled(true);
				botonPlay.setEnabled(true);
			}
		});
		botonPrincipio.setEnabled(false);
		panel1.add(botonPrincipio);
		botonFotogramaAnterior.setEnabled(false);
		panel1.add(botonFotogramaAnterior);
		
		botonSiguienteFotograma = new JButton("Siguiente");
		botonSiguienteFotograma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				botonPrincipio.setEnabled(true);
				botonFotogramaAnterior.setEnabled(true);

				if (ProcesadorDeVideo.obtenerInstancia().avanzarUnFotograma()){
					
					BufferedImage bufferSegmentado = volverASegmentarImagen();
					refrescarImagen(bufferSegmentado);
					botonSiguienteFotograma.setEnabled(true);
					botonPlay.setEnabled(true);
				} else {
					
					DialogsHelper.mostarMensaje(getContentPane(), "Fin del video");
					botonSiguienteFotograma.setEnabled(false);
					botonPlay.setEnabled(false);
				};
			
			}
		});
		botonSiguienteFotograma.setEnabled(false);
		panel1.add(botonSiguienteFotograma);
		
		botonPlay = new JButton("Play");
		botonPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
			         @Override
			         protected Void doInBackground() throws Exception {

			        	while (ProcesadorDeVideo.obtenerInstancia().avanzarUnFotograma()){
								
							BufferedImage bufferSegmentado = volverASegmentarImagen();
							refrescarImagen(bufferSegmentado);
						}
						return null;
			         }
			    };
			    mySwingWorker.execute();
				
				botonPrincipio.setEnabled(true);
				botonFotogramaAnterior.setEnabled(true);
				botonSiguienteFotograma.setEnabled(false);
				botonPlay.setEnabled(false);
			}
		});
		botonPlay.setEnabled(false);
		panel1.add(botonPlay);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNuevoVideo = new JMenu("Nuevo Video");
		menuBar.add(mnNuevoVideo);
		
		JMenuItem mntmHoja = new JMenuItem("Abuela");
		mntmHoja.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ProcesadorDeVideo.obtenerInstancia().cargarVideo(Fotogramas.ABUELA);
				refrescarImagen(ProcesadorDeVideo.obtenerInstancia().getImagenActual().getBufferedImage());
			}
		});
		mnNuevoVideo.add(mntmHoja);
		
	}
	
	private BufferedImage segmentarImagen() {
		
		ProcesadorDeVideo procesador = ProcesadorDeVideo.obtenerInstancia();
		BufferedImage imagenNueva = ProcesadorDeImagenes.obtenerInstancia().clonarBufferedImage(procesador.getImagenActual().getBufferedImage()); 
		Imagen image = new Imagen(imagenNueva, FormatoDeImagen.JPEG, "segmentada");
		
		BufferedImage bufferSegmentado = Segmentador.segmentarImagenPrimeraVez(image, 
				new Point(procesador.getX1(), procesador.getY1()), 
				new Point(procesador.getX2(), procesador.getY2()), 100, 50);
		return bufferSegmentado;
	}
	
	private BufferedImage volverASegmentarImagen() {
		
		ProcesadorDeVideo procesador = ProcesadorDeVideo.obtenerInstancia();
		BufferedImage imagenNueva = ProcesadorDeImagenes.obtenerInstancia().clonarBufferedImage(procesador.getImagenActual().getBufferedImage()); 
		Imagen image = new Imagen(imagenNueva, FormatoDeImagen.JPEG, "segmentada");
		
		BufferedImage bufferSegmentado = Segmentador.volverASegmentar(image, 100, 50);
		return bufferSegmentado;
	}
	
	public void refrescarImagen(BufferedImage imagen) {

		labelPrincipal.setIcon(new ImageIcon(imagen));
	}

	public JLabel getPanelDeImagen() {

		return labelPrincipal;
	}

	public void habilitarBotonSegmentar() {

		botonSegmentar.setEnabled(true);
	}
	
	public void habilitarBotonesNavegacion(){
		
		botonSiguienteFotograma.setEnabled(true);
		botonPlay.setEnabled(true);
	}
}
