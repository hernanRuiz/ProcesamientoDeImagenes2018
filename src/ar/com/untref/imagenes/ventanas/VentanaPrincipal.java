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
import ar.com.untref.imagenes.modelo.Imagen;
import ar.com.untref.imagenes.procesamiento.ColorManager;
import ar.com.untref.imagenes.procesamiento.ProcesadorDeImagenes;

@SuppressWarnings("serial")
public class VentanaPrincipal extends JFrame {

	private JPanel contentPane;
	private ProcesadorDeImagenes procesadorDeImagenes = new ProcesadorDeImagenes();
	private JMenu menuItemEditar;
	private JMenuItem menuItemEditarDimensionesRaw;
	private JTextField posicionXTextField;
	private JTextField posicionYTextField;

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
		final JLabel labelPrincipal = new JLabel(iconoSubirArchivo, JLabel.CENTER);
		scrollPane.setViewportView(labelPrincipal);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel labelPosicionX = new JLabel("Posicion X:");
		panel.add(labelPosicionX);
		
		posicionXTextField = new JTextField();
		panel.add(posicionXTextField);
		posicionXTextField.setColumns(10);
		
		JLabel labelPosicionY = new JLabel("Posicion Y:");
		labelPosicionY.setHorizontalAlignment(SwingConstants.TRAILING);
		panel.add(labelPosicionY);
		
		posicionYTextField = new JTextField();
		posicionYTextField.setColumns(10);
		panel.add(posicionYTextField);
		
		JButton btnBuscar = new JButton("Buscar");
		panel.add(btnBuscar);
		
		JLabel labelColorEnPosicion = new JLabel("Color:");
		labelColorEnPosicion.setHorizontalAlignment(SwingConstants.TRAILING);
		panel.add(labelColorEnPosicion);
		
		final JLabel labelColorResultante = new JLabel("");
		panel.add(labelColorResultante);
		
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (procesadorDeImagenes.getImagenActual()!=null){
					
					if (!posicionXTextField.getText().trim().isEmpty() && !posicionYTextField.getText().trim().isEmpty()){
						
						int[][] matriz = procesadorDeImagenes.getMatrizDeLaImagen(procesadorDeImagenes.getImagenActual().getBufferedImage());
						
						try{
							
							Integer x = Integer.valueOf(posicionXTextField.getText().trim());
							Integer y = Integer.valueOf(posicionYTextField.getText().trim());
							
							String colorHexa = ColorManager.getHexaDeColorRGB(matriz[x][y]);
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
		final JMenuItem menuItemGuardarComo = new JMenuItem("Guardar Como...");
		
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
			
				if (procesadorDeImagenes.getImagenActual()==null){
					
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
		
		inhabilitarItem(menuItemGuardarComo);
		
		menu.add(menuItemGuardarComo);
		menu.add(menuItem);
		
		menuItemEditar = new JMenu("Editar");
		
		inhabilitarItem(menuItemEditar);
		
		menuBar.add(menuItemEditar);
		
		menuItemEditarDimensionesRaw = new JMenuItem("RAW-Configurar Tama\u00F1o ");
		menuItemEditar.add(menuItemEditarDimensionesRaw);
		
		inhabilitarItem(menuItemEditarDimensionesRaw);
	}
	
	private void cargarImagen(JLabel labelPrincipal,
			JMenuItem menuItemGuardarComo) {
		Imagen imagenElegida = procesadorDeImagenes.cargarUnaImagenDesdeArchivo();
		
		if (imagenElegida!=null){
			
			labelPrincipal.setIcon(new ImageIcon(imagenElegida.getBufferedImage()));
			menuItemEditar.setVisible(true);
		}
		
		chequearGuardarComo(menuItemGuardarComo);
	}
	
	private void inhabilitarItem(JMenuItem item){
		
		item.addActionListener(null);
		item.setEnabled(false);
	}

	private void chequearGuardarComo(JMenuItem menuItemGuardarComo) {
		Imagen imagenActual = procesadorDeImagenes.getImagenActual();
		
		if (imagenActual!=null){
			
			menuItemGuardarComo.setEnabled(true);
			menuItemGuardarComo.addActionListener(new GuardarComoListener(imagenActual, contentPane));
		} else {
			
			menuItemGuardarComo.addActionListener(null);
			menuItemGuardarComo.setEnabled(false);
		}
	}

}
