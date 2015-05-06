package ar.com.untref.imagenes.dialogs;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import ar.com.untref.imagenes.ventanas.VentanaPrincipal;
import ar.com.untref.imagenes.ventanas.VentanaRuido;

@SuppressWarnings("serial")
public class DifusionIsotropicaDialog extends JDialog {

	private VentanaPrincipal ventana;
	private JButton botonConfirmar;
	private JLabel labelSigma;
	private JTextField sigmaElegido;
	private JLabel labelRepeticiones;
	private JTextField repeticionElegida;
	private VentanaRuido ventanaRuido;

	public DifusionIsotropicaDialog(VentanaPrincipal ventana) {
		super(ventana);
		this.ventana = ventana;
		initUI();
	}

	public DifusionIsotropicaDialog(VentanaRuido ventanaRuido) {
		super(ventanaRuido);
		this.ventanaRuido = ventanaRuido;
		initUI();
	}

	private void initUI() {

		labelSigma = new JLabel("Sigma");
		labelRepeticiones = new JLabel("Repeticiones");
		sigmaElegido = new JTextField();
		repeticionElegida = new JTextField();

		createLayout();

		setModalityType(ModalityType.APPLICATION_MODAL);

		setTitle("Difusion Isotropica");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
	}

	private void createLayout() {

		botonConfirmar = new JButton("Listo");

		botonConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!sigmaElegido.getText().toString().isEmpty() && !repeticionElegida.getText().toString().isEmpty()) {
					
					try{
						int sigma = Integer.valueOf(sigmaElegido.getText().toString());
						int repeticiones = Integer.valueOf(repeticionElegida.getText().toString());
						
						if(ventana != null){
							ventana.aplicarDifusionIsotropica(sigma, repeticiones);							
						}
						
						if (ventanaRuido != null){
							ventanaRuido.aplicarDifusionIsotropica(sigma, repeticiones);							
						}
						
						
						DifusionIsotropicaDialog.this.dispose();
					} catch (Exception ex){
						
						ex.printStackTrace();
					}
				}
			}
		});
		
		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER)
				.addComponent(labelSigma).addComponent(labelRepeticiones)
				.addComponent(sigmaElegido).addComponent(repeticionElegida)
				.addComponent(botonConfirmar).addGap(200));

		gl.setVerticalGroup(gl.createSequentialGroup().addGap(30)
				.addComponent(labelSigma).addGap(20).addComponent(sigmaElegido).addGap(20)
				.addComponent(labelRepeticiones).addGap(20).addComponent(repeticionElegida)
				.addGap(20).addComponent(botonConfirmar).addGap(30));

		pack();
	}
}