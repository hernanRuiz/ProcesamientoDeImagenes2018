package ar.com.untref.imagenes.dialogs;
	
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.com.untref.imagenes.enums.NivelMensaje;
import ar.com.untref.imagenes.helpers.DialogsHelper;
import ar.com.untref.imagenes.ventanas.VentanaPrincipal;

	@SuppressWarnings("serial")
	public class AlgoritmoComparativoDialog extends JDialog {

		private VentanaPrincipal ventana;
		DecimalFormat numberFormat = new DecimalFormat("#.00");
		
		private JButton botonConfirmar;
		private JLabel comboLabel;
		private JComboBox<String> combo;
		private JLabel ruidoLabel;
		private JLabel labelSigma;
		private JTextField sigmaElegido;
		private JPanel jpanel;
		
		private EspereDialog dialogoEspera;

		public AlgoritmoComparativoDialog(VentanaPrincipal ventana, JPanel jpanel) {
			super(ventana);
			this.ventana = ventana;
			this.jpanel = jpanel;
			initUI();
		}

		private void initUI() {
			
			comboLabel = new JLabel("Contraste");
			
			String[] porcentajes = { "0%", "5%", "10%", "15%", "20%", "25%", "30%", "35%", "40%" };
			
			combo = new JComboBox<String>(porcentajes);
			combo.setSelectedIndex(0);
			
			ruidoLabel = new JLabel("Ruido Gaussiano");
			labelSigma = new JLabel("Sigma");
			sigmaElegido = new JTextField();

			createLayout();

			setModalityType(ModalityType.APPLICATION_MODAL);

			setTitle("Comparador de Detectores de Esquinas");
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setLocationRelativeTo(getParent());
		}

		private void createLayout() {

			dialogoEspera = new EspereDialog(ventana);
			botonConfirmar = new JButton("Listo");
			
			botonConfirmar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
			});
			
			botonConfirmar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
			
					AlgoritmoComparativoDialog.this.dispose();
					if (!sigmaElegido.getText().toString().isEmpty()) {
						
						try{
							final int sigma = Integer.valueOf(sigmaElegido.getText().toString());
							final String coleccion = (String) combo.getSelectedItem();
							
								if (ventana != null){
									
									Runnable r = new Runnable() {
								         public void run() {
								        	 try {
								        		 ventana.ejecutarAlgoritmoComparativo(coleccion, sigma);	 
												dialogoEspera.ocultar();
											} catch (IOException e) {
												e.printStackTrace();
											}
								         }
								     };

								     Thread ejecutar = new Thread(r);
								     ejecutar.start();
								     
								     dialogoEspera.mostrar();
								}
								
						} catch (Exception ex){
							
							DialogsHelper.mostarMensaje(jpanel, "No se ha podido aplicar el algoritmo comparativo", NivelMensaje.ERROR);
							ex.printStackTrace();
						}
					} else {
						
						DialogsHelper.mostarMensaje(jpanel, "Por favor ingresa un sigma v\u00e1lido", NivelMensaje.ERROR);
					}
				}
			});
			
			Container pane = getContentPane();
			GroupLayout gl = new GroupLayout(pane);
			pane.setLayout(gl);

			gl.setAutoCreateContainerGaps(true);
			gl.setAutoCreateGaps(true);

			gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER)
					.addComponent(comboLabel).addComponent(combo)
					.addComponent(ruidoLabel)
					.addComponent(labelSigma).addComponent(sigmaElegido)
					.addComponent(botonConfirmar).addGap(200));

			gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
					.addComponent(comboLabel).addGap(10).addComponent(combo).addGap(20)
					.addComponent(ruidoLabel).addGap(10)
					.addComponent(labelSigma).addGap(10).addComponent(sigmaElegido).addGap(20)
					.addComponent(botonConfirmar).addGap(30));

			pack();
		}
	
}
