package ar.com.untref.imagenes.ventanas;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

import ar.com.untref.imagenes.bordes.AlgoritmoComparativoDeDetectores;
import ar.com.untref.imagenes.bordes.DetectorDeHarris;
import ar.com.untref.imagenes.bordes.DetectorSusan;
import ar.com.untref.imagenes.bordes.DoG;
import ar.com.untref.imagenes.bordes.DetectorDeBordesMoravec;
import ar.com.untref.imagenes.dialogs.AlgoritmoComparativoDialog;
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


@SuppressWarnings("serial")
public class VentanaPrincipal extends JFrame {

	private JPanel contentPane;
	private JMenu menuItemEditar;
	private JLabel labelPrincipal;
	private JMenu menu;
	private JTextField textFieldAnchoRAW;
	private JTextField textFieldAltoRAW;
	private JMenuItem menuItemGuardarComo;
	private EspereDialog dialogoEspera;
	private JPanel volverAImagenOriginal;

	public VentanaPrincipal() {

		this.setTitle("Procesamiento de Im\u00e1genes");
				
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		ImageIcon iconoSubirArchivo = new ImageIcon(VentanaPrincipal.class.getResource("/resources/upload.png"));
		labelPrincipal = new JLabel(iconoSubirArchivo, JLabel.CENTER);
		scrollPane.setViewportView(labelPrincipal);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JPanel panelRaw = new JPanel();
		panel.add(panelRaw);
		
		volverAImagenOriginal = new JPanel();

		JButton volverALaImagenOriginal = new JButton("Imagen Original");
		volverALaImagenOriginal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Imagen imageOriginal = ProcesadorDeImagenes.obtenerInstancia().getImagenOriginal();
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imageOriginal);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		volverAImagenOriginal.add(volverALaImagenOriginal);
		volverAImagenOriginal.setVisible(false);
		contentPane.add(volverAImagenOriginal, BorderLayout.SOUTH);
		
		JLabel labelTamanioRAW = new JLabel("Dimensiones RAW");
		panelRaw.add(labelTamanioRAW);
		
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
		
		JMenuItem menuItem = new JMenuItem("Cerrar");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
			}
		});
		
		JMenuItem menuItemAbrirImagen = new JMenuItem("Abrir Imagen");
		menuItemGuardarComo = new JMenuItem("Guardar Como...");
		
		labelPrincipal.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			
				if (ProcesadorDeImagenes.obtenerInstancia().getImagenActual()==null){
					
					cargarImagen(labelPrincipal, menuItemGuardarComo);
				} else {
					
					((Component) e.getSource()).removeMouseListener(this);
				}
			}
		});
		
		menuItemAbrirImagen.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			
				cargarImagen(labelPrincipal, menuItemGuardarComo);
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
					if(imagenElegida != null){
						actualizarPanelDeImagen(menuItemGuardarComo, imagenElegida);
					}
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
		
		menuItemEditar = new JMenu(" Editar ");
		inhabilitarItem(menuItemEditar);
		
		menuBar.add(menuItemEditar);
				
		JMenuItem menuItemAplicarRuido = new JMenuItem("Aplicar ruido");
		menuItemAplicarRuido.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenOriginal();
				if ( imagenActual!=null ){	
					VentanaRuido ventanaRuido = new VentanaRuido(imagenActual);
					ventanaRuido.setVisible(true);
				}
			}
		
		});
		menuItemEditar.add(menuItemAplicarRuido);		
		
		JMenu menuFiltros = new JMenu("Filtros");
		menuItemEditar.add(menuFiltros);
		
		JMenuItem menuItemFiltroGaussiano = new JMenuItem("Filtro Gaussiano");
		menuItemFiltroGaussiano.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FiltroGaussianoDialog m = new FiltroGaussianoDialog(VentanaPrincipal.this, contentPane);
				m.setVisible(true);
			}
		});
		menuFiltros.add(menuItemFiltroGaussiano);
		
	
		JMenu menuDeteccionDeBordes = new JMenu("Detecci\u00f3n de Bordes");
		menuItemEditar.add(menuDeteccionDeBordes);
		
		
		JMenuItem menuItemAlgoritmoComparativo = new JMenuItem(" Algoritmo comparativo de detectores");
		menuItemAlgoritmoComparativo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				AlgoritmoComparativoDialog dialog = new AlgoritmoComparativoDialog(VentanaPrincipal.this, contentPane);
				dialog.setVisible(true);
			}
		});
		
		menuBar.add(menuItemAlgoritmoComparativo);
		
		JMenuItem blanco = new JMenuItem();
		blanco.setEnabled(false);
		JMenuItem blanco2 = new JMenuItem();
		blanco2.setEnabled(false);
		menuBar.add(blanco);
		menuBar.add(blanco2);
		
		JMenuItem menuItemDetectorDeSusan = new JMenuItem("Detector de Susan");
		menuItemDetectorDeSusan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				SusanDialog dialog = new SusanDialog(VentanaPrincipal.this, contentPane);
				dialog.setVisible(true);
			}
		});
		
		JMenuItem menuItemHarris = new JMenuItem("Detector de Harris");
		menuItemHarris.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
			         @Override
			         protected Void doInBackground() throws Exception {
			        	 dialogoEspera = new EspereDialog(VentanaPrincipal.this);
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
		
		
		JMenuItem menuItemMoravec = new JMenuItem("Detector de Bordes de Movarec");
		menuItemMoravec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				DetectorDeMoravecDialog d = new DetectorDeMoravecDialog(VentanaPrincipal.this, contentPane);
				d.setVisible(true);
			}
		});
		menuDeteccionDeBordes.add(menuItemMoravec);
		
		JMenuItem menuItemDoG = new JMenuItem("Diferencia de Gaussianas");
		menuItemDoG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				DiferenciaDeGaussianasDialog d = new DiferenciaDeGaussianasDialog(VentanaPrincipal.this, contentPane);
				d.setVisible(true);
			}
		});
		menuDeteccionDeBordes.add(menuItemDoG);
		menuDeteccionDeBordes.add(menuItemHarris);
		menuDeteccionDeBordes.add(menuItemDetectorDeSusan);
			
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
		VentanaPrincipal.this.refrescarImagen();
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
		VentanaPrincipal.this.refrescarImagen();
	}
	
	//Una vez que abrimos una imagen, la desplegamos en pantalla
	private void cargarImagen(JLabel labelPrincipal,
			JMenuItem menuItemGuardarComo) {
		
		Imagen imagenElegida = ProcesadorDeImagenes.obtenerInstancia()
				.cargarUnaImagenDesdeArchivo();
		if(imagenElegida != null){
			actualizarPanelDeImagen(menuItemGuardarComo, imagenElegida);
		}
	}

	private void inhabilitarItem(JMenuItem item) {

		item.addActionListener(null);
		item.setEnabled(false);
	}

	private void chequearGuardarComo(JMenuItem menuItemGuardarComo) {
		Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia()
				.getImagenActual();

		if (imagenActual != null) {

			GuardarComoListener listener = new GuardarComoListener(
					imagenActual, contentPane);
			menuItemGuardarComo.setEnabled(true);

			if (menuItemGuardarComo.getActionListeners().length > 0) {

				menuItemGuardarComo.removeActionListener(menuItemGuardarComo
						.getActionListeners()[0]);
			}
			menuItemGuardarComo.addActionListener(listener);
		} else {

			menuItemGuardarComo.removeActionListener(menuItemGuardarComo
					.getActionListeners()[0]);
			menuItemGuardarComo.setEnabled(false);
		}
	}


	//Habilitamos el menu editar una vez que abrimos una imagen
	private void actualizarPanelDeImagen(final JMenuItem menuItemGuardarComo,
			Imagen imagenElegida) {
		if (imagenElegida != null) {

			labelPrincipal.setIcon(new ImageIcon(imagenElegida
					.getBufferedImage()));
			menuItemEditar.setEnabled(true);
			volverAImagenOriginal.setVisible(true);
		}

		chequearGuardarComo(menuItemGuardarComo);
	}

	
	/*Reload de la imagen en su estado actual ante cualquier modificación que se haga
	sobre ella*/
	public void refrescarImagen() {

		Imagen imagen = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		labelPrincipal.setIcon(new ImageIcon(imagen.getBufferedImage()));
		chequearGuardarComo(menuItemGuardarComo);
		ocultarDialogoDeEspera();
	}


	public JLabel getPanelDeImagen(){
		return labelPrincipal;
	}

	public void aplicarDetectorSusan(String flag) {
		
		Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		Imagen imagenResultante = new Imagen(DetectorSusan.aplicar(imagenActual, flag, true), imagenActual.getFormato(), imagenActual.getNombre()+"_susan");
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenResultante);
		DetectorSusan.setResultadosX(new LinkedList<Integer>());
		DetectorSusan.setResultadosY(new LinkedList<Integer>());
		VentanaPrincipal.this.refrescarImagen();
		
	}
	
	public void aplicarFiltroGaussiano(final int sigmaElegido) {
		SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
			@Override
	         protected Void doInBackground() throws Exception {
	        	 dialogoEspera = new EspereDialog(VentanaPrincipal.this);
	        	 Runnable r = new Runnable() {
			         public void run() {
			        	ProcesadorDeImagenes proc = ProcesadorDeImagenes.obtenerInstancia();
			      		Imagen imagenAnterior = proc.getImagenActual();
			      		Imagen imagenResultante = FiltroGaussiano.aplicarFiltroGaussiano(imagenAnterior, sigmaElegido);
			      		proc.setImagenOriginal(new Imagen(imagenAnterior.getBufferedImage(), proc.getImagenActual().getFormato(), proc.getImagenActual().getNombre()));
			      		proc.setImagenActual(imagenResultante);
			      		VentanaPrincipal.this.refrescarImagen();
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
	
	
	public void ejecutarAlgoritmoComparativo(final String coleccion, final int sigma) throws IOException{
		
		AlgoritmoComparativoDeDetectores.aplicarAlgoritmo(ProcesadorDeImagenes.obtenerInstancia().getImagenActual(), coleccion, sigma);
	}
	
	
	public void mostrarDialogoDeEspera(){
		
		if(this.dialogoEspera != null){this.dialogoEspera.mostrar();}
	}
	
	public void ocultarDialogoDeEspera(){
		
		if(this.dialogoEspera != null){this.dialogoEspera.ocultar();}
	}

}
