package ar.com.untref.imagenes.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

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
import javax.swing.border.EmptyBorder;

import ar.com.untref.imagenes.dialogs.OperacionesMatricesDialog;
import ar.com.untref.imagenes.enums.FormatoDeImagen;
import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.listeners.GuardarComoListener;
import ar.com.untref.imagenes.listeners.MostrarTablaDeColoresListener;
import ar.com.untref.imagenes.listeners.RecortarImagenListener;
import ar.com.untref.imagenes.listeners.UmbralListener;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.ColorManager;
import ar.com.untref.imagenes.procesamiento.Graficador;
import ar.com.untref.imagenes.procesamiento.MatricesManager;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;

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


	public VentanaPrincipal() {

		this.setTitle("Procesamiento de Imágenes");
				
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
		
		ImageIcon iconoSubirArchivo = new ImageIcon("resources/upload.png");
		labelPrincipal = new JLabel(iconoSubirArchivo, JLabel.CENTER);
		scrollPane.setViewportView(labelPrincipal);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JPanel panelRaw = new JPanel();
		panel.add(panelRaw);
		
		final JPanel panelPromedios = new JPanel();
		final JPanel imagenOriginal = new JPanel();
		panelPromedios.add(imagenOriginal);
		final JLabel cantidadPixeles = new JLabel("Cantidad de Pixeles:");
		cantidadPixeles.setVisible(false);
		resultadoCantidadPixeles = new JLabel("");
		resultadoCantidadPixeles.setVisible(false);
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
			}
		});
		imagenOriginal.add(volverALaImagenOriginal);
		
		JLabel labelTamañoRAW = new JLabel("Dimensiones RAW");
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
		
		panelPixel = new JPanel();
		panelPixel.setVisible(false);
		panel.add(panelPixel);
		
		JLabel labelPosicionX = new JLabel("Posicion X:");
		panelPixel.add(labelPosicionX);
		
		posicionXTextField = new JTextField();
		panelPixel.add(posicionXTextField);
		posicionXTextField.setColumns(4);
		
		JLabel labelPosicionY = new JLabel("Posicion Y:");
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
							
							DialogsHelper.mostarMensaje(contentPane, "Por favor ingresa una posición válida", NivelMensaje.ERROR);
						}
					} else {
						
						DialogsHelper.mostarMensaje(contentPane, "Por favor completa la posicion del pixel a buscar", NivelMensaje.ERROR);
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
					
					actualizarPanelDeImagen(menuItemGuardarComo, imagenElegida);
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
				
				DialogsHelper.mostarMensaje(contentPane, "Cliquea en la esquina superior izquierda y la inferior derecha que formarán el cuadrado para recortar la imagen");
				labelPrincipal.addMouseListener(new RecortarImagenListener(VentanaPrincipal.this));
			}
		});
		menuItemEditar.add(menuItemRecortarImagen);
		
		JMenuItem menuItemHistogramas = new JMenuItem("Histogramas");
		menuItemHistogramas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				if ( imagenActual!=null ){
					
					VentanaHistogramas ventanaHistogramas = new VentanaHistogramas(imagenActual);
					ventanaHistogramas.setVisible(true);
				}
			}
		});
		menuItemEditar.add(menuItemHistogramas);
		
		JMenu menuFiltros = new JMenu("Filtros");
		menuItemEditar.add(menuFiltros);
		
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
		menuFiltros.add(menuItemUmbralizar);
		menuFiltros.add(menuItemNegativo);
		
		JMenuItem menuItemDuplicarContraste = new JMenuItem("Aumento del Contraste (Fx Cuadrado)");
		menuItemDuplicarContraste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				
				ProcesadorDeImagenes.obtenerInstancia().aumentarContrastePorElCuadrado(imagenActual);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		menuFiltros.add(menuItemDuplicarContraste);
		
		JMenuItem menuItemAumentoContrasteAutomatico = new JMenuItem("Aumento del Contraste Autom\u00E1tico");
		menuItemAumentoContrasteAutomatico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				
				ProcesadorDeImagenes.obtenerInstancia().aumentoContrasteAutomatico(imagenActual);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		menuFiltros.add(menuItemAumentoContrasteAutomatico);
		
		
		JMenuItem menuItemCompresionDeRangoDinamico = new JMenuItem("Compresion de Rango Din\u00E1mico");
		menuItemCompresionDeRangoDinamico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage imagenTransformada = ProcesadorDeImagenes.obtenerInstancia().aplicarTransformacionLogaritmica(imagenActual.getBufferedImage());
				Imagen nuevaImagenActual = new Imagen(imagenTransformada, imagenActual.getFormato(), imagenActual.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		menuFiltros.add(menuItemCompresionDeRangoDinamico);
		
		
		
		menuItemTemplates = new JMenu("Plantillas");
		menuBar.add(menuItemTemplates);
		
		JMenuItem menuItemImagenConCuadrado = new JMenuItem("Imagen con Cuadrado (200x200)");
		menuItemImagenConCuadrado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				cantidadPixeles.setVisible(false);
				resultadoCantidadPixeles.setVisible(false);
				botonPromedio.setVisible(false);
				BufferedImage buf = Graficador.crearImagenConCuadradoEnElCentro(200, 200, 40);
				Imagen imagenConCuadrado = new Imagen(buf, FormatoDeImagen.JPG, "Imagen Con Cuadrado");
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenConCuadrado);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		menuItemTemplates.add(menuItemImagenConCuadrado);
		
		JMenuItem menuItemImagenConCirculo = new JMenuItem("Imagen con C\u00EDrculo (200x200)");
		menuItemImagenConCirculo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				cantidadPixeles.setVisible(false);
				resultadoCantidadPixeles.setVisible(false);
				botonPromedio.setVisible(false);
				BufferedImage buf = Graficador.crearImagenConCirculoEnElMedio(200,200,40);
				Imagen imagenConCirculo = new Imagen(buf, FormatoDeImagen.JPG, "Imagen Con Circulo");
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenConCirculo);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		menuItemTemplates.add(menuItemImagenConCirculo);
		
		JMenuItem menuItemDegradeDeGrises = new JMenuItem("Degrad\u00E9 de grises (200x250)");
		menuItemDegradeDeGrises.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cantidadPixeles.setVisible(false);
				resultadoCantidadPixeles.setVisible(false);
				botonPromedio.setVisible(false);
				BufferedImage buf = Graficador.crearImagenConDegradeDeGrises(200, 250);
				Imagen degrade = new Imagen(buf, FormatoDeImagen.JPG, "Degradé de grises");
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(degrade);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		menuItemTemplates.add(menuItemDegradeDeGrises);
		
		JMenuItem menuItemDegradeColor = new JMenuItem("Degrad\u00E9 de color (200x250)");
		menuItemDegradeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cantidadPixeles.setVisible(false);
				resultadoCantidadPixeles.setVisible(false);
				botonPromedio.setVisible(false);
				BufferedImage buf = Graficador.crearImagenConDegradeColor(200, 250);
				Imagen degrade = new Imagen(buf, FormatoDeImagen.JPG, "Degradé de color");
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(degrade);
				VentanaPrincipal.this.refrescarImagen();
			}
		});
		menuItemTemplates.add(menuItemDegradeColor);
		
		JMenuItem menuItemPromedioGrises = new JMenuItem("Valores Promedio");
		menuItemPromedioGrises.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cantidadPixeles.setVisible(true);
				resultadoCantidadPixeles.setVisible(true);
				botonPromedio.setVisible(true);
				}
			
		});
		menuItemEditar.add(menuItemPromedioGrises);
		
		
		JMenuItem menuItemOperacionesMatrices = new JMenuItem("Operaciones");
		menuItemOperacionesMatrices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OperacionesMatricesDialog m = new OperacionesMatricesDialog(VentanaPrincipal.this);
				m.setVisible(true);
			}
			
		});
		menuItemEditar.add(menuItemOperacionesMatrices);
		
	}
	
	private void cargarImagen(JLabel labelPrincipal,
			JMenuItem menuItemGuardarComo) {
		Imagen imagenElegida = ProcesadorDeImagenes.obtenerInstancia().cargarUnaImagenDesdeArchivo();
		int cantidadPixeles = imagenElegida.getBufferedImage().getWidth()* imagenElegida.getBufferedImage().getHeight();
		refrescarCantidadPixeles(cantidadPixeles);
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

	public void cambiarColorDePixel(int rgb) {

		int posicionX = Integer.valueOf(posicionXTextField.getText());
		int posicionY = Integer.valueOf(posicionYTextField.getText());
		
		Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		imagenActual.getBufferedImage().setRGB(posicionX, posicionY, rgb);
		labelPrincipal.setIcon(new ImageIcon(imagenActual.getBufferedImage()));
	}
	
	private void actualizarPanelDeImagen(
			final JMenuItem menuItemGuardarComo, Imagen imagenElegida) {
		if (imagenElegida!=null){
			
			labelPrincipal.setIcon(new ImageIcon(imagenElegida.getBufferedImage()));
			menuItemEditar.setEnabled(true);
			panelPixel.setVisible(true);
		}
		
		chequearGuardarComo(menuItemGuardarComo);
	}

	public void refrescarImagen() {

		Imagen imagen = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		labelPrincipal.setIcon(new ImageIcon(imagen.getBufferedImage()));
		chequearGuardarComo(menuItemGuardarComo);
	}

	public void refrescarCantidadPixeles(int cantidadPixeles){
		resultadoCantidadPixeles.setText(String.valueOf(cantidadPixeles));
	}
	
	public void obtenerMatrizResultanteDeSuma(Imagen imagen){
		
		int[][] matriz2 = ProcesadorDeImagenes.obtenerInstancia().calcularMatrizDeLaImagen(imagen.getBufferedImage());
		
		BufferedImage bufferedImage = ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage();
		int[][] matriz1 = ProcesadorDeImagenes.obtenerInstancia().calcularMatrizDeLaImagen(bufferedImage);
		
		int[][] matrizResultante = MatricesManager.sumarMatrices(matriz1, matriz2);	    					
		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage imagenResultante = MatricesManager.obtenerImagenDeMatriz(matrizResultante);
		BufferedImage imagenResultanteTransformada = ProcesadorDeImagenes.obtenerInstancia().aplicarTransformacionLineal(imagenResultante);
		Imagen nuevaImagenActual = new Imagen(imagenResultanteTransformada, imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
		VentanaPrincipal.this.refrescarImagen();
	}
	
	public void obtenerMatrizResultanteDeResta(Imagen imagen){
		
		int[][] matriz2 = ProcesadorDeImagenes.obtenerInstancia().calcularMatrizDeLaImagen(imagen.getBufferedImage());
		
		BufferedImage bufferedImage = ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage();
		int[][] matriz1 = ProcesadorDeImagenes.obtenerInstancia().calcularMatrizDeLaImagen(bufferedImage);
		
		int[][] matrizResultante = MatricesManager.restarMatrices(matriz1, matriz2);	    					
		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage imagenResultante = MatricesManager.obtenerImagenDeMatriz(matrizResultante);
		BufferedImage imagenResultanteTransformada = ProcesadorDeImagenes.obtenerInstancia().aplicarTransformacionLineal(imagenResultante);
		Imagen nuevaImagenActual = new Imagen(imagenResultanteTransformada, imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
		VentanaPrincipal.this.refrescarImagen();
	}
	
	public void obtenerMatrizResultanteDeMultiplicar(Imagen imagen){
		
		int[][] matriz2 = ProcesadorDeImagenes.obtenerInstancia().calcularMatrizDeLaImagen(imagen.getBufferedImage());
		
		BufferedImage bufferedImage = ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage();
		int[][] matriz1 = ProcesadorDeImagenes.obtenerInstancia().calcularMatrizDeLaImagen(bufferedImage);
		
		int[][] matrizResultante = MatricesManager.multiplicarMatrices(matriz1, matriz2);	    					
		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage imagenResultante = MatricesManager.obtenerImagenDeMatriz(matrizResultante);
		BufferedImage imagenResultanteTransformada = ProcesadorDeImagenes.obtenerInstancia().aplicarTransformacionLogaritmica(imagenResultante);
		Imagen nuevaImagenActual = new Imagen(imagenResultanteTransformada, imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
		VentanaPrincipal.this.refrescarImagen();
	}
	
	public void obtenerMatrizResultanteDeSumaEscalar(int escalar){
		
		BufferedImage bufferedImage = ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage();
		int[][] matriz = ProcesadorDeImagenes.obtenerInstancia().calcularMatrizDeLaImagen(bufferedImage);
		
		int[][] matrizResultante = MatricesManager.sumarMatrizYEscalar(matriz, escalar);	    					
		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage imagenResultante = MatricesManager.obtenerImagenDeMatriz(matrizResultante);
		BufferedImage imagenResultanteTransformada = ProcesadorDeImagenes.obtenerInstancia().aplicarTransformacionLineal(imagenResultante);
		Imagen nuevaImagenActual = new Imagen(imagenResultanteTransformada, imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
		VentanaPrincipal.this.refrescarImagen();
	}
	
	public void obtenerMatrizResultanteDeRestaEscalar(int escalar){
		
		BufferedImage bufferedImage = ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage();
		int[][] matriz = ProcesadorDeImagenes.obtenerInstancia().calcularMatrizDeLaImagen(bufferedImage);
		
		int[][] matrizResultante = MatricesManager.restarMatrizYEscalar(matriz, escalar);	    					
		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage imagenResultante = MatricesManager.obtenerImagenDeMatriz(matrizResultante);
		BufferedImage imagenResultanteTransformada = ProcesadorDeImagenes.obtenerInstancia().aplicarTransformacionLineal(imagenResultante);
		Imagen nuevaImagenActual = new Imagen(imagenResultanteTransformada, imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
		VentanaPrincipal.this.refrescarImagen();
	}
	
	public void obtenerMatrizResultanteDeMultiplicarPorEscalar(int escalar){
		
		BufferedImage bufferedImage = ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage();
		int[][] matriz = ProcesadorDeImagenes.obtenerInstancia().calcularMatrizDeLaImagen(bufferedImage);
		
		int[][] matrizResultante = MatricesManager.multiplicarMatrizPorEscalar(matriz, escalar);	    					
		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage imagenResultante = MatricesManager.obtenerImagenDeMatriz(matrizResultante);
		BufferedImage imagenResultanteTransformada = ProcesadorDeImagenes.obtenerInstancia().aplicarTransformacionLogaritmica(imagenResultante);
		Imagen nuevaImagenActual = new Imagen(imagenResultanteTransformada, imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
		VentanaPrincipal.this.refrescarImagen();
	}
}
