package ar.com.untref.imagenes.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import ar.com.untref.imagenes.bordes.AlgoritmoComparativoDeDetectores;
import ar.com.untref.imagenes.bordes.DetectarBordesDireccionales;
import ar.com.untref.imagenes.bordes.DetectorDeBordes;
import ar.com.untref.imagenes.bordes.DetectorDeBordesDeCanny;
import ar.com.untref.imagenes.bordes.DetectorDeHarris;
import ar.com.untref.imagenes.bordes.DetectorSusan;
import ar.com.untref.imagenes.bordes.DoG;
import ar.com.untref.imagenes.bordes.InterfaceDetectorDeBordes;
import ar.com.untref.imagenes.bordes.TransformadaDeHough;
import ar.com.untref.imagenes.bordes.DetectorDeBordesMoravec;
import ar.com.untref.imagenes.bordes.AlgoritmoComparativoDeDetectores;
import ar.com.untref.imagenes.dialogs.AlgoritmoComparativoDialog;
import ar.com.untref.imagenes.dialogs.DetectorDeCannyDialog;
import ar.com.untref.imagenes.dialogs.DetectorDeMoravecDialog;
import ar.com.untref.imagenes.dialogs.DiferenciaDeGaussianasDialog;
import ar.com.untref.imagenes.dialogs.DifusionAnisotropicaDialog;
import ar.com.untref.imagenes.dialogs.DifusionIsotropicaDialog;
import ar.com.untref.imagenes.dialogs.EspereDialog;
import ar.com.untref.imagenes.dialogs.FiltroGaussianoDialog;
import ar.com.untref.imagenes.dialogs.HisteresisDialog;
import ar.com.untref.imagenes.dialogs.HoughDialog;
import ar.com.untref.imagenes.dialogs.LoGDialog;
import ar.com.untref.imagenes.dialogs.OperacionesMatricesDialog;
import ar.com.untref.imagenes.dialogs.SegmentacionDialog;
import ar.com.untref.imagenes.dialogs.SiftDialog;
import ar.com.untref.imagenes.dialogs.SigmaDialog;
import ar.com.untref.imagenes.dialogs.SusanDialog;
import ar.com.untref.imagenes.enums.Canal;
import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.filtros.FiltroGaussiano;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.listeners.GuardarComoListener;
import ar.com.untref.imagenes.listeners.MarcarImagenListener;
import ar.com.untref.imagenes.listeners.MostrarTablaDeColoresListener;
import ar.com.untref.imagenes.listeners.RecortarImagenListener;
import ar.com.untref.imagenes.listeners.UmbralListener;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.modelo.MatrizDeColores;
import ar.com.untref.imagenes.modelo.OperacionMatematica;
import ar.com.untref.imagenes.procesamiento.ColorManager;
import ar.com.untref.imagenes.procesamiento.Difuminador;
import ar.com.untref.imagenes.procesamiento.Graficador;
import ar.com.untref.imagenes.procesamiento.MatricesManager;
import ar.com.untref.imagenes.procesamiento.OperacionesManager;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;
import ar.com.untref.imagenes.procesamiento.Umbralizador;
import ar.com.untref.imagenes.segmentacion.Segmentador;

@SuppressWarnings("serial")
public class VentanaPrincipal extends JFrame {

	private JPanel contentPane;
	private JMenu menuItemEditar;
	private JMenuItem menuItemRecortarImagen;
	private JLabel labelPrincipal;
	private JMenu menu;
	private JMenu menuItemTemplates;
	private JPanel panelPixel;
	private JPanel panelUmbral;
	private JTextField posicionXTextField;
	private JTextField posicionYTextField;
	private JTextField textFieldAnchoRAW;
	private JTextField textFieldAltoRAW;
	private JMenuItem menuItemGuardarComo;
	private JSlider umbralSlider;
	private JLabel resultadoCantidadPixeles;
	private EspereDialog dialogoEspera;

	public VentanaPrincipal() {

		dialogoEspera = new EspereDialog();

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
		
		final JPanel panelPromedios = new JPanel();
		final JPanel imagenOriginal = new JPanel();
		panelPromedios.add(imagenOriginal);
		
		final JButton botonSegmentar = new JButton("Segmentar");
		
		final JButton botonSeleccionar = new JButton("Seleccionar");
		botonSeleccionar.setVisible(false);
		botonSeleccionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DialogsHelper.mostarMensaje(contentPane, "Cliquea en la esquina superior izquierda y la inferior derecha que formar\u00e1n el cuadrado para marcar una regi\u00f3n en la imagen");
				labelPrincipal.addMouseListener(new MarcarImagenListener(VentanaPrincipal.this));
				botonSegmentar.setEnabled(true);
			}
		});
		panelPromedios.add(botonSeleccionar);
		
		final JLabel cantidadPixeles = new JLabel("Cantidad de Pixeles:");
		cantidadPixeles.setVisible(false);
		resultadoCantidadPixeles = new JLabel("");
		resultadoCantidadPixeles.setVisible(false);
		
		botonSegmentar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				SegmentacionDialog m = new SegmentacionDialog(VentanaPrincipal.this, contentPane);
				m.setVisible(true);
			}
		});
		botonSegmentar.setEnabled(false);
		panelPromedios.add(botonSegmentar);
		panelPromedios.add(cantidadPixeles);
		panelPromedios.add(resultadoCantidadPixeles);
		
		final JButton botonPromedio = new JButton("Valores Promedio:");
		botonPromedio.setVisible(false);
		final JLabel labelPromedioGrises = new JLabel("Niveles de Gris:");
		labelPromedioGrises.setVisible(false);
		
		final JLabel labelResultadoPromedioRojo = new JLabel("");
		labelResultadoPromedioRojo.setVisible(false);
		
		final JLabel labelResultadoPromedioVerde = new JLabel("");
		labelResultadoPromedioVerde.setVisible(false);
		
		final JLabel labelResultadoPromedioAzul = new JLabel("");
		labelResultadoPromedioAzul.setVisible(false);
		
		
		panelPromedios.add(botonPromedio, BorderLayout.PAGE_END);
		panelPromedios.add(labelPromedioGrises, BorderLayout.PAGE_END);
		panelPromedios.add(labelResultadoPromedioRojo, BorderLayout.PAGE_END);
		panelPromedios.add(labelResultadoPromedioVerde, BorderLayout.PAGE_END);
		panelPromedios.add(labelResultadoPromedioAzul, BorderLayout.PAGE_END);
		
		contentPane.add(panelPromedios, BorderLayout.PAGE_END);
		
		
		panelPromedios.add(labelPromedioGrises);
		panelPromedios.setVisible(true);
		
		botonPromedio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Imagen imagen = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage imagenActual = imagen.getBufferedImage(); 
				int[] promedios = ProcesadorDeImagenes.obtenerInstancia().calcularValoresPromedio(imagenActual, imagenActual.getWidth(), imagenActual.getHeight());
				
					labelResultadoPromedioRojo.setVisible(true);
					labelResultadoPromedioRojo.setText("Rojo: " + String.valueOf(promedios[0]));
					labelResultadoPromedioVerde.setVisible(true);
					labelResultadoPromedioVerde.setText("Verde: " + String.valueOf(promedios[1]));
					labelResultadoPromedioAzul.setVisible(true);
					labelResultadoPromedioAzul.setText("Azul: " + String.valueOf(promedios[2]));
				}
			
		});
		
		JButton volverALaImagenOriginal = new JButton("Imagen Original");
		volverALaImagenOriginal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Imagen imageOriginal = ProcesadorDeImagenes.obtenerInstancia().getImagenOriginal();
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imageOriginal);
				VentanaPrincipal.this.refrescarImagen();
				VentanaPrincipal.this.refrescarCantidadPixeles(ProcesadorDeImagenes.obtenerInstancia().getImagenOriginal().getBufferedImage().getWidth()*ProcesadorDeImagenes.obtenerInstancia().getImagenOriginal().getBufferedImage().getHeight());
			}
		});
		imagenOriginal.add(volverALaImagenOriginal);
		
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
		
		panelPixel = new JPanel();
		panelPixel.setVisible(false);
		panel.add(panelPixel);
		
		JLabel labelPosicionX = new JLabel("Posici\u00f3n X:");
		panelPixel.add(labelPosicionX);
		
		posicionXTextField = new JTextField();
		panelPixel.add(posicionXTextField);
		posicionXTextField.setColumns(4);
		
		JLabel labelPosicionY = new JLabel("Posici\u00f3n Y:");
		panelPixel.add(labelPosicionY);
		labelPosicionY.setHorizontalAlignment(SwingConstants.TRAILING);
		
		posicionYTextField = new JTextField();
		panelPixel.add(posicionYTextField);
		posicionYTextField.setColumns(4);
		
		JButton btnBuscar = new JButton("Buscar");
		panelPixel.add(btnBuscar);
		
		JLabel labelColorEnPosicion = new JLabel("Color:");
		panelPixel.add(labelColorEnPosicion);
		labelColorEnPosicion.setHorizontalAlignment(SwingConstants.TRAILING);
		
		final JLabel labelColorResultante = new JLabel("");
		panelPixel.add(labelColorResultante);
		
		panelUmbral = new JPanel();
		panelUmbral.setVisible(false);
		panel.add(panelUmbral);
		
		JLabel labelUmbral = new JLabel("Umbral:");
		panelUmbral.add(labelUmbral);
		
		labelColorResultante.addMouseListener(new MostrarTablaDeColoresListener(this));
		
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (ProcesadorDeImagenes.obtenerInstancia().getImagenActual()!=null){
					
					if (!posicionXTextField.getText().trim().isEmpty() && !posicionYTextField.getText().trim().isEmpty()){
						
						try{
							
							Integer x = Integer.valueOf(posicionXTextField.getText().trim());
							Integer y = Integer.valueOf(posicionYTextField.getText().trim());
							
							int color = ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage().getRGB(x, y);
							
							String colorHexa = ColorManager.getHexaDeColorRGB(color);
							labelColorResultante.setText(colorHexa);
							labelColorResultante.setBackground(Color.decode(colorHexa));
							labelColorResultante.setOpaque(true);
						} catch (Exception e) {
							
							DialogsHelper.mostarMensaje(contentPane, "Por favor ingresa una posici\u00f3n v\u00e1lida", NivelMensaje.ERROR);
						}
					} else {
						
						DialogsHelper.mostarMensaje(contentPane, "Por favor completa la posici\u00f3n del pixel a buscar", NivelMensaje.ERROR);
					}
					
				} else {
					
					DialogsHelper.mostarMensaje(contentPane, "Por favor elija una imagen para comenzar");
				}
			}
		});
		
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
					int cantidadPixeles = alto*ancho;
					refrescarCantidadPixeles(cantidadPixeles);
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
		
		JMenuItem menuEditarVideo = new JMenuItem("Editar Video");
		menuEditarVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				VentanaVideo video = new VentanaVideo();
				video.setVisible(true);
			}
		});
		//menu.add(menuEditarVideo);
		menu.add(menuItem);
		
		menuItemEditar = new JMenu("Editar");
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
		
		
		menuItemRecortarImagen = new JMenuItem("Recortar Imagen");
		menuItemRecortarImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				DialogsHelper.mostarMensaje(contentPane, "Cliquea en la esquina superior izquierda y la inferior derecha que formar\u00e1n el cuadrado para recortar la imagen");
				labelPrincipal.addMouseListener(new RecortarImagenListener(VentanaPrincipal.this));
			}
		});
		//menuItemEditar.add(menuItemRecortarImagen);
		
		
		
		JMenuItem menuItemHistogramas = new JMenuItem("Histogramas");
		menuItemHistogramas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				if ( imagenActual!=null ){
					
					VentanaHistogramas ventanaHistogramas = new VentanaHistogramas(imagenActual, true);
					ventanaHistogramas.setVisible(true);
				}
			}
		});
		
		JMenuItem menuItemSift = new JMenuItem("Sift");
		menuItemSift.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {

					SiftDialog dialogo = new SiftDialog();
					dialogo.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					DialogsHelper.mostarMensaje(contentPane, "Ocurri\u00f3 un error aplicando el m\u00e9todo Sift. Intenta con otra imagen", NivelMensaje.ERROR);
				} 
			}
		});
		//menuItemEditar.add(menuItemSift);
		//menuItemEditar.add(menuItemHistogramas);
		
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
		
		
		JMenuItem menuItemNegativo = new JMenuItem("Negativo");
		menuItemNegativo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				
				if (imagenActual!=null){
					
					Imagen negativo = ProcesadorDeImagenes.obtenerInstancia().aplicarNegativo(imagenActual);
					if (negativo!=null){
						
						VentanaPrincipal.this.refrescarImagen();
					} else {
						
						DialogsHelper.mostarMensaje(contentPane, "Error al aplicar el filtro", NivelMensaje.ERROR);
					}
				}
			}
		});
		
		JMenuItem menuItemUmbralizar = new JMenuItem("Umbralizar");
		
		umbralSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 147);
		//Turn on labels at major tick marks.
		umbralSlider.setMajorTickSpacing(51);
		umbralSlider.setMinorTickSpacing(51);
		umbralSlider.setPaintTicks(true);
		umbralSlider.setPaintLabels(true);
		
		panelUmbral.add(umbralSlider);
		
		JButton botonFinalizarEditado = new JButton("Finalizar");
		botonFinalizarEditado.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				panelUmbral.setVisible(false);
				menu.setEnabled(true);
				menuItemEditar.setEnabled(true);
				menuItemTemplates.setEnabled(true);
			}
		});
		panelUmbral.add(botonFinalizarEditado);

		menuItemUmbralizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				VentanaPrincipal.this.setExtendedState(VentanaPrincipal.this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
				panelUmbral.setVisible(true);
				menu.setEnabled(false);
				menuItemEditar.setEnabled(false);
				menuItemTemplates.setEnabled(false);
				
				UmbralListener listener = new UmbralListener(ProcesadorDeImagenes.obtenerInstancia().getImagenActual(), VentanaPrincipal.this);

				if (umbralSlider.getChangeListeners().length >0){
					
					menuItemGuardarComo.removeChangeListener(umbralSlider.getChangeListeners()[0]);
				}
				umbralSlider.addChangeListener(listener);
			}
		});
		//menuFiltros.add(menuItemUmbralizar);
		//menuFiltros.add(menuItemNegativo);
		
		JMenuItem menuItemCompresionDeRangoDinamico = new JMenuItem("Compresi\u00f3n de Rango Din\u00E1mico");
		menuItemCompresionDeRangoDinamico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage imagenTransformada = ProcesadorDeImagenes.obtenerInstancia().aplicarTransformacionLogaritmica(imagenActual.getBufferedImage());
				Imagen nuevaImagenActual = new Imagen(imagenTransformada, imagenActual.getFormato(), imagenActual.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		
		
		JMenuItem menuItemUmbralGlobal = new JMenuItem("Umbral Global");
		menuItemUmbralGlobal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ProcesadorDeImagenes.obtenerInstancia().encontrarUmbralGlobal(VentanaPrincipal.this, 150);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		
		JMenuItem menuItemContrasteConFactor = new JMenuItem("Aumento de contraste con factor");
		menuItemContrasteConFactor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				ProcesadorDeImagenes.obtenerInstancia().aumentarContrastePorFactor(imagenActual);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		//menuFiltros.add(menuItemContrasteConFactor);
		//menuFiltros.add(menuItemCompresionDeRangoDinamico);
		//menuFiltros.add(menuItemUmbralGlobal);
		
		JMenuItem menuItemUmbralOtsu = new JMenuItem("Umbral Otsu");
		menuItemUmbralOtsu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int umbralOtsu = Umbralizador.generarUmbralizacionOtsu(ProcesadorDeImagenes.obtenerInstancia().getImagenActual(), Canal.ROJO, true);
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(Umbralizador.umbralizarImagen(ProcesadorDeImagenes.obtenerInstancia().getImagenActual(), umbralOtsu));
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		//menuFiltros.add(menuItemUmbralOtsu);
		
		JMenuItem menuItemOtsuColor = new JMenuItem("Otsu Color");
		menuItemOtsuColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Imagen imagenUmbralizada = Umbralizador.generarUmbralizacionColor(ProcesadorDeImagenes.obtenerInstancia().getImagenActual());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenUmbralizada);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		//menuFiltros.add(menuItemOtsuColor);
		
		menuItemTemplates = new JMenu("Plantillas");
		//menuBar.add(menuItemTemplates);
		
		JMenuItem menuItemImagenConCuadrado = new JMenuItem("Imagen con Cuadrado (200x200)");
		menuItemImagenConCuadrado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				botonSeleccionar.setVisible(false);
				cantidadPixeles.setVisible(false);
				resultadoCantidadPixeles.setVisible(false);
				botonPromedio.setVisible(false);
				BufferedImage buf = Graficador.crearImagenConCuadradoEnElCentro(200, 200, 40);
				Imagen imagenConCuadrado = new Imagen(buf, FormatoDeImagen.JPG, "Imagen Con Cuadrado", new int[buf.getWidth()][buf.getHeight()], new int[buf.getWidth()][buf.getHeight()], new int[buf.getWidth()][buf.getHeight()]);
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenConCuadrado);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		menuItemTemplates.add(menuItemImagenConCuadrado);
		
		JMenuItem menuItemImagenConCirculo = new JMenuItem("Imagen con C\u00EDrculo (200x200)");
		menuItemImagenConCirculo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				botonSeleccionar.setVisible(false);
				cantidadPixeles.setVisible(false);
				resultadoCantidadPixeles.setVisible(false);
				botonPromedio.setVisible(false);
				BufferedImage buf = Graficador.crearImagenConCirculoEnElMedio(200,200,40);
				Imagen imagenConCirculo = new Imagen(buf, FormatoDeImagen.JPG, "Imagen Con C\u00edrculo", new int[buf.getWidth()][buf.getHeight()], new int[buf.getWidth()][buf.getHeight()], new int[buf.getWidth()][buf.getHeight()]);
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenConCirculo);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		menuItemTemplates.add(menuItemImagenConCirculo);
		
		JMenuItem menuItemDegradeDeGrises = new JMenuItem("Degrad\u00E9 de grises (200x250)");
		menuItemDegradeDeGrises.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				botonSeleccionar.setVisible(false);
				cantidadPixeles.setVisible(false);
				resultadoCantidadPixeles.setVisible(false);
				botonPromedio.setVisible(false);
				BufferedImage buf = Graficador.crearImagenConDegradeDeGrises(200, 250);
				Imagen degrade = new Imagen(buf, FormatoDeImagen.JPG, "Degrad\u00e9 de grises", new int[buf.getWidth()][buf.getHeight()], new int[buf.getWidth()][buf.getHeight()], new int[buf.getWidth()][buf.getHeight()]);
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(degrade);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		menuItemTemplates.add(menuItemDegradeDeGrises);
		
		JMenuItem menuItemDegradeColor = new JMenuItem("Degrad\u00E9 de color (200x250)");
		menuItemDegradeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				botonSeleccionar.setVisible(false);
				cantidadPixeles.setVisible(false);
				resultadoCantidadPixeles.setVisible(false);
				botonPromedio.setVisible(false);
				BufferedImage buf = Graficador.crearImagenConDegradeColor(200, 250);
				Imagen degrade = new Imagen(buf, FormatoDeImagen.JPG, "Degrad\u00e9 de color", new int[buf.getWidth()][buf.getHeight()], new int[buf.getWidth()][buf.getHeight()], new int[buf.getWidth()][buf.getHeight()]);
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(degrade);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		menuItemTemplates.add(menuItemDegradeColor);
		
		JMenuItem menuItemPromedioGrises = new JMenuItem("Valores Promedio");
		menuItemPromedioGrises.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaPrincipal.this.setExtendedState(VentanaPrincipal.this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
				
				botonSeleccionar.setVisible(true);
				cantidadPixeles.setVisible(true);
				resultadoCantidadPixeles.setVisible(true);
				botonPromedio.setVisible(true);
				}
			
		});
		//menuItemEditar.add(menuItemPromedioGrises);
		
		
		JMenuItem menuItemOperacionesMatrices = new JMenuItem("Operaciones");
		menuItemOperacionesMatrices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OperacionesMatricesDialog m = new OperacionesMatricesDialog(VentanaPrincipal.this);
				m.setVisible(true);
			}
			
		});
		//menuItemEditar.add(menuItemOperacionesMatrices);
		
		
		JMenu menuDeteccionDeBordes = new JMenu("Detecci\u00f3n de Bordes");
		menuItemEditar.add(menuDeteccionDeBordes);
		
		
		JMenuItem menuItemAlgoritmoComparativo = new JMenuItem("Algoritmo comparativo de detectores");
		menuItemAlgoritmoComparativo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				AlgoritmoComparativoDialog dialog = new AlgoritmoComparativoDialog(VentanaPrincipal.this, contentPane);
				dialog.setVisible(true);
			}
		});
		//menuDeteccionDeBordes.add(menuItemAlgoritmoComparativo);
		
		menuBar.add(menuItemAlgoritmoComparativo);
		
		
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

			        	Imagen imagenConHarris = DetectorDeHarris.detectarEsquinas(ProcesadorDeImagenes.obtenerInstancia().getImagenActual(), true);
						ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenConHarris);
						DetectorDeHarris.setResultadosX(new LinkedList<Integer>());
						DetectorDeHarris.setResultadosY(new LinkedList<Integer>());
						refrescarImagen();
						
						return null;
			         }
			      };

			      mySwingWorker.execute();
			      mostrarDialogoDeEspera();
			}
		});
		
		JMenuItem menuItemTransfoHough = new JMenuItem("Transformada de hough");
		menuItemTransfoHough.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				HoughDialog d = new HoughDialog(VentanaPrincipal.this);
				d.setVisible(true);
			}
		});
		//menuDeteccionDeBordes.add(menuItemTransfoHough);
		
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
				
				//aplicarDoG(1, 2, 3, 4);
				DiferenciaDeGaussianasDialog d = new DiferenciaDeGaussianasDialog(VentanaPrincipal.this, contentPane);
				d.setVisible(true);
			}
		});
		menuDeteccionDeBordes.add(menuItemDoG);
		
		
		menuDeteccionDeBordes.add(menuItemHarris);
		menuDeteccionDeBordes.add(menuItemDetectorDeSusan);
		
		JMenu menuCanny = new JMenu("Detector de Canny");
		//menuDeteccionDeBordes.add(menuCanny);
		
		JMenuItem mntmSupresionNoMaximosItem = new JMenuItem("Supresi\u00F3n No M\u00E1ximos");
		mntmSupresionNoMaximosItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				Imagen nuevaImagenActual = DetectorDeBordesDeCanny.mostrarImagenNoMaximos(imagenAnterior);
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		menuCanny.add(mntmSupresionNoMaximosItem);
		
		JMenuItem menuItemHisteresis = new JMenuItem("Umbralizaci\u00F3n con Hist\u00E9resis");
		menuItemHisteresis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				HisteresisDialog dialog = new HisteresisDialog(VentanaPrincipal.this, contentPane);
				dialog.setVisible(true);
			}
		});
		menuCanny.add(menuItemHisteresis);
		
		JMenuItem menuItemDetectorCanny = new JMenuItem("Aplicar Detector de Canny");
		menuItemDetectorCanny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				DetectorDeCannyDialog dialog = new DetectorDeCannyDialog(VentanaPrincipal.this, contentPane);
				dialog.setVisible(true);
			}
		});
		menuCanny.add(menuItemDetectorCanny);
		
		JMenu menuDeteccionDePrewitt = new JMenu("Detector De Prewitt");
		//menuDeteccionDeBordes.add(menuDeteccionDePrewitt);
		
		JMenuItem menuItemDetectorDePrewitt = new JMenuItem("Aplicar");
		menuItemDetectorDePrewitt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.aplicarDetectorDePrewitt(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		menuDeteccionDePrewitt.add(menuItemDetectorDePrewitt);
		
		
		JMenuItem menuItemMascaraEnXPrewitt = new JMenuItem("Mostrar m\u00e1scara en X");
		menuItemMascaraEnXPrewitt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraDePrewittEnX(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		menuDeteccionDePrewitt.add(menuItemMascaraEnXPrewitt);
		
		
		JMenuItem menuItemMascaraEnYPrewitt = new JMenuItem("Mostrar m\u00e1scara en Y");
		menuItemMascaraEnYPrewitt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraDePrewittEnY(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		menuDeteccionDePrewitt.add(menuItemMascaraEnYPrewitt);
		
		
		JMenu menuDeteccionDeSobel = new JMenu("Detector De Sobel");
		//menuDeteccionDeBordes.add(menuDeteccionDeSobel);
		
		JMenuItem menuItemDetectorDeSobel = new JMenuItem("Aplicar");
		menuItemDetectorDeSobel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.aplicarDetectorDeSobel(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		menuDeteccionDeSobel.add(menuItemDetectorDeSobel);
		
		JMenuItem menuItemMascaraEnXSobel = new JMenuItem("Mostrar m\u00e1scara en X");
		menuItemMascaraEnXSobel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraDeSobelEnX(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		menuDeteccionDeSobel.add(menuItemMascaraEnXSobel);
		
		
		JMenuItem menuItemMascaraEnYSobel = new JMenuItem("Mostrar m\u00e1scara en Y");
		menuItemMascaraEnYSobel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraDeSobelEnY(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		menuDeteccionDeSobel.add(menuItemMascaraEnYSobel);
		
		
		JMenu menuDeteccionLaplaciano = new JMenu("Detector Laplaciano");
		//menuDeteccionDeBordes.add(menuDeteccionLaplaciano);
		
		JMenuItem menuItemMostrarMascaraLaplaciano = new JMenuItem("Mostrar M\u00e1scara");
		menuItemMostrarMascaraLaplaciano.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraDeLaplaciano(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		
		JMenuItem menuItemAplicarDetectorLaplaciano = new JMenuItem("Aplicar");
		menuItemAplicarDetectorLaplaciano.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.aplicarDetectorLaplaciano(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		
		JMenuItem menuItemMostrarCrucesPorCero = new JMenuItem("Mostrar Cruces Por Cero");
		menuItemMostrarCrucesPorCero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraCrucesPorCeros(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		
		menuDeteccionLaplaciano.add(menuItemMostrarMascaraLaplaciano);
		menuDeteccionLaplaciano.add(menuItemMostrarCrucesPorCero);
		menuDeteccionLaplaciano.add(menuItemAplicarDetectorLaplaciano);
		
		
		JMenu menuDeteccionLaplacianoDelGaussiano = new JMenu("Detector Laplaciano del Gaussiano");
		//menuDeteccionDeBordes.add(menuDeteccionLaplacianoDelGaussiano);
		
		JMenuItem menuItemAplicarDetectorLaplacianoDelGaussiano = new JMenuItem("Aplicar");
		menuItemAplicarDetectorLaplacianoDelGaussiano.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				LoGDialog dialogo = new LoGDialog(VentanaPrincipal.this);
				dialogo.setVisible(true);
			}
		});
		
		JMenuItem menuItemMostrarMascaraLaplacianoDelGaussiano = new JMenuItem("Mostrar M\u00e1scara");
		menuItemMostrarMascaraLaplacianoDelGaussiano.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraLaplacianoDelGaussiano(imagenAnterior, 40);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		
		menuDeteccionLaplacianoDelGaussiano.add(menuItemAplicarDetectorLaplacianoDelGaussiano);
		
		JMenuItem menuItemMostrarMascaraLoG = new JMenuItem("Mostrar m\u00E1scara");
		menuItemMostrarMascaraLoG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SigmaDialog dialogo = new SigmaDialog(VentanaPrincipal.this);
				dialogo.setVisible(true);
			}
		});
		menuDeteccionLaplacianoDelGaussiano.add(menuItemMostrarMascaraLoG);

		
		JMenu menuDeteccionDeBordesDireccionales = new JMenu("Detecci\u00f3n de Bordes Direccionales");
		//menuItemEditar.add(menuDeteccionDeBordesDireccionales);
		
		JMenuItem menuItemPrewittDireccional = new JMenuItem("Aplicar Prewitt Direccional");
		menuItemPrewittDireccional.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectarBordesDireccionales.aplicarDetectorDeBordesDireccional(imagenAnterior, "Prewitt");
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		menuDeteccionDeBordesDireccionales.add(menuItemPrewittDireccional);
		
		JMenuItem menuItemSobelDireccional = new JMenuItem("Aplicar Sobel Direccional");
		menuItemSobelDireccional.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectarBordesDireccionales.aplicarDetectorDeBordesDireccional(imagenAnterior, "Sobel");
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		menuDeteccionDeBordesDireccionales.add(menuItemSobelDireccional);
		
		JMenuItem menuItemKirshDireccional = new JMenuItem("Aplicar Kirsh Direccional");
		menuItemKirshDireccional.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectarBordesDireccionales.aplicarDetectorDeBordesDireccional(imagenAnterior, "Kirsh");
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		menuDeteccionDeBordesDireccionales.add(menuItemKirshDireccional);
		
		JMenuItem menuItemNuevaDireccional = new JMenuItem("Aplicar Nueva Direccional");
		menuItemNuevaDireccional.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectarBordesDireccionales.aplicarDetectorDeBordesDireccional(imagenAnterior, "Nueva");
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaPrincipal.this.refrescarImagen();
			}
			
		});
		menuDeteccionDeBordesDireccionales.add(menuItemNuevaDireccional);
		
		JMenu menuDifusion = new JMenu("Difusi\u00f3n");
		//menuItemEditar.add(menuDifusion);
		
		JMenuItem menuItemDifusionIsotropica = new JMenuItem("Aplicar Difusi\u00f3n Isotr\u00f3pica");
		menuItemDifusionIsotropica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DifusionIsotropicaDialog dialogo = new DifusionIsotropicaDialog(VentanaPrincipal.this);
				dialogo.setVisible(true);
			}
			
		});
		menuDifusion.add(menuItemDifusionIsotropica);
		
		
		JMenuItem menuItemDifusionAnisotropica = new JMenuItem("Aplicar Difusi\u00f3n Anisotr\u00f3pica");
		menuItemDifusionAnisotropica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DifusionAnisotropicaDialog dialogo = new DifusionAnisotropicaDialog(VentanaPrincipal.this);
				dialogo.setVisible(true);
			}
			
		});
		menuDifusion.add(menuItemDifusionAnisotropica);
		
	}

	public void aplicarLaplacianoDelGaussiano(int sigma, int umbral, int longitudMascara) {
		
		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage bufferedImage = DetectorDeBordes.aplicarDetectorLaplacianoDelGaussiano(imagenAnterior, sigma, umbral, longitudMascara);
		Imagen nuevaImagenActual = new Imagen(bufferedImage,
				imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(
				nuevaImagenActual);

		VentanaPrincipal.this.refrescarImagen();
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
	
	
	
	public void umbralizarConHisteresis(int umbral1, int umbral2) {
		
		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		MatrizDeColores matrizDeColores = DetectorDeBordesDeCanny.calcularSupresionNoMaximos(imagenAnterior);
		
		int[][] matrizRojos = matrizDeColores.getMatrizRojos();
		int[][] matrizVerdes = matrizDeColores.getMatrizVerdes();
		int[][] matrizAzules = matrizDeColores.getMatrizAzules();
		
		int[][] matrizRojoTrasnpuesta = new int[matrizRojos[0].length][matrizRojos.length];
		int[][] matrizVerdeTranspuesta = new int[matrizRojos[0].length][matrizRojos.length];
		int[][] matrizAzulTranspuesta = new int[matrizRojos[0].length][matrizRojos.length];
		
		
		   for(int j = 0; j < matrizRojos.length; j++){
	           for(int i = 0; i < matrizRojos[0].length; i++){
	        	   matrizRojoTrasnpuesta[i][j] = matrizRojos[j][i];
	        	   matrizVerdeTranspuesta[i][j] = matrizVerdes[j][i];
	        	   matrizAzulTranspuesta[i][j] = matrizAzules[j][i];
	           }
	        }
		
		int[][] matrizHisteresisRojo = MatricesManager.aplicarTransformacionLineal(DetectorDeBordesDeCanny.aplicarUmbralizacionConHisteresis(matrizRojoTrasnpuesta, umbral1, umbral2));
		int[][] matrizHisteresisVerde = MatricesManager.aplicarTransformacionLineal(DetectorDeBordesDeCanny.aplicarUmbralizacionConHisteresis(matrizVerdeTranspuesta, umbral1, umbral2));
		int[][] matrizHisteresisAzul = MatricesManager.aplicarTransformacionLineal(DetectorDeBordesDeCanny.aplicarUmbralizacionConHisteresis(matrizAzulTranspuesta, umbral1, umbral2));
		
		BufferedImage bufferedNuevo = MatricesManager.obtenerImagenDeMatrices(matrizHisteresisRojo, matrizHisteresisVerde, matrizHisteresisAzul);
		Imagen imagenNueva = new Imagen(bufferedNuevo, imagenAnterior.getFormato(), imagenAnterior.getNombre()+"_histeresis");
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenNueva);
		
		VentanaPrincipal.this.refrescarImagen();
	}
	
	public void aplicarDetectorCanny(int umbral1, int umbral2, int sigma1, int sigma2) {
		
		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
	
		int[][] matrizRojos1 = imagenAnterior.getMatriz(Canal.ROJO);
		int[][] matrizVerdes1 = imagenAnterior.getMatriz(Canal.VERDE);
		int[][] matrizAzules1 = imagenAnterior.getMatriz(Canal.AZUL);

		int[][] matrizRojoTranspuesta1 = new int[matrizRojos1[0].length][matrizRojos1.length];
		int[][] matrizVerdeTranspuesta1 = new int[matrizRojos1[0].length][matrizRojos1.length];
		int[][] matrizAzulTranspuesta1 = new int[matrizRojos1[0].length][matrizRojos1.length];
	
		   for(int j = 0; j < matrizRojos1.length; j++){
	           for(int i = 0; i < matrizRojos1[0].length; i++){
	        	   matrizRojoTranspuesta1[i][j] = matrizRojos1[j][i];
	        	   matrizVerdeTranspuesta1[i][j] = matrizVerdes1[j][i];
	        	   matrizAzulTranspuesta1[i][j] = matrizAzules1[j][i];
	           }
	        }
		   
		BufferedImage buffer1 = MatricesManager.obtenerImagenDeMatrices(matrizRojoTranspuesta1, matrizVerdeTranspuesta1, matrizAzulTranspuesta1); 
		Imagen imagen1 = new Imagen(buffer1, imagenAnterior.getFormato(), imagenAnterior.getNombre()+"Transpuesta");
		
		Imagen imagenFinal = DetectorDeBordesDeCanny.aplicarDetectorDeCanny(imagen1, sigma1, sigma2, umbral1, umbral2);		
		Imagen imagenNueva = new Imagen(imagenFinal.getBufferedImage(), imagenAnterior.getFormato(), imagenAnterior.getNombre()+"_canny");
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenNueva);
		
		VentanaPrincipal.this.refrescarImagen();
	}
	
	private void cargarImagen(JLabel labelPrincipal,
			JMenuItem menuItemGuardarComo) {
		
		Imagen imagenElegida = ProcesadorDeImagenes.obtenerInstancia()
				.cargarUnaImagenDesdeArchivo();
		if(imagenElegida != null){
			int cantidadPixeles = imagenElegida.getBufferedImage().getWidth()
					* imagenElegida.getBufferedImage().getHeight();
			refrescarCantidadPixeles(cantidadPixeles);
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

	public void cambiarColorDePixel(int rgb) {

		int posicionX = Integer.valueOf(posicionXTextField.getText());
		int posicionY = Integer.valueOf(posicionYTextField.getText());

		Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia()
				.getImagenActual();
		imagenActual.getBufferedImage().setRGB(posicionX, posicionY, rgb);
		labelPrincipal.setIcon(new ImageIcon(imagenActual.getBufferedImage()));
	}

	private void actualizarPanelDeImagen(final JMenuItem menuItemGuardarComo,
			Imagen imagenElegida) {
		if (imagenElegida != null) {

			labelPrincipal.setIcon(new ImageIcon(imagenElegida
					.getBufferedImage()));
			menuItemEditar.setEnabled(true);
			panelPixel.setVisible(true);
		}

		chequearGuardarComo(menuItemGuardarComo);
	}

	public void refrescarImagen() {

		Imagen imagen = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		labelPrincipal.setIcon(new ImageIcon(imagen.getBufferedImage()));
		chequearGuardarComo(menuItemGuardarComo);
		ocultarDialogoDeEspera();
	}

	public void refrescarCantidadPixeles(int cantidadPixeles) {
		resultadoCantidadPixeles.setText(String.valueOf(cantidadPixeles));
	}

	public void mostrarImagenMarcada() {

		Graphics g = super.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.GREEN);

		Integer[] coordenadas = ProcesadorDeImagenes.obtenerInstancia()
				.getXEY();
		int x1 = coordenadas[0];
		int x2 = coordenadas[1];
		int y1 = coordenadas[2];
		int y2 = coordenadas[3];
		g2.drawRect(x1, x1, x2 - x1, y2 - y1);

	}

	public void obtenerMatrizResultanteDeSuma(Imagen primeraImagen,
			Imagen segundaImagen) {

		OperacionesManager.aplicarOperacionMatematica(VentanaPrincipal.this,
				contentPane, primeraImagen, segundaImagen,
				OperacionMatematica.SUMA);
	}

	public void obtenerMatrizResultanteDeResta(Imagen primeraImagen,
			Imagen segundaImagen) {

		OperacionesManager.aplicarOperacionMatematica(VentanaPrincipal.this,
				contentPane, primeraImagen, segundaImagen,
				OperacionMatematica.RESTA);
	}

	public void obtenerMatrizResultanteDeMultiplicar(Imagen primeraImagen,
			Imagen segundaImagen) {

		OperacionesManager.aplicarOperacionMatematica(VentanaPrincipal.this,
				contentPane, primeraImagen, segundaImagen,
				OperacionMatematica.MULTIPLICACION);
	}

	public void obtenerMatrizResultanteDeSumaEscalar(int escalar) {

		OperacionesManager.operarConUnEscalar(VentanaPrincipal.this,
				contentPane, escalar, OperacionMatematica.SUMA);
	}

	public void obtenerMatrizResultanteDeRestaEscalar(int escalar) {

		OperacionesManager.operarConUnEscalar(VentanaPrincipal.this,
				contentPane, escalar, OperacionMatematica.RESTA);
	}

	public void obtenerMatrizResultanteDeMultiplicarPorEscalar(int escalar) {

		OperacionesManager.operarConUnEscalar(VentanaPrincipal.this,
				contentPane, escalar, OperacionMatematica.MULTIPLICACION);
	}

	public void mostrarMascaraLaplacianoDelGaussiano(int sigma) {

		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraLaplacianoDelGaussiano(imagenAnterior, sigma);
		Imagen nuevaImagenActual = new Imagen(bufferedImage,
				imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(
				nuevaImagenActual);

		VentanaPrincipal.this.refrescarImagen();
	}
	
	public void aplicarDifusionIsotropica(int repeticiones) {

		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage bufferedImage = Difuminador.aplicarDifusion(imagenAnterior, null, repeticiones, true);
		Imagen nuevaImagenActual = new Imagen(bufferedImage,
				imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(
				nuevaImagenActual);

		VentanaPrincipal.this.refrescarImagen();
	}
	
	public void aplicarDifusionAnisotropica(int repeticiones, InterfaceDetectorDeBordes detectorDeBordes) {

		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage bufferedImage = imagenAnterior.getBufferedImage();
		
		bufferedImage = Difuminador.aplicarDifusion(imagenAnterior, detectorDeBordes, repeticiones, false);
	
		Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);

		VentanaPrincipal.this.refrescarImagen();
	}
	
	public void segmentarImagen(int repeticiones, int diferenciaColor) {
		
		ProcesadorDeImagenes procesador = ProcesadorDeImagenes.obtenerInstancia();
		
		BufferedImage imagenSinCambio = procesador.clonarBufferedImage(procesador.getImagenOriginal().getBufferedImage()); 
		
		BufferedImage bufferSegmentado = Segmentador.segmentarImagenPrimeraVez(procesador.getImagenOriginal(), 
				new Point(procesador.getX1(), procesador.getY1()), 
				new Point(procesador.getX2(), procesador.getY2()), repeticiones, diferenciaColor);
		
		Imagen imagenSegmentada = new Imagen(bufferSegmentado, procesador.getImagenActual().getFormato(), procesador.getImagenActual().getNombre()+"_segmentada");
		procesador.setImagenActual(imagenSegmentada);
		procesador.getImagenOriginal().setBufferedImage(imagenSinCambio);
		VentanaPrincipal.this.refrescarImagen();
		VentanaPrincipal.this.refrescarCantidadPixeles(procesador.getImagenOriginal().getBufferedImage().getWidth()*procesador.getImagenOriginal().getBufferedImage().getHeight());
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
	
	public void aplicarTransformadaDeHough(int titaMin, int titaMax,
			int discTita, int roMin, int roMax, int discRo, int umbral) {

		ProcesadorDeImagenes proc = ProcesadorDeImagenes.obtenerInstancia();
		BufferedImage imagenAnterior = proc.clonarBufferedImage(proc.getImagenActual().getBufferedImage());
		TransformadaDeHough.aplicarTransformadaDeHough(ProcesadorDeImagenes.obtenerInstancia().getImagenActual(), 
				titaMin, titaMax, discTita, roMin, roMax, discRo, umbral, VentanaPrincipal.this);
		proc.setImagenOriginal(new Imagen(imagenAnterior, proc.getImagenActual().getFormato(), proc.getImagenActual().getNombre()));
	}
	
	
	public void aplicarFiltroGaussiano(int sigmaElegido) {
		ProcesadorDeImagenes proc = ProcesadorDeImagenes.obtenerInstancia();
		Imagen imagenAnterior = proc.getImagenActual();
		Imagen imagenResultante = FiltroGaussiano.aplicarFiltroGaussiano(imagenAnterior, sigmaElegido);
		proc.setImagenOriginal(new Imagen(imagenAnterior.getBufferedImage(), proc.getImagenActual().getFormato(), proc.getImagenActual().getNombre()));
		proc.setImagenActual(imagenResultante);
		VentanaPrincipal.this.refrescarImagen();
	}
	
	
	public void ejecutarAlgoritmoComparativo(final String coleccion, final int sigma) throws IOException{
		
		AlgoritmoComparativoDeDetectores.aplicarAlgoritmo(ProcesadorDeImagenes.obtenerInstancia().getImagenActual(), coleccion, sigma);

		//ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenConHarris);
		//refrescarImagen();
	}
	
	
	public void mostrarDialogoDeEspera(){
		
		this.dialogoEspera.mostrar();
	}
	
	public void ocultarDialogoDeEspera(){
		
		this.dialogoEspera.ocultar();
	}

}
