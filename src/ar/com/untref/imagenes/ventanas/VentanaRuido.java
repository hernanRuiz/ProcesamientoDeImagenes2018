package ar.com.untref.imagenes.ventanas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import ar.com.untref.imagenes.bordes.DetectorDeBordesMoravec;
import ar.com.untref.imagenes.bordes.DetectorDeHarris;
import ar.com.untref.imagenes.bordes.DetectorSusan;
import ar.com.untref.imagenes.bordes.DoG;
import ar.com.untref.imagenes.dialogs.DetectorDeMoravecDialog;
import ar.com.untref.imagenes.dialogs.DiferenciaDeGaussianasDialog;
import ar.com.untref.imagenes.dialogs.EspereDialog;
import ar.com.untref.imagenes.dialogs.FiltroGaussianoDialog;
import ar.com.untref.imagenes.dialogs.SusanDialog;
import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.filtros.FiltroGaussiano;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.listeners.GuardarComoListener;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;
import ar.com.untref.imagenes.ruido.GeneradorDeRuido;

@SuppressWarnings("serial")
public class VentanaRuido extends JFrame {

	private JPanel contentPane;
	private JMenu menuItemEditar;
	private JLabel labelPrincipal;
	private JLabel labelSigma;
	private JMenu menu;
	private JTextField textFieldSigma;
	private JTextField textFieldAnchoRAW;
	private JTextField textFieldAltoRAW;
	private JMenuItem menuItemGuardarComo;
	private static Imagen imagenSinCambios;
	private EspereDialog dialogoEspera;
	private JPanel volverAImagenOriginal;
	
	public VentanaRuido(Imagen imagenSCambios) {
		
		dialogoEspera = new EspereDialog(this);
		imagenSinCambios = imagenSCambios;
		this.setTitle("Generador de Ruido y Filtros");
		
		setBounds(100, 100, 800, 600);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu = new JMenu("Archivo");
		menuBar.add(menu);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		ImageIcon imagen = new ImageIcon(imagenSinCambios.getBufferedImage());
		labelPrincipal = new JLabel(imagen, JLabel.CENTER);
		
		scrollPane.setViewportView(labelPrincipal);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JPanel panelRuido = new JPanel();
		panel.add(panelRuido);
		
		JButton volverALaImagenOriginal = new JButton("Imagen Original");
		volverALaImagenOriginal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(VentanaRuido.imagenSinCambios);
				VentanaRuido.this.refrescarImagen();
			}
		});
		
		volverAImagenOriginal = new JPanel();
		volverAImagenOriginal.add(volverALaImagenOriginal);
		contentPane.add(volverAImagenOriginal, BorderLayout.SOUTH);
		
		//Panel RAW
		JPanel panelRaw = new JPanel();
		panelRuido.add(panelRaw);
		
		JLabel labelTamañoRAW = new JLabel("RAW");
		panelRaw.add(labelTamañoRAW);
		
		JLabel labelAltoRAW = new JLabel("Alto:");
		panelRaw.add(labelAltoRAW);
		
		textFieldAltoRAW = new JTextField();
		panelRaw.add(textFieldAltoRAW);
		textFieldAltoRAW.setMinimumSize(new Dimension(3, 20));
		textFieldAltoRAW.setPreferredSize(new Dimension(1, 20));
		textFieldAltoRAW.setText("256");
		textFieldAltoRAW.setColumns(3);
		
		JLabel labelAnchoRAW = new JLabel("Ancho:");
		panelRaw.add(labelAnchoRAW);
		
		textFieldAnchoRAW = new JTextField();
		panelRaw.add(textFieldAnchoRAW);
		textFieldAnchoRAW.setText("256");
		textFieldAnchoRAW.setColumns(3);
		
		JLabel ruidoGauss = new JLabel("Ruido Gaussiano -");
		panelRuido.add(ruidoGauss);
		
		labelSigma = new JLabel("σ:");
		panelRuido.add(labelSigma);
		
		textFieldSigma = new JTextField();
		panelRuido.add(textFieldSigma);
		textFieldSigma.setMinimumSize(new Dimension(3, 20));
		textFieldSigma.setPreferredSize(new Dimension(1, 20));
		textFieldSigma.setColumns(3);
		
		JButton aplicarRuidoGauss = new JButton("Aplicar");
		panelRuido.add(aplicarRuidoGauss);
		aplicarRuidoGauss.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String campoSigma = textFieldSigma.getText().trim();

				if (!campoSigma.isEmpty()){
					
					try {
						
					final Integer sigma = Integer.valueOf(campoSigma);
					final Integer mu = 0;
					
					SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
				         @Override
				         protected Void doInBackground() throws Exception {

				        	BufferedImage bufferedImage = GeneradorDeRuido.generarRuidoGauss(ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage(), sigma, mu);
							Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
							Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
							ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
							
							VentanaRuido.this.refrescarImagen();
				            
							return null;
				         }
				      };

				      mySwingWorker.execute();
				      mostrarDialogoDeEspera();
					
					} catch (Exception e) {
						
						DialogsHelper.mostarMensaje(contentPane, "Por favor ingrese parámetro numérico", NivelMensaje.ERROR);
					}
				} else {
					
					DialogsHelper.mostarMensaje(contentPane, "Por favor completa el campo Sigma", NivelMensaje.ERROR);
				}
			}
		
		});
		
		JMenuItem menuItem = new JMenuItem("Cerrar");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				VentanaRuido.this.setVisible(false);
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(VentanaRuido.imagenSinCambios);
				VentanaRuido.this.refrescarImagen();
			}
		});
		
		JMenuItem menuItemAbrirImagen = new JMenuItem("Abrir Imagen");
		menuItemGuardarComo = new JMenuItem("Guardar Como...");
		
		menuItemAbrirImagen.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			
				cargarImagen(labelPrincipal, menuItemGuardarComo);
				VentanaRuido.imagenSinCambios = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
			}
		});
		
		menu.add(menuItemAbrirImagen);
		
		JMenuItem menuItemAbrirRaw = new JMenuItem("Abrir RAW");
		menuItemAbrirRaw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					Integer alto = Integer.valueOf(textFieldAltoRAW.getText().trim());
					Integer ancho = Integer.valueOf(textFieldAnchoRAW.getText().trim());
					Imagen imagenElegida = ProcesadorDeImagenes.obtenerInstancia().cargarUnaImagenRawDesdeArchivo(alto, ancho);
					actualizarPanelDeImagen(menuItemGuardarComo, imagenElegida);
					
					imagenSinCambios = imagenElegida;
				} catch (Exception e){
					
					e.printStackTrace();
					DialogsHelper.mostarMensaje(contentPane, "Por favor completa correctamente las dimensiones de la imagen RAW y vuelve a intentar"
							, NivelMensaje.ERROR);
				}
				
			}
		});
		
		menu.add(menuItemAbrirRaw);
			
		inhabilitarItem(menuItemGuardarComo);
		
		menu.add(menuItemGuardarComo);
		menu.add(menuItem);
		
		menuItemEditar = new JMenu("Editar");
				
		menuBar.add(menuItemEditar);
		
		JMenu menuFiltros = new JMenu("Filtros");
		menuItemEditar.add(menuFiltros);
		
		JMenuItem filtroGaussianoMenuItem = new JMenuItem("Filtro gaussiano");
		filtroGaussianoMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				FiltroGaussianoDialog m = new FiltroGaussianoDialog(VentanaRuido.this, contentPane);
				m.setVisible(true);
			}
		});
		
		menuFiltros.add(filtroGaussianoMenuItem);
		
		JMenu menuDeteccionDeBordes = new JMenu("Deteccion de Bordes");
		menuItemEditar.add(menuDeteccionDeBordes);
		
		JMenuItem menuItemHarris = new JMenuItem("Detector de Harris");
		menuItemHarris.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
			         @Override
			         protected Void doInBackground() throws Exception {

			        	 dialogoEspera = new EspereDialog(VentanaRuido.this);
			        	 Runnable r = new Runnable() {
					         public void run() {
				        		 Imagen imagenConHarris = DetectorDeHarris.detectarEsquinas(ProcesadorDeImagenes.obtenerInstancia().getImagenActual(), true);
				        		 ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenConHarris);
				        		 DetectorDeHarris.setResultadosX(new LinkedList<Integer>());
				        		 DetectorDeHarris.setResultadosY(new LinkedList<Integer>());
				        		 refrescarImagen();
				        		 dialogoEspera.ocultar();
					         }
					     };

					    Thread ejecutar = new Thread(r);
					    ejecutar.start();
					     
					    dialogoEspera.mostrar();
						return null;
			         }
			      };

			      mySwingWorker.execute();
			}
		});
		menuDeteccionDeBordes.add(menuItemHarris);
		
		JMenuItem menuItemDetectorDeSusan = new JMenuItem("Detector de Susan");
		menuItemDetectorDeSusan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				SusanDialog dialog = new SusanDialog(VentanaRuido.this, contentPane);
				dialog.setVisible(true);
			}
		});
		menuDeteccionDeBordes.add(menuItemDetectorDeSusan);
		
		
		JMenuItem menuItemMoravec = new JMenuItem("Detector de Bordes de Movarec");
		menuItemMoravec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				DetectorDeMoravecDialog d = new DetectorDeMoravecDialog(VentanaRuido.this, contentPane);
				d.setVisible(true);
			}
		});
		menuDeteccionDeBordes.add(menuItemMoravec);
		
		
		JMenuItem menuItemDoG = new JMenuItem("Diferencia de Gaussianas");
		menuItemDoG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				DiferenciaDeGaussianasDialog d = new DiferenciaDeGaussianasDialog(VentanaRuido.this, contentPane);
				d.setVisible(true);
			}
		});
		menuDeteccionDeBordes.add(menuItemDoG);
				
	}
	

	private void cargarImagen(JLabel labelPrincipal,
			JMenuItem menuItemGuardarComo) {
		Imagen imagenElegida = ProcesadorDeImagenes.obtenerInstancia().cargarUnaImagenDesdeArchivo();
		actualizarPanelDeImagen(menuItemGuardarComo, imagenElegida);
	}
	
	
	private void inhabilitarItem(JMenuItem item){
		
		item.addActionListener(null);
		item.setEnabled(false);
	}

	private void chequearGuardarComo(JMenuItem menuItemGuardarComo) {
		
		Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		
		if (imagenActual!=null){
			
			GuardarComoListener listener = new GuardarComoListener(imagenActual, contentPane);
			menuItemGuardarComo.setEnabled(true);
			
			if (menuItemGuardarComo.getActionListeners().length >0){
				
				menuItemGuardarComo.removeActionListener(menuItemGuardarComo.getActionListeners()[0]);
			}
			menuItemGuardarComo.addActionListener(listener);
		} else {
			
			menuItemGuardarComo.removeActionListener(menuItemGuardarComo.getActionListeners()[0]);
			menuItemGuardarComo.setEnabled(false);
		}
	}

	private void actualizarPanelDeImagen(
			final JMenuItem menuItemGuardarComo, Imagen imagenElegida) {
		if (imagenElegida!=null){
			
			labelPrincipal.setIcon(new ImageIcon(imagenElegida.getBufferedImage()));
		}
		
		chequearGuardarComo(menuItemGuardarComo);
	}

	public void refrescarImagen() {

		ocultarDialogoDeEspera();
		Imagen imagen = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		labelPrincipal.setIcon(new ImageIcon(imagen.getBufferedImage()));
		chequearGuardarComo(menuItemGuardarComo);
	}

	public void aplicarFiltroGaussiano(final Integer sigmaElegido) {
		SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
			@Override
	         protected Void doInBackground() throws Exception {
	        	 dialogoEspera = new EspereDialog(VentanaRuido.this);
	        	 Runnable r = new Runnable() {
			         public void run() {
			        	ProcesadorDeImagenes proc = ProcesadorDeImagenes.obtenerInstancia();
			      		Imagen imagenAnterior = proc.getImagenActual();
			      		Imagen imagenResultante = FiltroGaussiano.aplicarFiltroGaussiano(imagenAnterior, sigmaElegido);
			      		proc.setImagenOriginal(new Imagen(imagenAnterior.getBufferedImage(), proc.getImagenActual().getFormato(), proc.getImagenActual().getNombre()));
			      		proc.setImagenActual(imagenResultante);
			      		VentanaRuido.this.refrescarImagen();
			      		dialogoEspera.ocultar();
			         }
			     };

			     Thread ejecutar = new Thread(r);
			     ejecutar.start();
			     
			     dialogoEspera.mostrar();
			     return null;
			}
		};
		mySwingWorker.execute();
	}


	public void mostrarDialogoDeEspera(){
		
		this.dialogoEspera.mostrar();
	}
	
	public void ocultarDialogoDeEspera(){
		
		this.dialogoEspera.ocultar();
	}
	
	public void aplicarDetectorSusan(String flag) {
		
		Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		Imagen imagenResultante = new Imagen(DetectorSusan.aplicar(imagenActual, flag, true), imagenActual.getFormato(), imagenActual.getNombre()+"_susan");
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenResultante);
		DetectorSusan.setResultadosX(new LinkedList<Integer>());
		DetectorSusan.setResultadosY(new LinkedList<Integer>());
		VentanaRuido.this.refrescarImagen();
		
	}
	
	
	public void aplicarDetectorDeBordesDeMoravec(int umbral, int radio) {
		
		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage bufferedImage = DetectorDeBordesMoravec.aplicarMoravec(imagenAnterior, radio, umbral, true);
		Imagen nuevaImagenActual = new Imagen(bufferedImage,
				imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(
				nuevaImagenActual);
		DetectorDeBordesMoravec.setResultadosX(new LinkedList<Integer>());
		DetectorDeBordesMoravec.setResultadosY(new LinkedList<Integer>());
		VentanaRuido.this.refrescarImagen();
	}
	
	
	public void aplicarDoG(int sigma1, int sigma2, int sigma3, int sigma4) {
		
		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage bufferedImage = DoG.aplicar(imagenAnterior, sigma1, sigma2, sigma3, sigma4, true);
		Imagen nuevaImagenActual = new Imagen(bufferedImage,
				imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(
				nuevaImagenActual);
		DoG.setResultadosX(new LinkedList<Integer>());
		DoG.setResultadosY(new LinkedList<Integer>());
		VentanaRuido.this.refrescarImagen();
	}
	
}



