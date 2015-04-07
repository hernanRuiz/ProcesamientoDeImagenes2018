package ar.com.untref.imagenes.ventanas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

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
import javax.swing.border.EmptyBorder;

import ar.com.untref.imagenes.dialogs.EspereDialog;
import ar.com.untref.imagenes.dialogs.MascaraDeLaMediaDialog;
import ar.com.untref.imagenes.dialogs.MascaraGaussianaDialog;
import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.filtros.FiltroDeLaMedia;
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
	private JLabel labelMu;
	private JMenu menu;
	private JTextField posicionXTextField;
	private JTextField posicionYTextField;
	private JTextField textFieldMu;
	private JTextField textFieldSigma;
	private JTextField textFieldLambda;
	private JTextField textFieldPhi;
	private JTextField textFieldPorcentaje;
	private JMenuItem menuItemGuardarComo;
	private JLabel resultadoCantidadPixeles;
	private JComboBox<String> comboGauss;
	private int cantidadDePixeles;
	private static Imagen imagenSinCambios;
	private EspereDialog dialogoEspera;
	
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
		
		String[] opcionesGauss = {"Ruido de Gauss", "Ruido Blanco de Gauss"};
		comboGauss = new JComboBox(opcionesGauss);
		comboGauss.setSelectedIndex(0);
		panelRuido.add(comboGauss);
		comboGauss.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                int opcionSeleccionada = comboGauss.getSelectedIndex();
                
                switch (opcionSeleccionada) {//check for a match
                	
                case 1:
                    	
        				Integer sigma = 1;
                    	//textFieldSigma.setText(String.valueOf(sigma));
    					Integer mu = 0;
    					//textFieldMu.setText(String.valueOf(mu));
    					
    					BufferedImage bufferedImage = GeneradorDeRuido.generarRuidoGauss(ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage(), sigma, mu);
    					Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
    					Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
    					ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
    					
    					VentanaRuido.this.refrescarImagen();
                }
            }
            
		});
	 
		//JLabel labelRuidoGauss = new JLabel("Ruido Gaussiano:");
		//panelRuido.add(labelRuidoGauss);
		
		labelSigma = new JLabel("Sigma:");
		panelRuido.add(labelSigma);
		
		textFieldSigma = new JTextField();
		panelRuido.add(textFieldSigma);
		textFieldSigma.setMinimumSize(new Dimension(3, 20));
		textFieldSigma.setPreferredSize(new Dimension(1, 20));
		textFieldSigma.setColumns(3);
		
		labelMu = new JLabel("Mu:");
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
						
					Integer sigma = Integer.valueOf(campoSigma);
					Integer mu = Integer.valueOf(campoMu);
					BufferedImage bufferedImage = GeneradorDeRuido.generarRuidoGauss(ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage(), sigma, mu);
					Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
					Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
					ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
					
					VentanaRuido.this.refrescarImagen();
					
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
		
		JLabel labelLambda = new JLabel("Lambda:");
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
						
					Integer lambda = Integer.valueOf(campoLambda);
					BufferedImage bufferedImage = GeneradorDeRuido.generarRuidoExponencialMultiplicativo(ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage(), lambda);
					Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
					Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
					ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
					
					VentanaRuido.this.refrescarImagen();
					
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
		
		JLabel labelPhi = new JLabel("Phi:");
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
						
					Integer phi = Integer.valueOf(campoPhi);
					BufferedImage bufferedImage = GeneradorDeRuido.generarRuidoRayleighMultiplicativo(ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage(), phi);
					Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
					Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
					ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
					
					VentanaRuido.this.refrescarImagen();
					
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
		
		JLabel labelPorcentaje = new JLabel("Porcentaje:");
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
						
					Integer porcentaje = Integer.valueOf(campoPorcentaje);
					BufferedImage bufferedImage = GeneradorDeRuido.generarRuidoSaltAndPepper(ProcesadorDeImagenes.obtenerInstancia().getImagenActual().getBufferedImage(), porcentaje);
					Imagen imagenAnterior = ProcesadorDeImagenes.obtenerInstancia().getImagenActual();
					Imagen nuevaImagenActual = new Imagen(bufferedImage, imagenAnterior.getFormato(), imagenAnterior.getNombre());
					ProcesadorDeImagenes.obtenerInstancia().setImagenActual(nuevaImagenActual);
					
					VentanaRuido.this.refrescarImagen();
					
					} catch (Exception e) {
						
						DialogsHelper.mostarMensaje(contentPane, "Por favor ingrese parámetro numérico", NivelMensaje.ERROR);
					}
				} else {
					
					DialogsHelper.mostarMensaje(contentPane, "Por favor completa el campo Porcentaje", NivelMensaje.ERROR);
				}
			}
		
		});
		
		JButton volverALaImagenOriginal = new JButton("Imagen Original");
		volverALaImagenOriginal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ProcesadorDeImagenes.obtenerInstancia().setImagenActual(VentanaRuido.imagenSinCambios);
				VentanaRuido.this.refrescarImagen();
			}
		});
		panelRuido.add(volverALaImagenOriginal);
		
		
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
					
					VentanaHistogramas ventanaHistogramas = new VentanaHistogramas(imagenActual);
					ventanaHistogramas.setVisible(true);
				}
			}
		});
		menuItemEditar.add(menuItemHistogramas);
		
		
		
		
		
		JMenu menuFiltros = new JMenu("Filtros");
		menuItemEditar.add(menuFiltros);
		
		JMenuItem filtroGaussianoMenuItem = new JMenuItem("Filtro Gaussiano");
		filtroGaussianoMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				MascaraGaussianaDialog m = new MascaraGaussianaDialog(VentanaRuido.this);
				m.setVisible(true);
			}
		});
		
		JMenuItem menuItemFiltroMedia = new JMenuItem("FiltroDeLaMedia");
		menuItemFiltroMedia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				MascaraDeLaMediaDialog d = new MascaraDeLaMediaDialog(VentanaRuido.this);
				d.setVisible(true);
			}
		});
		menuFiltros.add(menuItemFiltroMedia);
		menuFiltros.add(filtroGaussianoMenuItem);
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

	public void aplicarFiltroGaussiano(Integer sigmaElegido) {
		
		Imagen imagenFiltrada = FiltroGaussiano.aplicarFiltroGaussiano(ProcesadorDeImagenes.obtenerInstancia().getImagenActual(), sigmaElegido);
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenFiltrada);

		VentanaRuido.this.refrescarImagen();
	}

	public void aplicarFiltroDeLaMedia(Integer longitudMascara) {

		Imagen imagenFiltrada = FiltroDeLaMedia.aplicarFiltroDeLaMedia(ProcesadorDeImagenes.obtenerInstancia().getImagenActual(), longitudMascara);
		ProcesadorDeImagenes.obtenerInstancia().setImagenActual(imagenFiltrada);
		
		VentanaRuido.this.refrescarImagen();
	}
	
	public void mostrarDialogoDeEspera(){
		
		this.dialogoEspera.mostrar();
	}
	
	public void ocultarDialogoDeEspera(){
		
		this.dialogoEspera.ocultar();
	}
	
}
