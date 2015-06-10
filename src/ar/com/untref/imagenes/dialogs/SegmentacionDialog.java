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

@SuppressWarnings("serial")
public class SegmentacionDialog extends JDialog {

	private VentanaPrincipal ventana;
	private JButton botonConfirmar;
	private JLabel labelRepeticiones;
	private JLabel labelDiferenciaDeColor;
	private JTextField repeticiones;
	private JTextField diferenciaDeColor;
	private JPanel jpanel;

	public SegmentacionDialog(VentanaPrincipal ventana, JPanel jpanel) {
		super(ventana);
		this.ventana = ventana;
		this.jpanel = jpanel;
		initUI();
	}

	private void initUI() {

		labelRepeticiones = new JLabel("Repeticiones");
		labelDiferenciaDeColor = new JLabel("Diferencia de color (0-255)");
		repeticiones = new JTextField();
		diferenciaDeColor = new JTextField();

		createLayout();

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Segmentación");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
	}

	private void createLayout() {

		botonConfirmar = new JButton("Listo");

		botonConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!repeticiones.getText().toString().isEmpty() && !diferenciaDeColor.getText().toString().isEmpty()) {
					
					try{
						String rep = repeticiones.getText().toString().trim();
						String diferenciaColor = diferenciaDeColor.getText().toString().trim();
						
						if (!rep.isEmpty() && !diferenciaColor.isEmpty()){
							
							if (ventana != null){
								
								ventana.segmentarImagen(Integer.valueOf(rep), Integer.valueOf(diferenciaColor));
							}
							
							SegmentacionDialog.this.dispose();
						} else {
							
							DialogsHelper.mostarMensaje(jpanel, "Por favor complete los parámetros necesarios", NivelMensaje.ERROR);
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
				.addComponent(labelRepeticiones).addComponent(labelDiferenciaDeColor)
				.addComponent(repeticiones).addComponent(diferenciaDeColor)
				.addComponent(botonConfirmar).addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(labelRepeticiones).addGap(20).addComponent(repeticiones).addGap(20)
				.addComponent(labelDiferenciaDeColor).addGap(20).addComponent(diferenciaDeColor)
				.addGap(20).addComponent(botonConfirmar).addGap(30));

		pack();
	}
}
