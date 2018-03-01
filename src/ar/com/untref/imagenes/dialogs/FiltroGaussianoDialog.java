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
	public class FiltroGaussianoDialog extends JDialog {

		private VentanaPrincipal ventana;
		private JButton botonConfirmar;
		private JLabel labelSigma;
		private JTextField sigmaElegido;
		private VentanaRuido ventanaRuido;
		private JPanel jpanel;

		public FiltroGaussianoDialog(VentanaPrincipal ventana, JPanel jpanel) {
			super(ventana);
			this.ventana = ventana;
			this.jpanel = jpanel;
			initUI();
		}

		public FiltroGaussianoDialog(VentanaRuido ventanaRuido, JPanel jpanel) {
			super(ventanaRuido);
			this.ventanaRuido = ventanaRuido;
			this.jpanel = jpanel;
			initUI();
		}

		private void initUI() {

			labelSigma = new JLabel("Sigma");
			sigmaElegido = new JTextField();

			createLayout();

			setModalityType(ModalityType.APPLICATION_MODAL);

			setTitle("Filtro Gaussiano");
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setLocationRelativeTo(getParent());
		}

		private void createLayout() {

			botonConfirmar = new JButton("Listo");

			botonConfirmar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (!sigmaElegido.getText().toString().isEmpty()) {
						
						try{
							int sigma = Integer.valueOf(sigmaElegido.getText().toString());
							
								if (ventana != null){
									
									ventana.aplicarFiltroGaussiano(sigma);
								}
								
								if (ventanaRuido != null){
									
									ventanaRuido.aplicarFiltroGaussiano(sigma);
								}
								
								FiltroGaussianoDialog.this.dispose();
							
						} catch (Exception ex){
							
							DialogsHelper.mostarMensaje(jpanel, "Por favor ingresa umbrales v�lidos", NivelMensaje.ERROR);
							ex.printStackTrace();
						}
					} else {
						
						DialogsHelper.mostarMensaje(jpanel, "Por favor ingresa umbrales v�lidos", NivelMensaje.ERROR);
					}
				}
			});
			
			Container pane = getContentPane();
			GroupLayout gl = new GroupLayout(pane);
			pane.setLayout(gl);

			gl.setAutoCreateContainerGaps(true);
			gl.setAutoCreateGaps(true);

			gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER)
					.addComponent(labelSigma).addComponent(sigmaElegido)
					.addComponent(botonConfirmar).addGap(200));

			gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
					.addComponent(labelSigma).addGap(20).addComponent(sigmaElegido).addGap(20)
					.addComponent(botonConfirmar).addGap(30));

			pack();
		}
	}
