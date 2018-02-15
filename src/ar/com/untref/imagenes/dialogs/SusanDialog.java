package ar.com.untref.imagenes.dialogs;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;
import ar.com.untref.imagenes.ventanas.VentanaRuido;

@SuppressWarnings("serial")
public class SusanDialog extends JDialog {

	private VentanaPrincipal ventana;
	private JButton botonConfirmar;
	private JLabel labelEsquinas;
	private JLabel labelBordes;
	private JLabel labelSierra;
	private JCheckBox boxEsquinas;
	private JCheckBox boxBordes;
	private JCheckBox boxSierra;
	private VentanaRuido ventanaRuido;
	private JPanel jpanel;
	private EspereDialog dialogoEspera;
	private EspereDialog dialogoEsperaR;
	private Runnable r;
	
	public SusanDialog(VentanaPrincipal ventana, JPanel jpanel) {
		super(ventana);
		this.ventana = ventana;
		this.jpanel = jpanel;
		dialogoEspera = new EspereDialog(ventana);
		initUI();
	}

	public SusanDialog(VentanaRuido ventanaRuido, JPanel jpanel) {
		super(ventanaRuido);
		this.ventanaRuido = ventanaRuido;
		this.jpanel = jpanel;
		dialogoEsperaR = new EspereDialog (ventanaRuido);
		initUI();
	}

	private void initUI() {

		labelEsquinas = new JLabel("Esquinas");
		labelBordes = new JLabel("Bordes");
		labelSierra = new JLabel("Sierras");
		
		boxEsquinas = new JCheckBox();
		boxBordes = new JCheckBox();
		boxSierra = new JCheckBox();
		
		createLayout();

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Detector de Susan");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
	}

	private void createLayout() {
		
		botonConfirmar = new JButton("Listo");
		botonConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			     
				if (boxEsquinas.isSelected() && boxBordes.isSelected() && boxSierra.isSelected()){
					
					if (ventana != null){
						r = new Runnable() {
					         public void run() {
				        		ventana.aplicarDetectorSusan("BordesSierrasYEsquinas");	 
								dialogoEspera.ocultar();
					         }
					     };
					}
					
					if (ventanaRuido != null){
						r = new Runnable() {
					         public void run() {
					        	ventanaRuido.aplicarDetectorSusan("BordesSierrasYEsquinas");	 
								dialogoEsperaR.ocultar();
					         }
					     };
					}
				}else if (!boxEsquinas.isSelected() && boxBordes.isSelected() && boxSierra.isSelected()){
					
					if (ventana != null){
						r = new Runnable() {
					         public void run() {
				        		ventana.aplicarDetectorSusan("BordesYSierras");	 
								dialogoEspera.ocultar();
					         }
					     };
					}
					
					if (ventanaRuido != null){
						r = new Runnable() {
					         public void run() {
					        	ventanaRuido.aplicarDetectorSusan("BordesYSierras");	 
								dialogoEsperaR.ocultar();
					         }
					     };
					}
				}else if (boxEsquinas.isSelected() && !boxBordes.isSelected() && boxSierra.isSelected()){
					
					if (ventana != null){
						r = new Runnable() {
					         public void run() {
				        		ventana.aplicarDetectorSusan("EsquinasYSierras");	 
								dialogoEspera.ocultar();
					         }
					     };
					}
					
					if (ventanaRuido != null){
						r = new Runnable() {
					         public void run() {
					        	ventanaRuido.aplicarDetectorSusan("EsquinasYSierras");	 
								dialogoEsperaR.ocultar();
					         }
					     };
					}
				}else if (boxEsquinas.isSelected() && boxBordes.isSelected() && !boxSierra.isSelected()){
					
					if (ventana != null){
						r = new Runnable() {
					         public void run() {
				        		ventana.aplicarDetectorSusan("EsquinasYBordes");	 
								dialogoEspera.ocultar();
					         }
					     };
					}
					
					if (ventanaRuido != null){
						r = new Runnable() {
					         public void run() {
					        	ventanaRuido.aplicarDetectorSusan("EsquinasYBordes");	 
								dialogoEsperaR.ocultar();
					         }
					     };
					}
				}else if (!boxEsquinas.isSelected() && !boxBordes.isSelected() && boxSierra.isSelected()){
					
					if (ventana != null){
						r = new Runnable() {
					         public void run() {
				        		ventana.aplicarDetectorSusan("Sierras");	 
								dialogoEspera.ocultar();
					         }
					     };
					}
					
					if (ventanaRuido != null){
						r = new Runnable() {
					         public void run() {
					        	ventanaRuido.aplicarDetectorSusan("Sierras");	 
								dialogoEsperaR.ocultar();
					         }
					     };
					}
				}else if (!boxEsquinas.isSelected() && boxBordes.isSelected() && !boxSierra.isSelected()){
									
					if (ventana != null){
						r = new Runnable() {
					         public void run() {
				        		ventana.aplicarDetectorSusan("Bordes");	 
								dialogoEspera.ocultar();
					         }
					     };
					}
					
					if (ventanaRuido != null){
						r = new Runnable() {
					         public void run() {
					        	ventanaRuido.aplicarDetectorSusan("Bordes");	 
								dialogoEsperaR.ocultar();
					         }
					     };
					}
				}else if (boxEsquinas.isSelected() && !boxBordes.isSelected() && !boxSierra.isSelected()){
					
					if (ventana != null){
						r = new Runnable() {
					         public void run() {
				        		ventana.aplicarDetectorSusan("Esquinas");	 
								dialogoEspera.ocultar();
					         }
					     };
					}
					
					if (ventanaRuido != null){
						r = new Runnable() {
					         public void run() {
					        	ventanaRuido.aplicarDetectorSusan("Esquinas");	 
								dialogoEsperaR.ocultar();
					         }
					     };
					}
				}else{
					DialogsHelper.mostarMensaje(jpanel, "Por favor seleccione alg\u00fan criterio de detecci\u00f3n", NivelMensaje.ERROR);
				}
				
				Thread ejecutar = new Thread(r);
				ejecutar.start();
			     
			    SusanDialog.this.dispose();
			    
			    if(ventana != null){dialogoEspera.mostrar();}
			    if(ventanaRuido != null){dialogoEsperaR.mostrar();}
			}
		});
		
		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER)
				.addComponent(labelEsquinas).addComponent(boxEsquinas)
				.addComponent(labelBordes).addComponent(boxBordes)
				.addComponent(labelSierra).addComponent(boxSierra)
				.addComponent(botonConfirmar).addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(labelEsquinas).addComponent(boxEsquinas).addGap(20)
				.addComponent(labelBordes).addComponent(boxBordes).addGap(20)
				.addComponent(labelSierra).addComponent(boxSierra).addGap(20)
				.addComponent(botonConfirmar).addGap(30));

		pack();
	}
}
