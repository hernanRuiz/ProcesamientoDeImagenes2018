package ar.com.untref.imagenes.dialogs;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;
import ar.com.untref.imagenes.ventanas.VentanaRuido;

@SuppressWarnings("serial")
public class DiferenciaDeGaussianasDialog extends JDialog {

	private VentanaPrincipal ventana;
	private JButton botonConfirmar;
	private JLabel labelSigma1;
	private JLabel labelSigma2;
	private JTextField sigma1Elegido;
	private JTextField sigma2Elegido;
	private JLabel labelSigma3;
	private JLabel labelSigma4;
	private JTextField sigma3Elegido;
	private JTextField sigma4Elegido;
	private VentanaRuido ventanaRuido;
	private JPanel jpanel;
	private EspereDialog dialogoEspera;
	private EspereDialog dialogoEsperaR;
	private Runnable r;

	public DiferenciaDeGaussianasDialog(VentanaPrincipal ventana, JPanel jpanel) {
		super(ventana);
		this.ventana = ventana;
		this.jpanel = jpanel;
		dialogoEspera = new EspereDialog(ventana);
		initUI();
	}

	public DiferenciaDeGaussianasDialog(VentanaRuido ventanaRuido, JPanel jpanel) {
		super(ventanaRuido);
		this.ventanaRuido = ventanaRuido;
		this.jpanel = jpanel;
		dialogoEsperaR = new EspereDialog (ventanaRuido);
		initUI();
	}

	private void initUI() {

		labelSigma1 = new JLabel("Sigma 1");
		sigma1Elegido = new JTextField();

		labelSigma2 = new JLabel("Sigma 2");
		sigma2Elegido = new JTextField();
		
		labelSigma3 = new JLabel("Sigma 3");
		sigma3Elegido = new JTextField();

		labelSigma4 = new JLabel("Sigma 4");
		sigma4Elegido = new JTextField();

		createLayout();

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Detector de Esquinas por Diferencia de Gaussianas");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
	}

	private void createLayout() {

		botonConfirmar = new JButton("Listo");

		botonConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!sigma1Elegido.getText().toString().isEmpty() && !sigma2Elegido.getText().toString().isEmpty()
						&& !sigma3Elegido.getText().toString().isEmpty() && !sigma4Elegido.getText().toString().isEmpty()) {
					
					try{
						final int sigma1 = Integer.valueOf(sigma1Elegido.getText().toString());
						final int sigma2 = Integer.valueOf(sigma2Elegido.getText().toString());
						final int sigma3 = Integer.valueOf(sigma3Elegido.getText().toString());
						final int sigma4 = Integer.valueOf(sigma4Elegido.getText().toString());
						
							if (ventana != null){
								r = new Runnable() {
							         public void run() {
							        	ventana.aplicarDoG(sigma1, sigma2, sigma3, sigma4);	 
										dialogoEspera.ocultar();
							         }
							    };	
							}
							
							if (ventanaRuido != null){
								r = new Runnable() {
							         public void run() {
							        	ventanaRuido.aplicarDoG(sigma1, sigma2, sigma3, sigma4);	 
										dialogoEsperaR.ocultar();
							         }
							    };
							}
							
							Thread ejecutar = new Thread(r);
							ejecutar.start();
						     
							DiferenciaDeGaussianasDialog.this.dispose();
						    
						    if(ventana != null){dialogoEspera.mostrar();}
						    if(ventanaRuido != null){dialogoEsperaR.mostrar();}
						
					} catch (Exception ex){
						
						DialogsHelper.mostarMensaje(jpanel, "Por favor ingresa par\u00e1metros v\u00e1lidos", NivelMensaje.ERROR);
						ex.printStackTrace();
					}
				} else {
					
					DialogsHelper.mostarMensaje(jpanel, "Por favor ingresa par\u00e1metros v\u00e1lidos", NivelMensaje.ERROR);
				}
			}
		});
		
		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER)
				.addComponent(labelSigma1).addComponent(sigma1Elegido)
				.addComponent(labelSigma2).addComponent(sigma2Elegido)
				.addComponent(labelSigma3).addComponent(sigma3Elegido)
				.addComponent(labelSigma4).addComponent(sigma4Elegido)
				.addComponent(botonConfirmar).addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(labelSigma1).addGap(20).addComponent(sigma1Elegido).addGap(20)
				.addComponent(labelSigma2).addGap(20).addComponent(sigma2Elegido).addGap(20)
				.addComponent(labelSigma3).addGap(20).addComponent(sigma3Elegido).addGap(20)
				.addComponent(labelSigma4).addGap(20).addComponent(sigma4Elegido).addGap(20)
				.addComponent(botonConfirmar).addGap(30));

		pack();
	}
}
