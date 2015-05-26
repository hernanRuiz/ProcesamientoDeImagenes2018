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
public class HisteresisDialog extends JDialog {

	private VentanaPrincipal ventana;
	private JButton botonConfirmar;
	private JLabel labelUmbral1;
	private JLabel labelUmbral2;
	private JTextField umbral1Elegido;
	private JTextField umbral2Elegido;
	private VentanaRuido ventanaRuido;
	private JPanel jpanel;

	public HisteresisDialog(VentanaPrincipal ventana, JPanel jpanel) {
		super(ventana);
		this.ventana = ventana;
		this.jpanel = jpanel;
		initUI();
	}

	public HisteresisDialog(VentanaRuido ventanaRuido, JPanel jpanel) {
		super(ventanaRuido);
		this.ventanaRuido = ventanaRuido;
		this.jpanel = jpanel;
		initUI();
	}

	private void initUI() {

		labelUmbral1 = new JLabel("Umbral 1");
		labelUmbral2 = new JLabel("Umbral 2");
		umbral1Elegido = new JTextField();
		umbral2Elegido = new JTextField();

		createLayout();

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Umbralización con Histéresis");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
	}

	private void createLayout() {

		botonConfirmar = new JButton("Listo");

		botonConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!umbral1Elegido.getText().toString().isEmpty() && !umbral2Elegido.getText().toString().isEmpty()) {
					
					try{
						int umbral1 = Integer.valueOf(umbral1Elegido.getText().toString());
						int umbral2 = Integer.valueOf(umbral2Elegido.getText().toString());
						
						if (umbral1 < umbral2){
							
							if (ventana != null){
								
								ventana.umbralizarConHisteresis(umbral1, umbral2);
							}
							
							if (ventanaRuido != null){
								
								ventanaRuido.umbralizarConHisteresis(umbral1, umbral2);
							}
							
							HisteresisDialog.this.dispose();
						} else {
							
							DialogsHelper.mostarMensaje(jpanel, "El umbral 1 debe ser menor al umbral 2", NivelMensaje.ERROR);
						}
					} catch (Exception ex){
						
						DialogsHelper.mostarMensaje(jpanel, "Por favor ingresa umbrales válidos", NivelMensaje.ERROR);
						ex.printStackTrace();
					}
				} else {
					
					DialogsHelper.mostarMensaje(jpanel, "Por favor ingresa umbrales válidos", NivelMensaje.ERROR);
				}
			}
		});
		
		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER)
				.addComponent(labelUmbral1).addComponent(labelUmbral2)
				.addComponent(umbral1Elegido).addComponent(umbral2Elegido)
				.addComponent(botonConfirmar).addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(labelUmbral1).addGap(20).addComponent(umbral1Elegido).addGap(20)
				.addComponent(labelUmbral2).addGap(20).addComponent(umbral2Elegido)
				.addGap(20).addComponent(botonConfirmar).addGap(30));

		pack();
	}
}