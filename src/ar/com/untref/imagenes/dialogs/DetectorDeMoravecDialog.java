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
public class DetectorDeMoravecDialog extends JDialog {

	private VentanaPrincipal ventana;
	private JButton botonConfirmar;
	private JLabel labelUmbral;
	private JTextField umbralElegido;
	private JLabel labelRadio;
	private JTextField radioElegido;
	private VentanaRuido ventanaRuido;
	private JPanel jpanel;
	private EspereDialog dialogoEspera;
	private EspereDialog dialogoEsperaR;
	private Runnable r;

	public DetectorDeMoravecDialog(VentanaPrincipal ventana, JPanel jpanel) {
		super(ventana);
		this.ventana = ventana;
		this.jpanel = jpanel;
		dialogoEspera = new EspereDialog(ventana);
		initUI();
	}

	public DetectorDeMoravecDialog(VentanaRuido ventanaRuido, JPanel jpanel) {
		super(ventanaRuido);
		this.ventanaRuido = ventanaRuido;
		this.jpanel = jpanel;
		dialogoEsperaR = new EspereDialog (ventanaRuido);
		initUI();
	}

	private void initUI() {

		labelUmbral = new JLabel("Umbral");
		umbralElegido = new JTextField();

		labelRadio = new JLabel("Radio de la m\u00e1scara");
		radioElegido = new JTextField();

		createLayout();

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Detector de Esquinas de Moravec");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
	}

	private void createLayout() {

		botonConfirmar = new JButton("Listo");

		botonConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!umbralElegido.getText().toString().isEmpty() && !radioElegido.getText().toString().isEmpty()) {
					
					try{
						final int umbral = Integer.valueOf(umbralElegido.getText().toString());
						final int radio = Integer.valueOf(radioElegido.getText().toString());
						
							if (ventana != null){
								r = new Runnable() {
							         public void run() {
							        	 ventana.aplicarDetectorDeBordesDeMoravec(umbral, radio);	 
										dialogoEspera.ocultar();
							         }
							    };
							}
							
							if (ventanaRuido != null){
								r = new Runnable() {
							         public void run() {
							        	 ventanaRuido.aplicarDetectorDeBordesDeMoravec(umbral, radio);	 
										dialogoEsperaR.ocultar();
							         }
							    };
							}
						
						
							Thread ejecutar = new Thread(r);
							ejecutar.start();
						     
							DetectorDeMoravecDialog.this.dispose();
						    
						    if(ventana != null){dialogoEspera.mostrar();}
						    if(ventanaRuido != null){dialogoEsperaR.mostrar();}
						
					} catch (Exception ex){
						
						DialogsHelper.mostarMensaje(jpanel, "Por favor ingresa un radio v\u00e1lido", NivelMensaje.ERROR);
						ex.printStackTrace();
					}
				} else {
					
					DialogsHelper.mostarMensaje(jpanel, "Por favor ingresa un umbral v\u00e1lido", NivelMensaje.ERROR);
				}
			}
		});
		
		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER)
				.addComponent(labelRadio).addComponent(radioElegido)
				.addComponent(labelUmbral).addComponent(umbralElegido)
				.addComponent(botonConfirmar).addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(labelRadio).addGap(20).addComponent(radioElegido).addGap(20)
				.addComponent(labelUmbral).addGap(20).addComponent(umbralElegido).addGap(20)
				.addComponent(botonConfirmar).addGap(30));

		pack();
	}
}
