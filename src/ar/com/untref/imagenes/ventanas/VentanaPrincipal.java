package ar.com.untref.imagenes.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.listeners.GuardarComoListener;
import ar.com.untref.imagenes.listeners.MostrarTablaDeColoresListener;
import ar.com.untref.imagenes.listeners.RecortarImagenListener;
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.ColorManager;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;

import java.awt.Dimension;

@SuppressWarnings("serial")
public class VentanaPrincipal extends JFrame {

	private JPanel contentPane;
	private JMenu menuItemEditar;
	private JMenuItem menuItemRecortarImagen;
	private JLabel labelPrincipal;
	private JPanel panelPixel;
	private JTextField posicionXTextField;
	private JTextField posicionYTextField;
	private JTextField textFieldAnchoRAW;
	private JTextField textFieldAltoRAW;
	private JMenuItem menuItemGuardarComo;

	public VentanaPrincipal() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("Archivo");
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
							
							e.printStackTrace();
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
		
		menuItemRecortarImagen = new JMenuItem("Recortar Imagen");
		menuItemRecortarImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				DialogsHelper.mostarMensaje(contentPane, "Cliquea en la esquina superior izquierda y la inferior derecha que formarán el cuadrado para recortar la imagen");
				labelPrincipal.addMouseListener(new RecortarImagenListener(VentanaPrincipal.this));
			}
		});
		menuItemEditar.add(menuItemRecortarImagen);
		
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
			
			menuItemGuardarComo.setEnabled(true);
			menuItemGuardarComo.addActionListener(new GuardarComoListener(imagenActual, contentPane));
		} else {
			
			menuItemGuardarComo.addActionListener(null);
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
		menuItemGuardarComo.addActionListener(new GuardarComoListener(imagen, contentPane));
	}

}
