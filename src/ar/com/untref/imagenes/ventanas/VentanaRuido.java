package ar.com.untref.imagenes.ventanas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import ar.com.untref.imagenes.bordes.DetectorDeBordes;
import ar.com.untref.imagenes.bordes.DetectorDeBordesDeCanny;
import ar.com.untref.imagenes.bordes.InterfaceDetectorDeBordes;
import ar.com.untref.imagenes.dialogs.DifusionAnisotropicaDialog;
import ar.com.untref.imagenes.dialogs.DifusionIsotropicaDialog;
import ar.com.untref.imagenes.dialogs.EspereDialog;
import ar.com.untref.imagenes.dialogs.LoGDialog;
import ar.com.untref.imagenes.dialogs.MascaraGaussianaDialog;
import ar.com.untref.imagenes.dialogs.MedidaMascaraDialog;
import ar.com.untref.imagenes.dialogs.SigmaDialog;
import ar.com.untref.imagenes.enums.Mascara;
import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.filtros.FiltroDeLaMedia;
import ar.com.untref.imagenes.filtros.FiltroDeLaMediana;
import ar.com.untref.imagenes.filtros.FiltroGaussiano;
import ar.com.untref.imagenes.filtros.FiltroPasaAltos;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.listeners.GuardarComoListener;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.Difuminador;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;
import ar.com.untref.imagenes.procesamiento.Umbralizador;
import ar.com.untref.imagenes.ruido.GeneradorDeRuido;

@SuppressWarnings("serial")
public class VentanaRuido extends JFrame {

	private JPanel contentPane;
	private JMenu menuItemEditar;
	private JLabel labelPrincipal;
	private JLabel labelSigma;
	private JLabel labelMu;
	private JMenu menu;
	private JTextField posicionXTextField;
	private JTextField posicionYTextField;
	private JTextField textFieldMu;
	private JTextField textFieldSigma;
	private JTextField textFieldLambda;
	private JTextField textFieldPhi;
	private JTextField textFieldAnchoRAW;
	private JTextField textFieldAltoRAW;
	private JTextField textFieldPorcentaje;
	private JMenuItem menuItemGuardarComo;
	private JLabel resultadoCantidadPixeles;
	private JComboBox<String> comboGauss;
	private int cantidadDePixeles;
	private static Imagen imagenSinCambios;
	private EspereDialog dialogoEspera;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public VentanaRuido(Imagen imagenSCambios) {
		
		dialogoEspera = new EspereDialog();
		imagenSinCambios = imagenSCambios;
		this.setTitle("Generador de Ruido y Filtros");
		VentanaRuido.this.setExtendedState(VentanaRuido.this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		
		//setDefaultCloseOperation(JFrame.);
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
		
		final JPanel panelPromedios = new JPanel();
		JLabel cantidadPixeles = new JLabel("Cantidad de Pixeles:");
		resultadoCantidadPixeles = new JLabel("");
		panelPromedios.add(cantidadPixeles);
		panelPromedios.add(resultadoCantidadPixeles);
		cantidadDePixeles = imagenSinCambios.getBufferedImage().getHeight()*imagenSinCambios.getBufferedImage().getWidth();
		refrescarCantidadPixeles(cantidadDePixeles);
		
		JButton botonPromedio = new JButton("Valores Promedio:");
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
		JButton volverALaImagenOriginal = new JButton("Imagen Original");
		
		volverALaImagenOriginal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(VentanaRuido.imagenSinCambios);
				VentanaRuido.this.refrescarImagen();
				VentanaRuido.this.refrescarCantidadPixeles(VentanaRuido.imagenSinCambios.getBufferedImage().getWidth()*VentanaRuido.imagenSinCambios.getBufferedImage().getHeight());
			}
		});
		
		panelPromedios.add(volverALaImagenOriginal);
		
		contentPane.add(panelPromedios, BorderLayout.PAGE_END);
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
		
		String[] opcionesGauss = {"Ruido de Gauss", "Ruido Blanco de Gauss"};
		comboGauss = new JComboBox(opcionesGauss);
		comboGauss.setSelectedIndex(0);
		panelRuido.add(comboGauss);
		comboGauss.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

               if (comboGauss.getSelectedIndex()==1){
            	   
					Integer sigma = 1;
					Integer mu = 0;
					
					BufferedImage bufferedImage = GeneradorDeRuido.generarRuidoGauss(ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage(), sigma, mu);
					Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
					Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
					ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
					
					VentanaRuido.this.refrescarImagen();
                }
            }
            
		});
	 
		labelSigma = new JLabel("σ:");
		panelRuido.add(labelSigma);
		
		textFieldSigma = new JTextField();
		panelRuido.add(textFieldSigma);
		textFieldSigma.setMinimumSize(new Dimension(3, 20));
		textFieldSigma.setPreferredSize(new Dimension(1, 20));
		textFieldSigma.setColumns(3);
		
		labelMu = new JLabel("μ:");
		panelRuido.add(labelMu);
		
		textFieldMu = new JTextField();
		panelRuido.add(textFieldMu);
		textFieldMu.setColumns(3);
		
		
		JButton aplicarRuidoGauss = new JButton("Aplicar");
		panelRuido.add(aplicarRuidoGauss);
		aplicarRuidoGauss.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String campoSigma = textFieldSigma.getText().trim();
				String campoMu = textFieldMu.getText().trim();

				if (!campoSigma.isEmpty() && !campoMu.isEmpty()){
					
					try {
						
					final Integer sigma = Integer.valueOf(campoSigma);
					final Integer mu = Integer.valueOf(campoMu);
					
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
						
						DialogsHelper.mostarMensaje(contentPane, "Por favor ingrese parámetros numéricos", NivelMensaje.ERROR);
					}
				} else {
					
					DialogsHelper.mostarMensaje(contentPane, "Por favor completa los campos Mu y Sigma", NivelMensaje.ERROR);
				}
			}
		
		});
		
		JLabel labelRuidoExponencial = new JLabel("Ruido Exponencial:");
		panelRuido.add(labelRuidoExponencial);
		
		JLabel labelLambda = new JLabel("λ:");
		panelRuido.add(labelLambda);
		
		textFieldLambda = new JTextField();
		panelRuido.add(textFieldLambda);
		textFieldLambda.setMinimumSize(new Dimension(3, 20));
		textFieldLambda.setPreferredSize(new Dimension(1, 20));
		textFieldLambda.setColumns(3);
		
		JButton aplicarRuidoExponencial = new JButton("Aplicar");
		panelRuido.add(aplicarRuidoExponencial);
		aplicarRuidoExponencial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String campoLambda = textFieldLambda.getText().trim();

				if (!campoLambda.isEmpty()){
					
					try {
						
					final Integer lambda = Integer.valueOf(campoLambda);
					
					SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
				         @Override
				         protected Void doInBackground() throws Exception {

				        	BufferedImage bufferedImage = GeneradorDeRuido.generarRuidoExponencialMultiplicativo(ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage(), lambda);
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
					
					DialogsHelper.mostarMensaje(contentPane, "Por favor completa el campo Lambda", NivelMensaje.ERROR);
				}
			}
		
		});

		
		JLabel labelRuidoRayleigh = new JLabel("Ruido Rayleigh:");
		panelRuido.add(labelRuidoRayleigh);
		
		JLabel labelPhi = new JLabel("φ:");
		panelRuido.add(labelPhi);
		
		textFieldPhi = new JTextField();
		panelRuido.add(textFieldPhi);
		textFieldPhi.setMinimumSize(new Dimension(3, 20));
		textFieldPhi.setPreferredSize(new Dimension(1, 20));
		textFieldPhi.setColumns(3);
		
		JButton aplicarRuidoRayleigh = new JButton("Aplicar");
		panelRuido.add(aplicarRuidoRayleigh);
		aplicarRuidoRayleigh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String campoPhi = textFieldPhi.getText().trim();

				if (!campoPhi.isEmpty()){
					
					try {
						
					final Integer phi = Integer.valueOf(campoPhi);
					
					
					SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
				         @Override
				         protected Void doInBackground() throws Exception {

				        	BufferedImage bufferedImage = GeneradorDeRuido.generarRuidoRayleighMultiplicativo(ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage(), phi);
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
					
					DialogsHelper.mostarMensaje(contentPane, "Por favor completa el campo Lambda", NivelMensaje.ERROR);
				}
			}
		
		});
		
		JLabel labelRuidoSaltAndPepper = new JLabel("Ruido SyP");
		panelRuido.add(labelRuidoSaltAndPepper);
		
		JLabel labelPorcentaje = new JLabel("(%):");
		panelRuido.add(labelPorcentaje);
		
		textFieldPorcentaje = new JTextField();
		panelRuido.add(textFieldPorcentaje);
		textFieldPorcentaje.setMinimumSize(new Dimension(3, 20));
		textFieldPorcentaje.setPreferredSize(new Dimension(1, 20));
		textFieldPorcentaje.setColumns(3);
		
		
		JButton aplicarRuidoSaltAndPepper = new JButton("Aplicar");
		panelRuido.add(aplicarRuidoSaltAndPepper);
		aplicarRuidoSaltAndPepper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String campoPorcentaje = textFieldPorcentaje.getText().trim();

				if (!campoPorcentaje.isEmpty()){
					
					try {
						
					final Integer porcentaje = Integer.valueOf(campoPorcentaje);
					
					SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
				         @Override
				         protected Void doInBackground() throws Exception {

				        	BufferedImage bufferedImage = GeneradorDeRuido.generarRuidoSaltAndPepper(ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage(), porcentaje);
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
					
					DialogsHelper.mostarMensaje(contentPane, "Por favor completa el campo Porcentaje", NivelMensaje.ERROR);
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
					int cantidadPixeles = alto*ancho;
					refrescarCantidadPixeles(cantidadPixeles);
					
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
		
		JMenuItem menuItemHistogramas = new JMenuItem("Histogramas");
		menuItemHistogramas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Imagen imagenActual = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				if ( imagenActual!=null ){
					
					VentanaHistogramas ventanaHistogramas = new VentanaHistogramas(imagenActual, false);
					ventanaHistogramas.setVisible(true);
				}
			}
		});
		menuItemEditar.add(menuItemHistogramas);
		
		JMenu menuFiltros = new JMenu("Filtros");
		menuItemEditar.add(menuFiltros);
		
		JMenuItem filtroGaussianoMenuItem = new JMenuItem("Filtro gaussiano");
		filtroGaussianoMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				MascaraGaussianaDialog m = new MascaraGaussianaDialog(VentanaRuido.this);
				m.setVisible(true);
			}
		});
		
		JMenuItem menuItemFiltroMedia = new JMenuItem("Filtro de la media");
		menuItemFiltroMedia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				MedidaMascaraDialog d = new MedidaMascaraDialog(VentanaRuido.this, Mascara.MEDIA);
				d.setVisible(true);
			}
		});
		menuFiltros.add(menuItemFiltroMedia);
		menuFiltros.add(filtroGaussianoMenuItem);
		
		JMenuItem menuItemFiltroPasaAltos = new JMenuItem("Filtro pasa altos");
		menuItemFiltroPasaAltos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				MedidaMascaraDialog d = new MedidaMascaraDialog(VentanaRuido.this, Mascara.PASA_ALTOS);
				d.setVisible(true);
			}
		});
		
		JMenuItem menuItemFiltroDeLaMediana = new JMenuItem("Filtro de la mediana");
		menuItemFiltroDeLaMediana.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				MedidaMascaraDialog d = new MedidaMascaraDialog(VentanaRuido.this, Mascara.MEDIANA);
				d.setVisible(true);
			}
		});
		menuFiltros.add(menuItemFiltroDeLaMediana);
		menuFiltros.add(menuItemFiltroPasaAltos);
		
		
		JMenu menuDeteccionDeBordes = new JMenu("Deteccion de Bordes");
		menuItemEditar.add(menuDeteccionDeBordes);
		
		JMenu menuItemCanny = new JMenu("Detector De Bordes Canny");
		menuDeteccionDeBordes.add(menuItemCanny);
		
		JMenuItem menuItemNoMaximos = new JMenuItem("Supresión no Máximos");
		menuItemNoMaximos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				Imagen nuevaImagenActual = DetectorDeBordesDeCanny.mostrarImagenNoMaximos(imagenAnterior);
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaRuido.this.refrescarImagen();
			}
		});
		menuItemCanny.add(menuItemNoMaximos);
		
		
		JMenu menuDeteccionDePrewitt = new JMenu("Detector De Prewitt");
		menuDeteccionDeBordes.add(menuDeteccionDePrewitt);
		
		JMenuItem menuItemDetectorDePrewitt = new JMenuItem("Aplicar");
		menuItemDetectorDePrewitt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.aplicarDetectorDePrewitt(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaRuido.this.refrescarImagen();
			}
			
		});
		menuDeteccionDePrewitt.add(menuItemDetectorDePrewitt);
		
		
		JMenuItem menuItemMascaraEnXPrewitt = new JMenuItem("Mostrar mascara en X");
		menuItemMascaraEnXPrewitt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraDePrewittEnX(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaRuido.this.refrescarImagen();
			}
			
		});
		menuDeteccionDePrewitt.add(menuItemMascaraEnXPrewitt);
		
		
		JMenuItem menuItemMascaraEnYPrewitt = new JMenuItem("Mostrar mascara en Y");
		menuItemMascaraEnYPrewitt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraDePrewittEnY(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaRuido.this.refrescarImagen();
			}
			
		});
		menuDeteccionDePrewitt.add(menuItemMascaraEnYPrewitt);
		
		
		JMenu menuDeteccionDeSobel = new JMenu("Detector De Sobel");
		menuDeteccionDeBordes.add(menuDeteccionDeSobel);
		
		JMenuItem menuItemDetectorDeSobel = new JMenuItem("Aplicar");
		menuItemDetectorDeSobel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.aplicarDetectorDeSobel(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaRuido.this.refrescarImagen();
			}
			
		});
		menuDeteccionDeSobel.add(menuItemDetectorDeSobel);
		
		JMenuItem menuItemMascaraEnXSobel = new JMenuItem("Mostrar mascara en X");
		menuItemMascaraEnXSobel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraDeSobelEnX(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaRuido.this.refrescarImagen();
			}
			
		});
		menuDeteccionDeSobel.add(menuItemMascaraEnXSobel);
		
		
		JMenuItem menuItemMascaraEnYSobel = new JMenuItem("Mostrar mascara en Y");
		menuItemMascaraEnYSobel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraDeSobelEnY(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaRuido.this.refrescarImagen();
			}
			
		});
		menuDeteccionDeSobel.add(menuItemMascaraEnYSobel);
		
		
		JMenu menuDeteccionLaplaciano = new JMenu("Detector Laplaciano");
		menuDeteccionDeBordes.add(menuDeteccionLaplaciano);
		
		JMenuItem menuItemMostrarMascaraLaplaciano = new JMenuItem("Mostrar Mascara");
		menuItemMostrarMascaraLaplaciano.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraDeLaplaciano(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaRuido.this.refrescarImagen();
			}
			
		});
		
		JMenuItem menuItemAplicarDetectorLaplaciano = new JMenuItem("Aplicar");
		menuItemAplicarDetectorLaplaciano.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.aplicarDetectorLaplaciano(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaRuido.this.refrescarImagen();
			}
			
		});
		
		JMenuItem menuItemMostrarCrucesPorCero = new JMenuItem("Mostrar Cruces Por Cero");
		menuItemMostrarCrucesPorCero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
				BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraCrucesPorCeros(imagenAnterior);
				Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
				
				VentanaRuido.this.refrescarImagen();
			}
			
		});
		
		menuDeteccionLaplaciano.add(menuItemMostrarMascaraLaplaciano);
		menuDeteccionLaplaciano.add(menuItemMostrarCrucesPorCero);
		menuDeteccionLaplaciano.add(menuItemAplicarDetectorLaplaciano);
		
		JMenu menuDeteccionLoG = new JMenu("Detector Laplaciano del Gaussiano");
		menuDeteccionDeBordes.add(menuDeteccionLoG);
		
		JMenuItem menuItemAplicarDetectorLoG = new JMenuItem("Aplicar");
		menuItemAplicarDetectorLoG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				LoGDialog dialogo = new LoGDialog(VentanaRuido.this);
				dialogo.setVisible(true);
			}
			
		});
		menuDeteccionLoG.add(menuItemAplicarDetectorLoG);
		
		JMenuItem menuItemUmbralOtsu = new JMenuItem("Umbral Otsu");
		menuItemUmbralOtsu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(Umbralizador.generarUmbralizacionOtsu(ProcesadorDeImagenes.obtenerInstancia().getImagenActual()));
				VentanaRuido.this.refrescarImagen();
			}
		});
		
		JMenuItem menuItemUmbralGlobal = new JMenuItem("Umbral Global");
		menuItemUmbralGlobal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ProcesadorDeImagenes.obtenerInstancia().encontrarUmbralGlobal(VentanaRuido.this, 150);
				VentanaRuido.this.refrescarImagen();
			}
		});
		
		JMenu menuUmbral = new JMenu ("Umbrales");
		menuItemEditar.add(menuUmbral);
		
		menuUmbral.add(menuItemUmbralGlobal);
		menuUmbral.add(menuItemUmbralOtsu);
		
		JMenuItem menuItemMostrarMascaraLaplacianoDelGaussiano = new JMenuItem("Mostrar Mascara");
		menuItemMostrarMascaraLaplacianoDelGaussiano.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SigmaDialog dialogo = new SigmaDialog(VentanaRuido.this);
				dialogo.setVisible(true);
			}
			
		});
		menuDeteccionLoG.add(menuItemMostrarMascaraLaplacianoDelGaussiano);
		
		
		JMenu menuDifusion = new JMenu("Difusion");
		menuItemEditar.add(menuDifusion);
		
		JMenuItem menuItemDifusionIsotropica = new JMenuItem("Aplicar Difusión Isotrópica");
		menuItemDifusionIsotropica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DifusionIsotropicaDialog dialogo = new DifusionIsotropicaDialog(VentanaRuido.this);
				dialogo.setVisible(true);
			}
			
		});
		menuDifusion.add(menuItemDifusionIsotropica);
		
		JMenuItem menuItemDifusionAnisotropica = new JMenuItem("Aplicar Difusión Anisotrópica");
		menuItemDifusionAnisotropica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DifusionAnisotropicaDialog dialogo = new DifusionAnisotropicaDialog(VentanaRuido.this);
				dialogo.setVisible(true);
			}
			
		});
		menuDifusion.add(menuItemDifusionAnisotropica);
		
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
		}
		
		chequearGuardarComo(menuItemGuardarComo);
	}

	public void refrescarImagen() {

		ocultarDialogoDeEspera();
		Imagen imagen = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		labelPrincipal.setIcon(new ImageIcon(imagen.getBufferedImage()));
		chequearGuardarComo(menuItemGuardarComo);
	}

	public void refrescarCantidadPixeles(int cantidadPixeles){
		resultadoCantidadPixeles.setText(String.valueOf(cantidadPixeles));
	}

	public void aplicarFiltroGaussiano(final Integer sigmaElegido) {
		
		SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
	         @Override
	         protected Void doInBackground() throws Exception {

	            Imagen imagenFiltrada = FiltroGaussiano.aplicarFiltroGaussiano(ProcesadorDeImagenes.obtenerInstancia().getImagenActual(), sigmaElegido);
	    		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenFiltrada);

	    		VentanaRuido.this.refrescarImagen();

	            return null;
	         }
	      };

	      mySwingWorker.execute();
	      mostrarDialogoDeEspera();
	}

	public void aplicarFiltroDeLaMedia(final Integer longitudMascara) {

		SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
	         @Override
	         protected Void doInBackground() throws Exception {

	        	Imagen imagenFiltrada = FiltroDeLaMedia.aplicarFiltroDeLaMedia(ProcesadorDeImagenes.obtenerInstancia().getImagenActual(), longitudMascara);
	     		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenFiltrada);
	     		
	     		VentanaRuido.this.refrescarImagen();
	     		
	            return null;
	         }
	      };

	      mySwingWorker.execute();
	      mostrarDialogoDeEspera();
	}
	
	public void aplicarFiltroDeLaMediana(final Integer longitudMascara) {

		SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
	         @Override
	         protected Void doInBackground() throws Exception {

	     		float filtroK[] = new float[longitudMascara*longitudMascara];

	     		for (int i = 0; i < longitudMascara; i++) {
	     			for (int j = 0; j < longitudMascara; j++) {
	     				filtroK[i * longitudMascara + j] = 1;
	     			}
	     		} 
	        	 
	     		//Generamos un kernel con todos 1, ya que la mascara para este filtro para no modificar los valores al multiplicar por los de la máscara
	        	FiltroDeLaMediana filtro = new FiltroDeLaMediana(new Kernel(longitudMascara,longitudMascara, filtroK));
	        	Imagen imagenFiltrada = filtro.aplicarFiltroDeLaMediana(ProcesadorDeImagenes.obtenerInstancia().getImagenActual());
	     		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenFiltrada);
	     		
	     		VentanaRuido.this.refrescarImagen();
	     		
	            return null;
	         }
	      };

	      mySwingWorker.execute();
	      mostrarDialogoDeEspera();
	}
	
	public void aplicarFiltroPasaAltos(final Integer longitudMascara) {

		SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
	         @Override
	         protected Void doInBackground() throws Exception {

	        	Imagen imagenFiltrada = FiltroPasaAltos.aplicarFiltroPasaAltos(ProcesadorDeImagenes.obtenerInstancia().getImagenActual(), longitudMascara);
	     		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenFiltrada);
	     		
	     		VentanaRuido.this.refrescarImagen();
	     		
	            return null;
	         }
	      };

	      mySwingWorker.execute();
	      mostrarDialogoDeEspera();
	}
	
	public void mostrarDialogoDeEspera(){
		
		this.dialogoEspera.mostrar();
	}
	
	public void ocultarDialogoDeEspera(){
		
		this.dialogoEspera.ocultar();
	}
	
	public void aplicarLaplacianoDelGaussiano(int sigma, int umbral, int longitudMascara) {
		
		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage bufferedImage = DetectorDeBordes.aplicarDetectorLaplacianoDelGaussiano(imagenAnterior, sigma, umbral, longitudMascara);
		Imagen nuevaImagenActual = new Imagen(bufferedImage,
				imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(
				nuevaImagenActual);

		VentanaRuido.this.refrescarImagen();
	}
	
	public void mostrarMascaraLaplacianoDelGaussiano(int sigma) {

		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage bufferedImage = DetectorDeBordes.mostrarMascaraLaplacianoDelGaussiano(imagenAnterior, sigma);
		Imagen nuevaImagenActual = new Imagen(bufferedImage,
				imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(
				nuevaImagenActual);

		VentanaRuido.this.refrescarImagen();
	}
	
	public void aplicarDifusionIsotropica(int repeticiones) {

		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage bufferedImage = Difuminador.aplicarDifusion(imagenAnterior, null, repeticiones, true);
		Imagen nuevaImagenActual = new Imagen(bufferedImage,
				imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(
				nuevaImagenActual);

		VentanaRuido.this.refrescarImagen();
	}
	
	public void aplicarDifusionAnisotropica(int repeticiones, InterfaceDetectorDeBordes detectorDeBordes) {

		Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
		BufferedImage bufferedImage = imagenAnterior.getBufferedImage();
		
		bufferedImage = Difuminador.aplicarDifusion(imagenAnterior, detectorDeBordes, repeticiones, false);
	
		Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);

		VentanaRuido.this.refrescarImagen();
	}
}
